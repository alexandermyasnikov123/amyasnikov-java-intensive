package net.dunice.intensive.basics;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import java.util.Arrays;
import java.util.Comparator;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class StreamApi {

    /**
     * Pass a list of the strings via command line arguments directly
     */
    public static void main(String[] args) {
        final var resultingStrings = Arrays.stream(args)
                .filter(it -> it.length() >= 5)
                .map(String::toUpperCase)
                .sorted(Comparator.reverseOrder())
                .distinct()
                .toList();

        System.out.printf("""
                        You provide next strings as arguments:
                        %s
                        Here are your resulting values:
                        %s
                        """,
                Arrays.toString(args),
                resultingStrings
        );
    }
}
