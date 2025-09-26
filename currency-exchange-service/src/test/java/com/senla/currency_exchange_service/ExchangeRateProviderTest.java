package com.senla.currency_exchange_service;


import com.senla.currency_exchange_service.model.CurrencyRate;
import com.senla.currency_exchange_service.service.ExchangeRateProvider;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ExchangeRateProviderTest {
    private static ExchangeRateProvider provider;

    @BeforeAll
    static void setup() {
        provider = new ExchangeRateProvider("rates.json");
    }

    @Test
    void testGetExistingCurrency() {
        CurrencyRate usd = provider.getRate("USD");
        assertEquals("USD", usd.getCode());
        assertEquals(1.0, usd.getRate(), 0.0001);
    }

    @Test
    void testGetAllCodesContainsRub() {
        assertTrue(provider.getAllCodes().contains("RUB"));
    }

    @Test
    void testGetNonExistingCurrencyThrowsException() {
        assertThrows(IllegalArgumentException.class,
                () -> provider.getRate("ABC"));
    }
}

