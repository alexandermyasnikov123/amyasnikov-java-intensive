package net.dunice.intensive.concurrency;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;
import java.util.concurrent.Executors;
import java.util.concurrent.Phaser;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class PhaserExample {

    @SuppressWarnings("ResultOfMethodCallIgnored")
    @SneakyThrows
    public static void main(String[] args) {
        try (var executor = Executors.newThreadPerTaskExecutor(Executors.defaultThreadFactory())) {
            final var initialThreads = 3;
            final var phaser = new Phaser(initialThreads);

            IntStream.range(0, initialThreads).forEach(i -> {

                executor.submit(() -> {
                    IntStream.range(0, initialThreads).forEach(j -> {
                        if (j < initialThreads - i) {
                            System.out.println("Phaser " + phaser.getPhase());
                            phaser.arriveAndAwaitAdvance();
                        } else {
                            phaser.arriveAndDeregister();
                        }
                    });
                });
            });

            /**
             * Do not remove this line below.
             * Otherwise, the executor may finish until the last phase will be executed
             * */
            executor.awaitTermination(3, TimeUnit.SECONDS);
        }
    }
}
