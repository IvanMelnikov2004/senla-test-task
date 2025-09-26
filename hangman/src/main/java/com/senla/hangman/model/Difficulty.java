package com.senla.hangman.model;

public enum Difficulty {
    EASY(10),
    NORMAL(7),
    HARD(5);

    private final int attempts;

    Difficulty(int attempts) {
        this.attempts = attempts;
    }

    public int getAttempts() {
        return attempts;
    }
}
