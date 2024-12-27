package net.dunice.intensive.basics;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Scanner;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class FindMax {

    public static void main(String[] args) {
        try (var scanner = new Scanner(System.in)) {
            final List<Integer> integers = ScannerUtils.inputIntegersList(scanner);

            int maxValue = integers.getFirst();
            for (int i = 1; i < integers.size(); ++i) {
                final var current = integers.get(i);

                if (current > maxValue) {
                    maxValue = current;
                }
            }

            System.out.println("The maximum value of the given sequence is " + maxValue);
        } catch (NoSuchElementException e) {
            System.out.println("A sequence must consists of at least one value");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
