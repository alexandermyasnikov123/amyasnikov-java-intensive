package net.dunice.intensive.basics;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ScannerUtils {

    public static List<Integer> inputIntegersList(Scanner scanner) {
        System.out.println("Input a sequence of positive integers or a negative value to abort: ");

        final List<Integer> integers = new ArrayList<>();
        while (scanner.hasNextInt()) {
            final var value = scanner.nextInt();
            if (value < 0) {
                break;
            }
            integers.add(value);
        }

        return integers;
    }
}
