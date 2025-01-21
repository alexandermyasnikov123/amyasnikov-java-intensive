package net.dunice.intensive.concurrency;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;
import java.util.Date;
import java.util.concurrent.TimeUnit;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ThreadInterruptionExample {

    @SneakyThrows
    public static void main(String[] args) {
        final var timerThread = Thread.ofPlatform()
                .start(() -> {
                    while (!Thread.interrupted()) {
                        try {
                            TimeUnit.SECONDS.sleep(1);
                            System.out.println(new Date());
                        } catch (InterruptedException e) {
                            return;
                        }
                    }
                });

        TimeUnit.SECONDS.sleep(5);
        timerThread.interrupt();
    }
}
