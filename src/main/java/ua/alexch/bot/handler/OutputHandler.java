package ua.alexch.bot.handler;

import static ua.alexch.bot.util.HandlerUtil.createKeyboardStartButton;
import static ua.alexch.bot.util.HandlerUtil.createSendMessage;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import ua.alexch.bot.model.ExchangeRate;
import ua.alexch.bot.model.TgUser;
import ua.alexch.bot.repository.TemporalStorage;
import ua.alexch.bot.service.SearchSource;
import ua.alexch.bot.util.TgUtil;

@Service
public class OutputHandler implements Handler {
    private static final String OUTPUT_TEXT = "Курс на %1$td/%1$tm/%1$ty для %2$s/UAH:\nПокупка - %3$s / Продажа - %4$s";

    private final TemporalStorage tempStorage;
    private final List<SearchSource> sourceServices;

    @Autowired
    public OutputHandler(TemporalStorage temporalStorage, List<SearchSource> sourceServices) {
        this.tempStorage = temporalStorage;
        this.sourceServices = sourceServices;
    }

    @Override
    public List<PartialBotApiMethod<? extends Serializable>> handle(TgUser user, String selectedSrc) {
        final String selectedCurrency = tempStorage.getCurrency(user.getUserId());
        if (selectedCurrency == null) {
            return Collections.emptyList();
        }

        Optional<ExchangeRate> rateData = getServiceBySource(selectedSrc).getCurrencyRateData(selectedCurrency);

        if (!rateData.isPresent()) {
            return Collections.emptyList();
        }

        SendMessage outMessage = createSendMessage(user.getUserId(), formatMessageText(rateData.get()));
        outMessage.setReplyMarkup(createKeyboardStartButton());

        return Arrays.asList(outMessage);
    }

    // @formatter:off
    private String formatMessageText(ExchangeRate data) {
        return String.format(OUTPUT_TEXT,
                LocalDate.now(), data.getCurrency(), Double.parseDouble(data.getBuyRate()), Double.parseDouble(data.getSaleRate()));
    }

    private SearchSource getServiceBySource(String selectedSrc) {
        return sourceServices.stream()
                .filter(service -> service.sourceIdentifier().equals(selectedSrc))
                .findFirst()
                .orElseThrow(UnsupportedOperationException::new);
    }
    // @formatter:on

    @Override
    public List<String> callBackQueries() {
        return TgUtil.SOURCE_NAMES;
    }

}
