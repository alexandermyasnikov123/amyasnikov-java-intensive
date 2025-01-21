package net.dunice.intensive.concurrency;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import java.util.Date;
import java.util.concurrent.Exchanger;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ExchangerExample {

    public static void main(String[] args) {
        try (var executor = Executors.newScheduledThreadPool(10)) {
            final var schedulingInterval = 2;
            final var schedulingTimeout = 8;

            final var exchanger = new Exchanger<String>();

            IntStream.generate(() -> 0).limit(2).forEach(ignored -> executor.scheduleAtFixedRate(() -> {
                try {
                    final var threadName = Thread.currentThread().getName();
                    final var otherThreadName = exchanger.exchange(threadName);

                    System.out.printf(
                            "Time: %s, current thread: %s, other thread: %s%n",
                            new Date(),
                            threadName,
                            otherThreadName
                    );
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }, schedulingInterval, schedulingInterval, TimeUnit.SECONDS));

            if (executor.awaitTermination(schedulingTimeout, TimeUnit.SECONDS)) {
                System.out.println("Exchanger thread pool terminated");
            }
        } catch (InterruptedException e) {
            System.err.println("Exchanger thread pool interrupted");
        }
    }
}
