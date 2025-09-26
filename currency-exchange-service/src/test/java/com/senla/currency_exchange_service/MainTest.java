package com.senla.currency_exchange_service;



import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.*;

class MainTest {

    @Test
    void testConversionAndExit() {
        String simulatedUserInput = String.join(System.lineSeparator(),
                "USD", "RUB", "10", "END") + System.lineSeparator();

        ByteArrayInputStream testIn = new ByteArrayInputStream(simulatedUserInput.getBytes());
        ByteArrayOutputStream testOut = new ByteArrayOutputStream();

        // Подмена стандартных потоков
        System.setIn(testIn);
        System.setOut(new PrintStream(testOut));

        Main.main(new String[]{});

        String output = testOut.toString();

        assertTrue(output.contains("Конвертер валют"));
        assertTrue(output.contains("USD")); // список валют
        assertTrue(output.contains("RUB")); // список валют
        assertTrue(output.contains("Программа завершена."));
    }
}
