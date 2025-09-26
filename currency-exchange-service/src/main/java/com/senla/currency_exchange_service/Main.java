package com.senla.currency_exchange_service;



import com.senla.currency_exchange_service.service.CurrencyConverter;
import com.senla.currency_exchange_service.service.ExchangeRateProvider;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        ExchangeRateProvider provider = new ExchangeRateProvider("rates.json");
        CurrencyConverter converter = new CurrencyConverter(provider);

        try (Scanner scanner = new Scanner(System.in)) {
            System.out.println("=== Конвертер валют ===");
            System.out.println("Доступные валюты: " + String.join(", ", provider.getAllCodes()));
            System.out.println("Введите END в любой момент, чтобы выйти.");

            while (true) {
                System.out.print("\nИз какой валюты: ");
                String from = scanner.nextLine().trim();
                if (isEnd(from)) break;

                System.out.print("В какую валюту: ");
                String to = scanner.nextLine().trim();
                if (isEnd(to)) break;

                System.out.print("Сумма: ");
                String amountInput = scanner.nextLine().trim();
                if (isEnd(amountInput)) break;

                try {
                    double amount = Double.parseDouble(amountInput);
                    double result = converter.convert(from, to, amount);
                    System.out.printf("%.2f %s = %.2f %s%n",
                            amount, from.toUpperCase(), result, to.toUpperCase());
                } catch (NumberFormatException e) {
                    System.err.println("Ошибка: сумма должна быть числом.");
                } catch (Exception e) {
                    System.err.println("Ошибка: " + e.getMessage());
                }
            }
        }

        System.out.println("Программа завершена.");
    }

    private static boolean isEnd(String input) {
        return "END".equalsIgnoreCase(input);
    }
}