package net.dunice.intensive.concurrency;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.stream.IntStream;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ThreadLocalExample {

    public static void main(String[] args) {
        try (var executor = Executors.newCachedThreadPool()) {
            final var uniqueCounter = ThreadLocal.withInitial(() -> 0);

            IntStream.range(0, 100)
                    .mapToObj(i -> executor.submit(() -> {
                        uniqueCounter.set(100);
                        return uniqueCounter.get();
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
