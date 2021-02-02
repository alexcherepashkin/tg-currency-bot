package ua.alexch.bot.util;

import java.util.Arrays;
import java.util.List;

public final class TgUtil {
    public static final String BASE_CURR = "UAH";
    public static final String EUR = "EUR";
    public static final String USD = "USD";
    public static final String RUB = "RUB";

    public static final String PRIVAT_BANK = "ПриватБанк";
    public static final String OSCHAD_BANK = "ОЩАДБАНК";
    public static final String MONEY24 = "Money24";

    public static final String START_COMMAND = "/start";

    public static final List<String> CURRENCY_NAMES = Arrays.asList(EUR, USD, RUB);

    public static final List<String> SOURCE_NAMES = Arrays.asList(PRIVAT_BANK, OSCHAD_BANK, MONEY24);

}
