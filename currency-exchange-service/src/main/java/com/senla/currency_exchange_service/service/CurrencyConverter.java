package com.senla.currency_exchange_service.service;

public class CurrencyConverter {
    private final ExchangeRateProvider provider;

    public CurrencyConverter(ExchangeRateProvider provider) {
        this.provider = provider;
    }

    public double convert(String from, String to, double amount) {
        double fromRate = provider.getRate(from).getRate();
        double toRate = provider.getRate(to).getRate();
        return amount * (toRate / fromRate);
    }
}
