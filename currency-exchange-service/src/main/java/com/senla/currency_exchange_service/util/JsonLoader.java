package com.senla.currency_exchange_service.util;


import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.InputStream;
import java.util.Map;

public class JsonLoader {

    public static Map<String, Double> loadRates(String fileName) {
        ObjectMapper mapper = new ObjectMapper();
        try (InputStream input = JsonLoader.class.getClassLoader().getResourceAsStream(fileName)) {
            if (input == null) {
                throw new IllegalArgumentException("Файл " + fileName + " не найден в resources");
            }
            return mapper.readValue(input, Map.class);
        } catch (Exception e) {
            throw new RuntimeException("Ошибка загрузки курсов валют: " + e.getMessage(), e);
        }
    }
}
