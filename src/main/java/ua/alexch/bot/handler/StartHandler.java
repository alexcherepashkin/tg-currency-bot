package ua.alexch.bot.handler;

import static ua.alexch.bot.util.HandlerUtil.createKeyboardMarkupOneRow;
import static ua.alexch.bot.util.HandlerUtil.createSendMessage;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import ua.alexch.bot.model.TgUser;
import ua.alexch.bot.util.TgUtil;

@Service
public class StartHandler implements Handler {
    private static final String WELCOME = "Доброго времени суток, *%s*!";
    private static final String ASK_TEXT = "Выберите валюту, которую будем искать:";

    @Override
    public List<PartialBotApiMethod<? extends Serializable>> handle(TgUser user, String message) {
        final String welcome = String.format(WELCOME, user.getName());

        SendMessage welcomeMessage = createSendMessage(user.getUserId(), welcome);
        SendMessage askMessage = createSendMessage(user.getUserId(), ASK_TEXT);
        askMessage.setReplyMarkup(createKeyboardMarkupOneRow(TgUtil.CURRENCY_NAMES));

        return Arrays.asList(welcomeMessage, askMessage);
    }

    @Override
    public List<String> callBackQueries() {
        return Arrays.asList(TgUtil.START_COMMAND);
    }

}
