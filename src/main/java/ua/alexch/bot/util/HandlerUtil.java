package ua.alexch.bot.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

public final class HandlerUtil {
    private HandlerUtil() {
    }

    public static SendMessage createSendMessage(int userId, String text) {
        SendMessage sendMessage = SendMessage.builder().chatId(String.valueOf(userId)).text(text).build();
        sendMessage.enableMarkdown(true);
        return sendMessage;
    }

    public static InlineKeyboardMarkup createKeyboardMarkupRows(List<String> names) {
        InlineKeyboardMarkup inlineKeyboard = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> buttonsRows = new ArrayList<>();

        names.forEach(name -> buttonsRows.add(Arrays.asList(createKeyboardButton(name, name))));

        inlineKeyboard.setKeyboard(buttonsRows);

        return inlineKeyboard;
    }

    public static InlineKeyboardMarkup createKeyboardMarkupOneRow(List<String> names) {
        InlineKeyboardMarkup inlineKeyboard = new InlineKeyboardMarkup();
        List<InlineKeyboardButton> buttonsOneRow = new ArrayList<>();

        names.forEach(name -> buttonsOneRow.add(createKeyboardButton(name, name)));

        inlineKeyboard.setKeyboard(Arrays.asList(buttonsOneRow));

        return inlineKeyboard;
    }

    public static InlineKeyboardMarkup createKeyboardStartButton() {
        InlineKeyboardMarkup inlineKeyboard = new InlineKeyboardMarkup();

        List<InlineKeyboardButton> startButton = Arrays.asList(createKeyboardButton("-=START=-", TgUtil.START_COMMAND));
        inlineKeyboard.setKeyboard(Arrays.asList(startButton));

        return inlineKeyboard;
    }

    public static InlineKeyboardButton createKeyboardButton(String text, String command) {
        return InlineKeyboardButton.builder().text(text).callbackData(command).build();
    }
}
