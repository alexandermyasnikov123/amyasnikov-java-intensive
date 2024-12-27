package net.dunice.intensive.basics;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import java.util.stream.IntStream;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class StringConcatenation {

    public static void main(String[] args) {
        final var builder = new StringBuilder();
        final var separator = ", ";

        final int startInclusive = 1;
        final int endExclusive = 100;

        IntStream.range(startInclusive, endExclusive).forEach(value -> builder.append(value).append(separator));
        builder.append(endExclusive);

        System.out.println(builder);
    }
}
