package ua.alexch.bot.handler;

import static ua.alexch.bot.util.HandlerUtil.createKeyboardMarkupRows;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import ua.alexch.bot.model.TgUser;
import ua.alexch.bot.repository.TemporalStorage;
import ua.alexch.bot.util.HandlerUtil;
import ua.alexch.bot.util.TgUtil;

@Service
public class SourceHandler implements Handler {
    private static final String CURR_TEXT = "Ваш выбор: %s";
    private static final String ASK_TEXT = "Выберите источник данных:";

    private final TemporalStorage tempStorage;

    @Autowired
    public SourceHandler(TemporalStorage temporalStorage) {
        this.tempStorage = temporalStorage;
    }

    @Override
    public List<PartialBotApiMethod<? extends Serializable>> handle(TgUser user, String selectedCurrency) {
        final String currText = String.format(CURR_TEXT, selectedCurrency);

        tempStorage.saveCurrency(user.getUserId(), selectedCurrency);

        SendMessage currMessage = HandlerUtil.createSendMessage(user.getUserId(), currText);
        SendMessage askMessage = HandlerUtil.createSendMessage(user.getUserId(), ASK_TEXT);
        askMessage.setReplyMarkup(createKeyboardMarkupRows(TgUtil.SOURCE_NAMES));

        return Arrays.asList(currMessage, askMessage);
    }

    @Override
    public List<String> callBackQueries() {
        return TgUtil.CURRENCY_NAMES;
    }

}
