package com.senla.hangman.service;


import com.senla.hangman.model.*;
import com.senla.hangman.repository.WordRepository;

import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;

public class GameService {

    private final WordRepository wordRepository;
    private final Random random = new Random();

    public GameService(WordRepository wordRepository) {
        this.wordRepository = wordRepository;
    }

    /**
     * Создает и инициализирует новую игровую сессию.
     * @param difficulty Уровень сложности, определяющий количество попыток.
     * @return Новый объект GameSession.
     * @throws IllegalStateException если не удалось загрузить слова.
     */
    public GameSession startNewGame(Difficulty difficulty) {
        List<WordEntry> words = wordRepository.loadWords();
        if (words == null || words.isEmpty()) {
            throw new IllegalStateException("Словарь пуст! Невозможно начать игру.");
        }
        WordEntry randomWord = words.get(random.nextInt(words.size()));
        return new GameSession(randomWord, difficulty);
    }

    /**
     * Обрабатывает попытку угадать букву.
     * @param session Текущая игровая сессия (будет изменена).
     * @param letter Буква, которую ввел игрок.
     * @return Объект GuessResult с результатами хода.
     */
    public GuessResult makeGuess(GameSession session, char letter) {
        char lowerCaseLetter = Character.toLowerCase(letter);

        if (isGameFinished(session)) {
            return new GuessResult(GuessStatus.GAME_LOST, getMaskedWord(session), session.getAttemptsLeft(), "Игра уже окончена.", session.getGuessedLetters());
        }

        if (isLetterAlreadyGuessed(session, lowerCaseLetter)) {
            return new GuessResult(GuessStatus.ALREADY_GUESSED, getMaskedWord(session), session.getAttemptsLeft(), "Вы уже называли эту букву.", session.getGuessedLetters());
        }

        session.addGuessedLetter(lowerCaseLetter);

        if (isLetterCorrect(session, lowerCaseLetter)) {
            // Буква угадана верно
            if (isWordFullyGuessed(session)) {
                session.setStatus(GameStatus.WON);
                return new GuessResult(GuessStatus.GAME_WON, session.getWordEntry().word(), session.getAttemptsLeft(), "Поздравляем, вы победили!", session.getGuessedLetters());
            } else {
                return new GuessResult(GuessStatus.CORRECT, getMaskedWord(session), session.getAttemptsLeft(), "Отлично! Такая буква есть.", session.getGuessedLetters());
            }
        } else {
            // Буква угадана неверно
            session.decrementAttempts();
            if (hasNoAttemptsLeft(session)) {
                session.setStatus(GameStatus.LOST);
                String message = "Попытки закончились! Вы проиграли. Загаданное слово: " + session.getWordEntry().word();
                return new GuessResult(GuessStatus.GAME_LOST, getMaskedWord(session), 0, message, session.getGuessedLetters());
            } else {
                String message = "Увы, такой буквы нет. Осталось попыток: " + session.getAttemptsLeft();
                return new GuessResult(GuessStatus.INCORRECT, getMaskedWord(session), session.getAttemptsLeft(), message, session.getGuessedLetters());
            }
        }
    }

    /**
     * Предоставляет подсказку игроку.
     * @param session Текущая игровая сессия (будет изменена).
     * @return Строка с подсказкой или сообщение о том, что она уже использована.
     */
    public String requestHint(GameSession session) {
        if (isHintAlreadyUsed(session)) {
            return "Подсказка уже была использована.";
        }

        session.setHintUsed(true);
        return "Подсказка: " + session.getWordEntry().hint();
    }

    // ========== ПРЕДИКАТЫ ==========

    private boolean isGameFinished(GameSession session) {
        return session.getStatus() != GameStatus.IN_PROGRESS;
    }

    private boolean isLetterAlreadyGuessed(GameSession session, char letter) {
        return session.getGuessedLetters().contains(letter);
    }

    private boolean isLetterCorrect(GameSession session, char letter) {
        String secretWord = session.getWordEntry().word().toLowerCase();
        return secretWord.indexOf(letter) >= 0;
    }

    private boolean isWordFullyGuessed(GameSession session) {
        String secretWord = session.getWordEntry().word().toLowerCase();
        Set<Character> guessedLetters = session.getGuessedLetters();

        for (char c : secretWord.toCharArray()) {
            if (!guessedLetters.contains(c)) {
                return false;
            }
        }
        return true;
    }

    private boolean hasNoAttemptsLeft(GameSession session) {
        return session.getAttemptsLeft() <= 0;
    }

    private boolean isHintAlreadyUsed(GameSession session) {
        return session.isHintUsed();
    }

    // ========== ВСПОМОГАТЕЛЬНЫЕ МЕТОДЫ ==========

    private String getMaskedWord(GameSession session) {
        String secretWord = session.getWordEntry().word();
        Set<Character> guessedLetters = session.getGuessedLetters();

        return secretWord.chars()
                .mapToObj(c -> (char) c)
                .map(c -> guessedLetters.contains(Character.toLowerCase(c)) ? c : '_')
                .map(String::valueOf)
                .collect(Collectors.joining(" "));
    }
}
