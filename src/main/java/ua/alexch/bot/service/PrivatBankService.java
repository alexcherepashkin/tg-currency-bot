package ua.alexch.bot.service;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import ua.alexch.bot.model.ExchangeRate;
import ua.alexch.bot.util.TgUtil;

@Service
public class PrivatBankService implements SearchSource {
    private static final Logger LOGGER = LoggerFactory.getLogger(PrivatBankService.class);
    private static final String RUB_CODE = "RUR";
    private static final String CURR_FIELD_CODE = "ccy";

    private final RestTemplate restTemplate;

    @Value("${url.privatbank}")
    private String url;

    @Autowired
    public PrivatBankService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public Optional<ExchangeRate> getCurrencyRateData(String currency) {
        final ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.USE_JAVA_ARRAY_FOR_JSON_ARRAY, true);

        String json = retrieveData(url);

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
            LOGGER.error("Failed to deserialize content from given JSON string: " + json, e);
        }

        return Optional.ofNullable(result);
    }

    private String retrieveData(String url) {
//        return restTemplate.getForObject(url, String.class);
        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
        return response.getStatusCode() == HttpStatus.OK ? response.getBody() : "";
    }

    @Override
    public String sourceIdentifier() {
        return TgUtil.PRIVAT_BANK;
    }
}
