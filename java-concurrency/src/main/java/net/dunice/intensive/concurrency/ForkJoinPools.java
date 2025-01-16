package net.dunice.intensive.concurrency;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import java.util.Collection;
import java.util.concurrent.ForkJoinPool;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ForkJoinPools {

    public static void main(String[] args) {

        try (var forkJoinPool = ForkJoinPool.commonPool()) {

            final Collection<Integer> values = forkJoinPool.invoke(new ParallelStreamRecursiveTask(10_001));
            System.out.printf("Values length is %d, values: %s%n", values.size(), values);
        }
    }
}
