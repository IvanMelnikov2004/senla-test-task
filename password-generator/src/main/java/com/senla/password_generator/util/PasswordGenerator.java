package com.senla.password_generator.util;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public final class PasswordGenerator {

    private static final String LOWERCASE_LETTERS = "abcdefghijklmnopqrstuvwxyz";
    private static final String UPPERCASE_LETTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final String DIGITS = "0123456789";
    private static final String SPECIAL_CHARACTERS = "!@#$%^&*()_+-=[]{}|;':,./<>?";

    // Объединяем все наборы символов в один для удобства
    private static final String ALL_CHARACTERS = LOWERCASE_LETTERS + UPPERCASE_LETTERS + DIGITS + SPECIAL_CHARACTERS;

    // Используем SecureRandom для криптографически стойкой генерации
    private static final SecureRandom RANDOM = new SecureRandom();

    // Минимальная и максимальная длина пароля
    public static final int MIN_LENGTH = 8;
    public static final int MAX_LENGTH = 12;



    private PasswordGenerator() {
        throw new UnsupportedOperationException("Этот класс не может быть инстанциирован.");
    }


    public static String generateSecurePassword(int length) {
        if (length < MIN_LENGTH || length > MAX_LENGTH) {
            throw new IllegalArgumentException(String.format("Длина пароля должна быть от %d до %d символов.", MIN_LENGTH, MAX_LENGTH));
        }

        List<Character> passwordChars = new ArrayList<>(length);

        // 1. Гарантируем наличие как минимум одного символа из каждой категории
        passwordChars.add(getRandomChar(LOWERCASE_LETTERS));
        passwordChars.add(getRandomChar(UPPERCASE_LETTERS));
        passwordChars.add(getRandomChar(DIGITS));
        passwordChars.add(getRandomChar(SPECIAL_CHARACTERS));

        // 2. Заполняем оставшуюся часть пароля случайными символами из всех наборов
        for (int i = 4; i < length; i++) {
            passwordChars.add(getRandomChar(ALL_CHARACTERS));
        }

        // 3. Перемешиваем символы
        Collections.shuffle(passwordChars, RANDOM);

        // 4. Собираем итоговую строку с помощью StringBuilder
        StringBuilder passwordBuilder = new StringBuilder(length);
        for (Character ch : passwordChars) {
            passwordBuilder.append(ch);
        }

        return passwordBuilder.toString();
    }


    private static char getRandomChar(String source) {
        int randomIndex = RANDOM.nextInt(source.length());
        return source.charAt(randomIndex);
    }
}