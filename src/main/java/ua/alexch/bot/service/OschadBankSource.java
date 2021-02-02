package ua.alexch.bot.service;

import java.io.IOException;
import java.util.Optional;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import ua.alexch.bot.model.ExchangeRate;
import ua.alexch.bot.util.TgUtil;

@Service
public class OschadBankSource implements SearchSource {
    private static final Logger LOGGER = LoggerFactory.getLogger(OschadBankSource.class);
    private static final String BUY_CURR = "buy-%s";
    private static final String SELL_CURR = "sell-%s";

    @Value("${url.oschadbank}")
    private String url;

    @Override
    public Optional<ExchangeRate> getCurrencyRateData(String currency) {
        ExchangeRate result = null;

        try {
            Document html = Jsoup.connect(url).get();

            Elements buyInfo = html.getElementsByClass(String.format(BUY_CURR, currency));
            Elements sellInfo = html.getElementsByClass(String.format(SELL_CURR, currency));

            if (buyInfo.size() == 1 && buyInfo.size() == 1 && buyInfo.hasText() && sellInfo.hasText()) {
                result = new ExchangeRate();
                result.setCurrency(currency);
                result.setBuyRate(buyInfo.text());
                result.setSaleRate(sellInfo.text());
            }
        } catch (IOException e) {
            LOGGER.error("Failed to execute request for url=" + url, e);
        }
        return Optional.ofNullable(result);
    }

    @Override
    public String sourceIdentifier() {
        return TgUtil.OSCHAD_BANK;
    }
}
