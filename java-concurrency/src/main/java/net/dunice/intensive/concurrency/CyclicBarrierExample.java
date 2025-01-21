package net.dunice.intensive.concurrency;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import java.util.Arrays;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.stream.IntStream;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class CyclicBarrierExample {

    public static void main(String[] args) {

        final var threads = 10;
        final var amount = 100_000;
        final var hugeArray = new int[amount];

        final var cyclicBarrier = new CyclicBarrier(threads, () -> System.out.println(Arrays.toString(hugeArray)));
        IntStream.range(0, threads).forEach(i -> Thread.ofPlatform().start(() -> {
            try {
                final var count = amount / threads;
                final var start = i * count;

                IntStream.range(start, start + count).forEach(j -> hugeArray[j] = j);

                cyclicBarrier.await();
            } catch (InterruptedException | BrokenBarrierException e) {
                throw new RuntimeException(e);
            }
        }));
    }
}
