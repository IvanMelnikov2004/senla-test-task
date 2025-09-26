package com.senla.hangman;

import com.senla.hangman.model.GameConfig;
import com.senla.hangman.repository.WordRepository;
import com.senla.hangman.service.GameService;
import com.senla.hangman.ui.ConsoleManager;

public class Main {
    public static void main(String[] args) {
        // 1. Создаем конфигурацию
        GameConfig config = new GameConfig();

        // 2. Создаем репозиторий, передавая ему путь из конфига
        WordRepository wordRepository = new WordRepository(config.getWordsFileName());


        // 3. Создаем сервис, передавая ему репозиторий
        GameService gameService = new GameService(wordRepository);

        // 4. Создаем UI-менеджер, передавая ему сервис
        ConsoleManager consoleManager = new ConsoleManager(gameService);

        // 5. Запускаем главный цикл приложения
        consoleManager.run();
    }
}