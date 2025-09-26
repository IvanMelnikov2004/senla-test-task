package com.senla.password_generator;

import com.senla.password_generator.util.PasswordGenerator;

import java.util.InputMismatchException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        try (Scanner scanner = new Scanner(System.in)) {
            System.out.printf("Введите желаемую длину пароля (от %d до %d): ", PasswordGenerator.MIN_LENGTH, PasswordGenerator.MAX_LENGTH);

            try {
                int length = scanner.nextInt();
                String password = PasswordGenerator.generateSecurePassword(length);
                System.out.println("Ваш сгенерированный пароль: " + password);
            } catch (InputMismatchException e) {
                System.err.println("Ошибка: необходимо ввести целое число.");
            } catch (IllegalArgumentException e) {
                System.err.println("Ошибка: " + e.getMessage());
            }
        }
    }
}