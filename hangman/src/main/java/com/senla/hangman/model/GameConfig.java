package com.senla.hangman.model;

public class GameConfig {
    // Файл должен лежать в src/main/resources
    private final String wordsFileName = "hangman_words.json";

    public String getWordsFileName() {
        return wordsFileName;
    }
}
