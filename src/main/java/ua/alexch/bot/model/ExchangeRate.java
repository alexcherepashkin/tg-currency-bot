package ua.alexch.bot.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ExchangeRate {
    private String currency;
    private String buyRate;
    private String saleRate;

    public ExchangeRate() {
    }

    public String getCurrency() {
        return currency;
    }

    @JsonProperty(value = "ccy")
    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getBuyRate() {
        return buyRate;
    }

    @JsonProperty(value = "buy")
    public void setBuyRate(String buyRate) {
        this.buyRate = buyRate;
    }

    public String getSaleRate() {
        return saleRate;
    }

    @JsonProperty(value = "sale")
    public void setSaleRate(String saleRate) {
        this.saleRate = saleRate;
    }

}
