package net.dunice.intensive.concurrency;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import java.io.PrintStream;
import java.util.Scanner;
import java.util.concurrent.Exchanger;
import java.util.regex.Pattern;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class DaemonTask {
    private static final Pattern WAKE_UP_PATTERN = Pattern.compile("^подъ[её]м$", Pattern.CASE_INSENSITIVE + Pattern.UNICODE_CASE);

    public static void main(String[] args) {
        final var exchanger = new Exchanger<PrintStream>();
        final var scanner = new Scanner(System.in);

        Thread.ofPlatform()
                .daemon()
                .start(() -> {
                    while (true) {
                        try {
                            exchanger.exchange(System.out).println("ОТБОЙ");
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                    }
                });

        Thread.ofPlatform()
                .start(() -> {
                    while (true) {
                        try {
                            if (WAKE_UP_PATTERN.matcher(scanner.next()).matches()) {
                                exchanger.exchange(System.out);
                            }
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                    }
                });
    }
}
