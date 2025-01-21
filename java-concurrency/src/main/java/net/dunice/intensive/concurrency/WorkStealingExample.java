package net.dunice.intensive.concurrency;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;
import java.util.Date;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class WorkStealingExample {

    @SneakyThrows
    public static void main(String[] args) {
        try (var executor = Executors.newWorkStealingPool(10)) {
            final var futures = executor.invokeAll(
                    IntStream.range(0, 20)
                            .<Callable<Date>>mapToObj(i -> () -> {
                                TimeUnit.SECONDS.sleep(3);
                                return new Date();
                            })
                            .toList()
            );

            System.out.println(futures.stream().map(future -> {
                try {
                    return future.get();
                } catch (InterruptedException | ExecutionException e) {
                    throw new RuntimeException(e);
                }
            }).toList());
        }
    }
}
