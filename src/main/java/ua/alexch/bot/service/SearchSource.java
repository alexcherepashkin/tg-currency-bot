package ua.alexch.bot.service;

import java.util.Optional;

import ua.alexch.bot.model.ExchangeRate;

public interface SearchSource {

    Optional<ExchangeRate> getCurrencyRateData(String currency);

    String sourceIdentifier();
}
