package ua.alexch.bot.service;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.client.ExpectedCount;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.test.web.client.match.MockRestRequestMatchers;
import org.springframework.test.web.client.response.MockRestResponseCreators;
import org.springframework.web.client.RestTemplate;

import ua.alexch.bot.model.ExchangeRate;
import ua.alexch.bot.util.TgUtil;

@ExtendWith(SpringExtension.class)
@TestPropertySource("/application-test.properties")
@SpringBootTest
class PrivatBankServiceTest {
    private static final String BODY_JSON = "[{\"ccy\":\"USD\",\"base_ccy\":\"UAH\",\"buy\":\"27.75000\",\"sale\":\"28.17000\"},{\"ccy\":\"EUR\",\"base_ccy\":\"UAH\",\"buy\":\"33.35000\",\"sale\":\"33.95000\"}]";

    @Value("${url.privatbank}")
    private String url;

    @Autowired
    private PrivatBankService service;

    @Autowired
    private RestTemplate restTemplate;

    private MockRestServiceServer mockServer;

    Optional<ExchangeRate> emptyResult;

    @BeforeEach
    public void init() {
        mockServer = MockRestServiceServer.createServer(restTemplate);
    }

    @Test
    void shouldReturnExchangeRateWhenGivenCurrency() {
        final String currency = TgUtil.EUR;
        final String buy = "33.35000";
        final String sale = "33.95000";

        mockServer.expect(ExpectedCount.once(), MockRestRequestMatchers.requestTo(url))
                .andExpect(MockRestRequestMatchers.method(HttpMethod.GET))
                .andRespond(MockRestResponseCreators.withSuccess().contentType(MediaType.APPLICATION_JSON).body(BODY_JSON));

        ExchangeRate actualRate = service.getCurrencyRateData(currency).get();

        mockServer.verify();

        assertEquals(currency, actualRate.getCurrency());
        assertEquals(buy, actualRate.getBuyRate());
        assertEquals(sale, actualRate.getSaleRate());
    }

    @Test
    void shouldReturnEmptyResultWhenGivenInvalidCurrency() {
        String currency = "invalid_currency";

        mockServer.expect(ExpectedCount.once(), MockRestRequestMatchers.requestTo(url))
                .andExpect(MockRestRequestMatchers.method(HttpMethod.GET))
                .andRespond(MockRestResponseCreators.withSuccess().contentType(MediaType.APPLICATION_JSON).body(BODY_JSON));

        assertDoesNotThrow(() -> emptyResult = service.getCurrencyRateData(currency));
        assertFalse(emptyResult.isPresent());
    }
}
