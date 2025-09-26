package com.senla.hangman.ui;

import com.senla.hangman.model.*;
import com.senla.hangman.service.GameService;

import java.util.Scanner;
import java.util.stream.Collectors;

public class ConsoleManager {
    public ConsoleManager(GameService gameService, Scanner scanner) {
        this.gameService = gameService;
        this.scanner = scanner;
    }

    private final GameService gameService;
    private final Scanner scanner;

    // Массив с ASCII-артом для виселицы
    private static final String[] HANGMAN_PICS = {
            // 0 ошибок
            """
       +---+
       |   |
           |
           |
           |
           |
    =========""",
            // 1 ошибка
            """
       +---+
       |   |
       O   |
           |
           |
           |
    =========""",
            // 2 ошибки
            """
       +---+
       |   |
       O   |
       |   |
           |
           |
    =========""",
            // ... и так далее до 10 ошибок
            // 3 ошибки
            """
       +---+
       |   |
       O   |
      /|   |
           |
           |
    =========""",
            // 4 ошибки
            """
       +---+
       |   |
       O   |
      /|\\  |
           |
           |
    =========""",
            // 5 ошибок
            """
       +---+
       |   |
       O   |
      /|\\  |
      /    |
           |
    =========""",
            // 6 ошибок
            """
       +---+
       |   |
       O   |
      /|\\  |
      / \\  |
           |
    =========""",
            // 7 ошибок - для сложности Normal
            """
       +---+
       |   |
      [O]  |
      /|\\  |
      / \\  |
           |
    =========""",
            // 8, 9, 10 ошибок - для сложности Easy
            """
       +---+
       |   |
      [O]--|
      /|\\  |
      / \\  |
           |
    =========""",
            """
       +---+
       |   |
      [O]--|
      /|\\  |
     //\\\\ |
           |
    =========""",
            """
       +---+
       |   |
      [X]--|
      /|\\  |
     //\\\\ |
           |
    ========="""
    };

    public ConsoleManager(GameService gameService) {
        this.gameService = gameService;
        this.scanner = new Scanner(System.in);
    }

    public void run() {
        System.out.println("Добро пожаловать в игру 'Виселица'!");
        while (true) {
            System.out.println("\nВыберите действие:");
            System.out.println("1. Начать новую игру");
            System.out.println("2. Выход");
            System.out.print("> ");

            String choice = scanner.nextLine();
            switch (choice) {
                case "1" -> startNewGameLoop();
                case "2" -> {
                    System.out.println("До встречи!");
                    return;
                }
                default -> System.out.println("Неверный ввод. Пожалуйста, выберите 1 или 2.");
            }
        }
    }

    private void startNewGameLoop() {
        Difficulty difficulty = selectDifficulty();
        try {
            GameSession session = gameService.startNewGame(difficulty);
            playGame(session);
        } catch (IllegalStateException e) {
            System.err.println("Ошибка! " + e.getMessage());
            System.err.println("Пожалуйста, убедитесь, что файл " + new GameConfig().getWordsFileName() + " существует и содержит слова.");
        }
    }

    private Difficulty selectDifficulty() {
        while (true) {
            System.out.println("\nВыберите уровень сложности:");
            System.out.println("1. Легкий (10 попыток)");
            System.out.println("2. Нормальный (7 попыток)");
            System.out.println("3. Сложный (5 попыток)");
            System.out.print("> ");

            switch (scanner.nextLine()) {
                case "1" -> { return Difficulty.EASY; }
                case "2" -> { return Difficulty.NORMAL; }
                case "3" -> { return Difficulty.HARD; }
                default -> System.out.println("Неверный ввод. Пожалуйста, введите число от 1 до 3.");
            }
        }
    }

    private void playGame(GameSession session) {
        System.out.println("\nИгра началась! Слово состоит из " + session.getWordEntry().word().length() + " букв.");
        System.out.println("Чтобы получить подсказку, введите 'hint'.");

        // Начальное состояние
        displayGameState(session, null);

        while (session.getStatus() == GameStatus.IN_PROGRESS) {
            System.out.print("Введите букву: ");
            String input = scanner.nextLine().trim().toLowerCase();

            if (input.equals("hint")) {
                String hint = gameService.requestHint(session);
                System.out.println(hint);
                continue;
            }

            if (input.length() != 1 || !Character.isLetter(input.charAt(0))) {
                System.out.println("Пожалуйста, введите одну букву русского алфавита.");
                continue;
            }

            GuessResult result = gameService.makeGuess(session, input.charAt(0));
            displayGameState(session, result);
        }
    }

    private void displayGameState(GameSession session, GuessResult result) {
        System.out.println("\n" + "=".repeat(40));

        // Отображение виселицы
        int mistakes = session.getWordEntry().word().length() > 0 ?
                (result != null ? session.getWordEntry().word().length() - result.maskedWord().replace(" ", "").replace("_", "").length() : 0) :
                0; // This calculation is incorrect for hangman, let's fix it.
        // The number of mistakes is the total attempts minus remaining attempts.
        int totalAttempts = 0;
        for (Difficulty d : Difficulty.values()) {
            if (session.getAttemptsLeft() <= d.getAttempts()) {
                totalAttempts = d.getAttempts();
                break;
            }
        }
        int mistakesMade = totalAttempts - session.getAttemptsLeft();
        System.out.println(HANGMAN_PICS[mistakesMade]);


        // Отображение слова
        if (result != null) {
            System.out.println("\nСлово: " + result.maskedWord().toUpperCase());
            System.out.println("\n> " + result.message());
        } else { // Начальное состояние, result еще null
            String initialMaskedWord = "_ ".repeat(session.getWordEntry().word().length()).trim();
            System.out.println("\nСлово: " + initialMaskedWord);
        }

        // Отображение использованных букв
        if (!session.getGuessedLetters().isEmpty()) {
            String guessed = session.getGuessedLetters().stream()
                    .map(String::valueOf)
                    .sorted()
                    .collect(Collectors.joining(", "));
            System.out.println("Использованные буквы: " + guessed);
        }

        System.out.println("=".repeat(40));
    }
}
