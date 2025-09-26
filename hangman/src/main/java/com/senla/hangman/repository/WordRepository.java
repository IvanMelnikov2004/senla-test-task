package com.senla.hangman.repository;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.senla.hangman.model.WordEntry;

import java.io.InputStream;
import java.util.Collections;
import java.util.List;

public class WordRepository {

    private final String fileName;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public WordRepository(String fileName) {
        this.fileName = fileName;
    }

    public List<WordEntry> loadWords() {
        try (InputStream is = getClass().getClassLoader().getResourceAsStream(fileName)) {
            if (is == null) {
                System.err.println("Файл " + fileName + " не найден в resources!");
                return Collections.emptyList();
            }
            return objectMapper.readValue(is, new TypeReference<>() {});
        } catch (Exception e) {
            System.err.println("Ошибка при чтении файла: " + e.getMessage());
            return Collections.emptyList();
        }
    }
}
