package com.senla.hangman.service;

import com.senla.hangman.model.*;
import com.senla.hangman.repository.WordRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GameServiceTest {

    @Mock
    private WordRepository wordRepository;

    private GameService gameService;
    private List<WordEntry> testWords;

    @BeforeEach
    void setUp() {
        gameService = new GameService(wordRepository);
        testWords = Arrays.asList(
                new WordEntry("тест", "проверка"),
                new WordEntry("программа", "код")
        );
    }

    @Test
    void startNewGame_shouldCreateGameSession_whenWordsAvailable() {
        // Given
        when(wordRepository.loadWords()).thenReturn(testWords);

        // When
        GameSession session = gameService.startNewGame(Difficulty.NORMAL);

        // Then
        assertThat(session).isNotNull();
        assertThat(session.getWordEntry()).isIn(testWords);
        assertThat(session.getAttemptsLeft()).isEqualTo(7);
        assertThat(session.getStatus()).isEqualTo(GameStatus.IN_PROGRESS);
    }

    @Test
    void startNewGame_shouldThrowException_whenNoWordsAvailable() {
        // Given
        when(wordRepository.loadWords()).thenReturn(List.of());

        // When & Then
        assertThatThrownBy(() -> gameService.startNewGame(Difficulty.NORMAL))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("Словарь пуст! Невозможно начать игру.");
    }

    @Test
    void makeGuess_shouldReturnCorrect_whenLetterExists() {
        // Given
        when(wordRepository.loadWords()).thenReturn(testWords);
        GameSession session = gameService.startNewGame(Difficulty.NORMAL);
        // Насильно устанавливаем слово для предсказуемости теста
        session = new GameSession(new WordEntry("тест", "hint"), Difficulty.NORMAL);

        // When
        GuessResult result = gameService.makeGuess(session, 'т');

        // Then
        assertThat(result.status()).isEqualTo(GuessStatus.CORRECT);
        assertThat(result.message()).contains("Отлично! Такая буква есть");
        assertThat(result.maskedWord()).contains("т");
    }



    @Test
    void makeGuess_shouldReturnGameWon_whenWordFullyGuessed() {
        // Given
        GameSession session = new GameSession(new WordEntry("кот", "hint"), Difficulty.NORMAL);

        // When - угадываем все буквы
        gameService.makeGuess(session, 'к');
        gameService.makeGuess(session, 'о');
        GuessResult result = gameService.makeGuess(session, 'т');

        // Then
        assertThat(result.status()).isEqualTo(GuessStatus.GAME_WON);
        assertThat(result.message()).contains("Поздравляем, вы победили!");
        assertThat(session.getStatus()).isEqualTo(GameStatus.WON);
    }

    @Test
    void makeGuess_shouldReturnGameLost_whenNoAttemptsLeft() {
        // Given
        GameSession session = new GameSession(new WordEntry("тест", "hint"), Difficulty.HARD); // 5 попыток
        String word = session.getWordEntry().word();

        // When - делаем неверные попытки до исчерпания
        char wrongLetter = 'а';
        for (int i = 0; i < 5; i++) {
            wrongLetter = (char) ('а' + i);
            if (word.indexOf(wrongLetter) == -1) {
                gameService.makeGuess(session, wrongLetter);
            }
        }
        GuessResult result = gameService.makeGuess(session, 'х'); // Последняя неверная попытка

        // Then
        assertThat(result.status()).isEqualTo(GuessStatus.GAME_LOST);
        assertThat(result.message()).contains("Игра уже окончена.");
        assertThat(session.getStatus()).isEqualTo(GameStatus.LOST);
    }

    @Test
    void makeGuess_shouldReturnAlreadyGuessed_whenLetterRepeated() {
        // Given
        GameSession session = new GameSession(new WordEntry("тест", "hint"), Difficulty.NORMAL);
        gameService.makeGuess(session, 'т');

        // When
        GuessResult result = gameService.makeGuess(session, 'т');

        // Then
        assertThat(result.status()).isEqualTo(GuessStatus.ALREADY_GUESSED);
        assertThat(result.message()).contains("Вы уже называли эту букву");
    }

    @Test
    void requestHint_shouldReturnHint_whenFirstTime() {
        // Given
        GameSession session = new GameSession(new WordEntry("тест", "это подсказка"), Difficulty.NORMAL);

        // When
        String hint = gameService.requestHint(session);

        // Then
        assertThat(hint).isEqualTo("Подсказка: это подсказка");
        assertThat(session.isHintUsed()).isTrue();
    }

    @Test
    void requestHint_shouldReturnAlreadyUsed_whenHintAlreadyRequested() {
        // Given
        GameSession session = new GameSession(new WordEntry("тест", "hint"), Difficulty.NORMAL);
        gameService.requestHint(session);

        // When
        String hint = gameService.requestHint(session);

        // Then
        assertThat(hint).isEqualTo("Подсказка уже была использована.");
    }
}