package net.dunice.intensive.concurrency;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;
import java.util.ArrayList;
import java.util.Collections;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class CountDownLatchExample {

    @SneakyThrows
    public static void main(String[] args) {
        final var latch = new CountDownLatch(3);

        final var threadNames = Collections.synchronizedList(new ArrayList<>());

        IntStream.range(0, 3).forEach(i -> new Thread(() -> {
            try {
                TimeUnit.SECONDS.sleep(1);
                threadNames.add(Thread.currentThread().getName());
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            } finally {
                latch.countDown();
            }
        }).start());

        latch.await();
        System.out.printf("Code completed, threads: %s%n", threadNames);
    }
}
