package net.dunice.intensive.concurrency;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import java.util.Random;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ThreadLocalExample {

    public static void main(String[] args) {
        try (var executor = Executors.newCachedThreadPool()) {
            final var uniqueCounter = ThreadLocal.withInitial(() -> 0);
            final var random = new Random();

            IntStream.range(0, 100)
                    .mapToObj(i -> executor.submit(() -> {
                        TimeUnit.SECONDS.sleep(random.nextInt(1, 4));

                        final var before = uniqueCounter.get();
                        uniqueCounter.set(100);
                        return "Before: %d, after: %d".formatted(before, uniqueCounter.get());
                    })).toList().forEach(future -> {
                        try {
                            System.out.println(future.get());
                        } catch (InterruptedException | ExecutionException e) {
                            throw new RuntimeException(e);
                        }
                    });
        }
    }
}
