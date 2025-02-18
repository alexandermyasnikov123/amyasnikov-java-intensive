package net.dunice.intensive;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public class Kata {
    private static final int START_LETTER_CODE = 'a' - 1;

    public static void main(String[] args) {
        final var testArguments = List.of("abad", "bronks babe", "abad adab", "abad addb", "b aa");
        testArguments.forEach(string -> System.out.printf("Initial = %s, high = %s%n", string, high(string)));
    }

    /**
     * Получив строку слов, вам нужно найти слово, набравшее наибольшее количество баллов.
     * Каждая буква в слове набирает очки в зависимости от своего положения в алфавите:
     * a = 1, b = 2, c = 3 и т. д.
     * Например, сумма abad равна 8 (1 + 2 + 1 + 4).
     * Вам нужно вернуть слово, набравшее наибольшее количество баллов, в виде строки.
     * Если два слова набрали одинаковое количество баллов, верните слово, которое
     * встречается в исходной строке раньше. Все буквы будут строчными, и все входные
     * данные будут действительными.
     */
    public static String high(String input) {
        return Arrays.stream(input.split("\\s"))
                .max(Comparator.comparing(Kata::countScores))
                .orElse(input);
    }

    private static int countScores(String word) {
        return word
                .chars()
                .map(letter -> letter - START_LETTER_CODE)
                .sum();
    }
}
