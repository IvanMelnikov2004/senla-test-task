package com.senla.currency_exchange_service;


import com.senla.currency_exchange_service.util.JsonLoader;
import org.junit.jupiter.api.Test;
import java.util.Map;
import static org.junit.jupiter.api.Assertions.*;

class JsonLoaderTest {

    @Test
    void testLoadRatesSuccessfully() {
        Map<String, Double> rates = JsonLoader.loadRates("rates.json");
        assertNotNull(rates);
        assertTrue(rates.containsKey("USD"));
        assertEquals(1.0, rates.get("USD"), 0.0001);
    }

    @Test
    void testLoadRatesFileNotFound() {
        Exception exception = assertThrows(RuntimeException.class,
                () -> JsonLoader.loadRates("not_exists.json"));
        assertTrue(exception.getMessage().contains("не найден в resources"));
    }
}
