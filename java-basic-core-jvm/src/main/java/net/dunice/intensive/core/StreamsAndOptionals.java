package net.dunice.intensive.core;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.OptionalInt;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class StreamsAndOptionals {
    private static List<String> generateIds(int amount) {
        return IntStream
                .range(0, amount)
                .mapToObj(ignored -> "ID " + ignored)
                .toList();
    }

    private static List<Integer> generateRandomNumbers(int origin, int bound, int amount) {
        final var random = new Random();
        return IntStream
                .generate(() -> random.nextInt(origin, bound))
                .limit(amount)
                .boxed()
                .toList();
    }

    private static void showStreamExamples() {
        final var random = new Random();
        final var ids = generateIds(random.nextInt(Byte.MAX_VALUE / 2, Byte.MAX_VALUE + 1));

        System.out.println("------Sequential-----");
        final var sequential = ids.stream()
                .peek(System.out::println)
                .collect(Collectors.toList());

        System.out.println("------Parallel-----");
        System.out.println("Parallel stream does not guarantee order of the execution");
        final var parallel = ids.stream()
                .parallel()
                .peek(System.out::println)
                .toList();

        sequential.add("will be added");
        try {
            parallel.add("must throw an exception");
        } catch (UnsupportedOperationException e) {
            System.out.println("Direct call to toList() returns unmodifiableList");
        }

        for (int i = 0; i < Math.min(sequential.size(), parallel.size()); ++i) {
            final var sequentialElement = sequential.get(i);
            final var parallelElement = parallel.get(i);

            if (!sequentialElement.equals(parallelElement)) {
                throw new IllegalStateException("Parallel stream must returns values in original order!");
            }

            System.out.printf("Sequential is - %s, parallel is - %s%n", sequentialElement, parallelElement);
        }
    }

    private static void showOptionalExamples() {
        final var random = new Random();
        final int maxBound = Byte.MAX_VALUE;
        final Optional<String> message = generateRandomNumbers(0, maxBound, maxBound)
                .stream()
                .peek(System.out::println)
                .filter(num -> num % 2 != 0)
                .sorted(Comparator.reverseOrder())
                .skip(random.nextInt(1, maxBound / 2))
                .findFirst()
                .map(i -> "Message " + i);

        message.ifPresentOrElse(
                msg -> System.out.println("Found: " + msg),
                () -> System.out.println("Nothing found")
        );
    }

    public static void main(String[] args) {
        showOptionalExamples();
        showStreamExamples();
    }
}
