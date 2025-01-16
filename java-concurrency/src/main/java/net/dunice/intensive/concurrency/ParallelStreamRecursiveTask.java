package net.dunice.intensive.concurrency;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Random;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.RecursiveTask;
import java.util.stream.IntStream;

@RequiredArgsConstructor
public class ParallelStreamRecursiveTask extends RecursiveTask<Collection<Integer>> {
    public static final int LOAD_FACTOR_BORDER = 10_000;

    private static final Random random = new Random();

    private final int loadFactor;

    @SneakyThrows
    @Override
    protected Collection<Integer> compute() {
        if (loadFactor > LOAD_FACTOR_BORDER) {
            final int mainLoadFactor = loadFactor / 2;

            final var firstTask = new ParallelStreamRecursiveTask(mainLoadFactor);
            final var secondTask = new ParallelStreamRecursiveTask(loadFactor - mainLoadFactor);

            var a = firstTask.fork();
            var b = secondTask.fork();

            return CompletableFuture.supplyAsync(() -> {
                final Collection<Integer> fL = a.join();
                final Collection<Integer> sL = b.join();
                var result = new ArrayList<Integer>();
                result.addAll(fL);
                result.addAll(sL);
                return result;
            }).get();
        } else {
            return IntStream
                    .range(0, loadFactor)
                    .mapToObj(ignored -> random.nextInt(0, 1112))
                    .toList();
        }
    }
}
