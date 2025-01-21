package net.dunice.intensive.concurrency;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import java.util.Date;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class SemaphoreExample {
    private int sharedCounter;

    public static void main(String[] args) {
        final var example = new SemaphoreExample();
        final var semaphore = new Semaphore(5);

        IntStream.range(0, 100).forEach(i -> Thread.ofPlatform().start(() -> {
            try {
                semaphore.acquire();
                TimeUnit.SECONDS.sleep(1);
                System.out.printf("Counter is - %d, time - %s%n", ++example.sharedCounter, new Date());
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            } finally {
                semaphore.release();
            }
        }));
    }
}
