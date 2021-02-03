package ua.alexch.bot.service;

import java.io.IOException;
import java.util.Optional;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import ua.alexch.bot.model.ExchangeRate;
import ua.alexch.bot.util.TgUtil;

@Service
public class Money24Service implements SearchSource {
    private static final Logger LOGGER = LoggerFactory.getLogger(Money24Service.class);
    private static final String RATE_INFO = "tabs-1";
    private static final String RATE_DETAILS = "rate-number";
    private static final String CURRENCY_STR = "%s%s-uah";

    @Value("${url.money24}")
    private String url;

    @Override
    public Optional<ExchangeRate> getCurrencyRateData(String currency) {
        final String getUrl = String.format(CURRENCY_STR, url, currency.toLowerCase());

        ExchangeRate result = null;
        try {
            Document html = Jsoup.connect(getUrl).get();

            Element rateInfo = html.getElementById(RATE_INFO);
            Elements details = rateInfo.getElementsByClass(RATE_DETAILS);

            if (details.size() == 2 && details.hasText()) {
                result = new ExchangeRate();
                result.setCurrency(currency);
                result.setBuyRate(details.first().text());
                result.setSaleRate(details.last().text());
            }
        } catch (IOException e) {
            LOGGER.error("Failed to execute request for url=" + getUrl, e);
        }
        return Optional.ofNullable(result);
    }

    @Override
    public String sourceIdentifier() {
        return TgUtil.MONEY24;
    }
}
