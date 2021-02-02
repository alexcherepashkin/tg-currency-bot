package ua.alexch.bot.service;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import ua.alexch.bot.model.ExchangeRate;
import ua.alexch.bot.util.TgUtil;

@Service
public class PrivatBankSource implements SearchSource {
    private static final Logger LOGGER = LoggerFactory.getLogger(PrivatBankSource.class);
    private static final String RUB_CODE = "RUR";
    private static final String CURR_FIELD_CODE = "ccy";

    private final RestTemplate restTemplate;

    @Value("${url.privatbank}")
    private String url;

    @Autowired
    public PrivatBankSource(RestTemplateBuilder templateBuilder) {
        this.restTemplate = templateBuilder.build();
    }

    @Override
    public Optional<ExchangeRate> getCurrencyRateData(String currency) {
        String json = restTemplate.getForObject(url, String.class);

        return retrieveData(json, currency);
    }

    private Optional<ExchangeRate> retrieveData(String json, String currency) {
        final ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.USE_JAVA_ARRAY_FOR_JSON_ARRAY, true);

        ExchangeRate result = null;
        try {
            JsonNode[] nodes = mapper.readValue(json, JsonNode[].class);
            // PrivatBank specific case!
            currency = currency.equals(TgUtil.RUB) ? RUB_CODE : currency;

            for (JsonNode obj : nodes) {
                String curr = obj.findValue(CURR_FIELD_CODE).asText();
                if (curr.contains(currency)) {
                    result = mapper.convertValue(obj, ExchangeRate.class);
                }
            }
        } catch (JsonProcessingException e) {
            LOGGER.error("Failed to retrieve data from request: " + url, e);
        }

        return Optional.ofNullable(result);
    }

    @Override
    public String sourceIdentifier() {
        return TgUtil.PRIVAT_BANK;
    }
}
