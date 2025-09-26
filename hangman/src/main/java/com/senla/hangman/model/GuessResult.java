package com.senla.hangman.model;

import java.util.Set;

public record GuessResult(
        GuessStatus status,
        String maskedWord,
        int attemptsLeft,
        String message,
        Set<Character> guessedLetters
) {}

