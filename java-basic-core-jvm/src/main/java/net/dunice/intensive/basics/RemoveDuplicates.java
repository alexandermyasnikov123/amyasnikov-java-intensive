package net.dunice.intensive.basics;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class RemoveDuplicates {

    public static void main(String[] args) {
        try (var scanner = new Scanner(System.in)) {
            final List<Integer> integers = ScannerUtils.inputIntegersList(scanner);

            System.out.println("""
                    Choose an algorithm to duplicates removing:
                    1 - Stream API-based implementation
                    2 - Self written O(N^2) algo
                    """);

            final DuplicatesRemover<Integer> duplicatesRemover = switch (scanner.nextInt()) {
                case 1 -> new StreamApiRemover<>();
                case 2 -> new SelfWrittenRemover<>();
                default -> throw new IllegalArgumentException("Input must be in range [1, 2]");
            };

            final var sortedList = duplicatesRemover.removeDuplicates(integers);
            System.out.printf("Initial list was: %n%s%n Sorted list is: %n%s%n", integers, sortedList);
        } catch (Exception e) {
            System.out.println("Invalid input was provided");
        }
    }

    private abstract static sealed class DuplicatesRemover<T extends Number> {
        public abstract List<T> removeDuplicates(List<? extends T> values);
    }

    private static final class StreamApiRemover<T extends Number> extends DuplicatesRemover<T> {

        @Override
        public List<T> removeDuplicates(List<? extends T> values) {
            return List.copyOf(values.stream().distinct().toList());
        }
    }

    private static final class SelfWrittenRemover<T extends Number> extends DuplicatesRemover<T> {

        @Override
        public List<T> removeDuplicates(List<? extends T> values) {
            final List<T> resultingList = new ArrayList<>(values);

            for (int i = 0; i < resultingList.size(); ++i) {
                final var current = resultingList.get(i);

                for (int j = i + 1; j < resultingList.size(); ++j) {
                    final var next = resultingList.get(j);

                    if (current != null && current.equals(next)) {
                        resultingList.set(j, null);
                    }
                }
            }

            return resultingList.stream().filter(Objects::nonNull).toList();
        }
    }
}
