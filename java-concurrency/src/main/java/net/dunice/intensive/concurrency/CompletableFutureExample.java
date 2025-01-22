package net.dunice.intensive.concurrency;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import java.util.Random;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class CompletableFutureExample {

    public static void main(String[] args) {
        try (var executor = Executors.newVirtualThreadPerTaskExecutor()) {

            final var origin = 0;
            final var bound = 10;
            final var timeOut = 5;

            IntStream.range(origin, bound).mapToObj(ignored -> CompletableFuture.supplyAsync(() -> {
                try {
                    final var random = new Random();
                    final var value = random.nextInt(origin, bound);

                    TimeUnit.SECONDS.sleep(value);

                    if (value % 2 == 0) {
                        throw new UnsupportedOperationException("Can't operate on even numbers");
                    }
                    return value;
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }, executor).completeOnTimeout(-2, timeOut, TimeUnit.SECONDS)
                            .exceptionallyAsync(throwable -> -1)
                            .thenAccept(System.out::println)
            ).toList().forEach(CompletableFuture::join);

        }
    }
}
