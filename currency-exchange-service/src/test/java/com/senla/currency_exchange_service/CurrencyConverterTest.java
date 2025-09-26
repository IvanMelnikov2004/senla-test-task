package com.senla.currency_exchange_service;



import com.senla.currency_exchange_service.service.CurrencyConverter;
import com.senla.currency_exchange_service.service.ExchangeRateProvider;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CurrencyConverterTest {
    private static CurrencyConverter converter;

    @BeforeAll
    static void setup() {
        ExchangeRateProvider provider = new ExchangeRateProvider("rates.json");
        converter = new CurrencyConverter(provider);
    }

    @Test
    void testUsdToRub() {
        double result = converter.convert("USD", "RUB", 10);
        assertTrue(result > 0, "Результат должен быть положительным");
    }

    @Test
    void testEurToUsd() {
        double result = converter.convert("EUR", "USD", 100);
        // 100 EUR = ~105.26 USD при rate EUR=0.95
        assertEquals(105.26, result, 0.5);
    }

    @Test
    void testSameCurrencyConversion() {
        double result = converter.convert("USD", "USD", 50);
        assertEquals(50, result, 0.0001);
    }
}
