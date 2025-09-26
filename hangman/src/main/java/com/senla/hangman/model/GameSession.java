package com.senla.hangman.model;

import java.util.HashSet;
import java.util.HashSet;
import java.util.Set;

public class GameSession {
    private final WordEntry wordEntry;
    private final Set<Character> guessedLetters;
    private int attemptsLeft;
    private GameStatus status;
    private boolean isHintUsed;

    public GameSession(WordEntry wordEntry, Difficulty difficulty) {
        this.wordEntry = wordEntry;
        this.attemptsLeft = difficulty.getAttempts();
        this.guessedLetters = new HashSet<>();
        this.status = GameStatus.IN_PROGRESS;
        this.isHintUsed = false;
    }


    public WordEntry getWordEntry() { return wordEntry; }
    public Set<Character> getGuessedLetters() { return Set.copyOf(guessedLetters); } // Возвращаем копию для безопасности
    public int getAttemptsLeft() { return attemptsLeft; }
    public GameStatus getStatus() { return status; }
    public boolean isHintUsed() { return isHintUsed; }


    public void addGuessedLetter(char letter) {
        this.guessedLetters.add(letter);
    }

    public void decrementAttempts() {
        this.attemptsLeft--;
    }

    public void setStatus(GameStatus status) {
        this.status = status;
    }

    public void setHintUsed(boolean isHintUsed) {
        this.isHintUsed = isHintUsed;
    }
}
