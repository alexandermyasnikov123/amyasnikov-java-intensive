package net.dunice.intensive.core;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ThreadSafeSets {
    private static final int DEFAULT_AMOUNT = 32;

    private static final String AMOUNT_ARG_PREFIX = "-am=";

    private static final String NAME_PREFIX_FORMAT = "name_%d";

    public static void main(String[] args) {
        final var counter = new AtomicInteger();
        final Set<String> strings = generateThreadNames(args);

        final List<Thread> threads = strings.stream().map(name -> Thread.ofPlatform()
                .name(name)
                .unstarted(() -> {
                    while (true) {
                        if (strings.isEmpty()) {
                            return;
                        }

                        final int current = counter.incrementAndGet();
                        strings.remove(name);
                        System.out.printf("Работает поток - %s, счётчик = %d%n", name, current);
                    }
                })).toList();

        startAllAndJoin(threads);

        System.out.printf("Количество итераций потоков: %d", counter.get());
    }

    private static Set<String> generateThreadNames(String[] args) {
        return Arrays.stream(args)
                .filter(arg -> arg.startsWith(AMOUNT_ARG_PREFIX))
                .map(arg -> Integer.parseInt(arg.substring(AMOUNT_ARG_PREFIX.length())))
                .findFirst()
                .or(() -> Optional.of(DEFAULT_AMOUNT))
                .map(amount -> {
                    final var original = IntStream
                            .range(0, amount)
                            .mapToObj(NAME_PREFIX_FORMAT::formatted)
                            .collect(Collectors.toSet());

                    return Collections.synchronizedSet(original);
                })
                .orElseThrow();
    }

    private static void startAllAndJoin(Collection<Thread> threads) {
        threads.forEach(Thread::start);
        threads.forEach(thread -> {
            try {
                thread.join();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });
    }
}
