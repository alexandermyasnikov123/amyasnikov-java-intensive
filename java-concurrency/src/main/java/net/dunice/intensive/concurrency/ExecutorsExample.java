package net.dunice.intensive.concurrency;

import lombok.NoArgsConstructor;
import java.util.Collections;
import java.util.Date;
import java.util.Hashtable;
import java.util.Vector;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

@NoArgsConstructor
public final class ExecutorsExample {

    private static void tryWithResourcesShutdownsExecutorCorrectly() {
        try (var executor = Executors.newFixedThreadPool(4)) {
            final var task = executor.submit(() -> {
                while (true) {
                    try {
                        TimeUnit.SECONDS.sleep(1);
                        System.out.println(new Date());
                    } catch (InterruptedException e) {
                        System.out.println("Timer is cancelled");
                        return;
                    }
                }
            });

            executor.execute(() -> {
                try {
                    task.get(5, TimeUnit.SECONDS);
                } catch (InterruptedException | ExecutionException | TimeoutException e) {
                    task.cancel(true);
                }
            });
        }
    }

    public static void main(String[] args) {
        tryWithResourcesShutdownsExecutorCorrectly();

    }
}
