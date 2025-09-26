package com.senla.currency_exchange_service.model;

public class CurrencyRate {
    private final String code;
    private final double rate;

    public CurrencyRate(String code, double rate) {
        this.code = code;
        this.rate = rate;
    }

    public String getCode() {
        return code;
    }

    public double getRate() {
        return rate;
    }
}
