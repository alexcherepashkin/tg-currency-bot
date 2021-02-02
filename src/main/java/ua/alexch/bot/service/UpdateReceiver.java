package ua.alexch.bot.service;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;

import ua.alexch.bot.handler.Handler;
import ua.alexch.bot.model.TgUser;
import ua.alexch.bot.repository.TgUserRepository;

@Service
public class UpdateReceiver {
    private static final Logger LOGGER = LoggerFactory.getLogger(UpdateReceiver.class);
    private final List<Handler> handlers;
    private final TgUserRepository userRepository;

    @Autowired
    public UpdateReceiver(List<Handler> handlers, TgUserRepository userRepository) {
        this.handlers = handlers;
        this.userRepository = userRepository;
    }

    public List<PartialBotApiMethod<? extends Serializable>> handle(Update update) {
        try {
            if (isTextMessage(update)) {
                Message message = update.getMessage();
                int userId = message.getFrom().getId();

                TgUser user = userRepository.findByUserId(userId)
                        .orElseGet(() -> saveNewUser(message.getFrom()));

                return selectHandlerBy(message.getText()).handle(user, message.getText());

            }
            else if (update.hasCallbackQuery()) {
                CallbackQuery callbackQuery = update.getCallbackQuery();
                int userId = callbackQuery.getFrom().getId();

                TgUser user = userRepository.findByUserId(userId)
                        .orElseGet(() -> saveNewUser(callbackQuery.getFrom()));

                String query = callbackQuery.getData();

                return selectHandlerBy(query).handle(user, query);
            }

        } catch (UnsupportedOperationException e) {
            LOGGER.error("No suitable handler found", e);
        }
        return Collections.emptyList();
    }

    // @formatter:off
    private Handler selectHandlerBy(String query) {
        return handlers.stream()
                .filter(handler -> handler.callBackQueries().stream()
                        .anyMatch(q -> q.equalsIgnoreCase(query)))
                .findFirst()
                .orElseThrow(UnsupportedOperationException::new);
    }

    private TgUser saveNewUser(User user) {
        LOGGER.info("Register new user with name: " + user.getFirstName());
        
        return userRepository
                .save(new TgUser(user.getId(), user.getFirstName(), user.getLastName()));
    }

    private boolean isTextMessage(Update update) {
        return !update.hasCallbackQuery() &&
                update.hasMessage() &&
                update.getMessage().hasText();
    }
    // @formatter:on
}
