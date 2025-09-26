package com.senla.currency_exchange_service.service;



import com.senla.currency_exchange_service.model.CurrencyRate;
import com.senla.currency_exchange_service.util.JsonLoader;

import java.util.HashMap;
import java.util.Map;

import java.util.*;

public class ExchangeRateProvider {
    private final Map<String, CurrencyRate> rates = new HashMap<>();

    public ExchangeRateProvider(String resourceFile) {
        Map<String, Double> rawRates = JsonLoader.loadRates(resourceFile);
        rawRates.forEach((code, rate) ->
                rates.put(code.toUpperCase(), new CurrencyRate(code.toUpperCase(), rate)));
    }

    public CurrencyRate getRate(String currencyCode) {
        CurrencyRate rate = rates.get(currencyCode.toUpperCase());
        if (rate == null) {
            throw new IllegalArgumentException("Валюта " + currencyCode + " не найдена");
        }
        return rate;
    }

    public Set<String> getAllCodes() {
        return Collections.unmodifiableSet(rates.keySet());
    }
}