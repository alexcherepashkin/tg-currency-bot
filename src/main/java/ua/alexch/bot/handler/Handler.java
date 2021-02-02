package ua.alexch.bot.handler;

import java.io.Serializable;
import java.util.List;

import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;

import ua.alexch.bot.model.TgUser;

public interface Handler {

    List<PartialBotApiMethod<? extends Serializable>> handle(TgUser user, String message);

    List<String> callBackQueries();
}
