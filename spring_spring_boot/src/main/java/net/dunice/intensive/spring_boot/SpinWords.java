package net.dunice.intensive.spring_boot;

import lombok.NonNull;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Напишите функцию, которая принимает строку,
 * состоящую из одного или нескольких слов, и возвращает ту же строку,
 * но со всеми словами, состоящими из пяти или более букв, в обратном порядке (как в названии этой задачи).
 * Передаваемые строки будут состоять только из букв и пробелов.
 * Пробелы будут присутствовать только в том случае, если слов больше одного.
 * <p>
 * Примеры:
 * "Hey fellow warriors"  --> "Hey wollef sroirraw"
 * "This is a test        --> "This is a test"
 * "This is another test" --> "This is rehtona test"
 */
public class SpinWords {
    private static final int EXPECTED_LENGTH = 5;

    private static final String WHITESPACE_DELIMITER = " ";

    public static void main(String[] args) {
        final var examples = List.of("Hey fellow warriors", "This is a test", "This is another test", "test", "stonks");
        examples.forEach(value -> System.out.printf("Initial string is '%s', modified is '%s'%n", value, spinWords(value)));
    }

    public static String spinWords(@NonNull String input) {
        return Arrays.stream(input.split("\\s"))
                .map(value -> value.length() >= EXPECTED_LENGTH ? new StringBuilder(input).reverse() : value)
                .collect(Collectors.joining(WHITESPACE_DELIMITER));
    }
}
