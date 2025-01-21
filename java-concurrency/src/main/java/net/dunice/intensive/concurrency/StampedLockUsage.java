package net.dunice.intensive.concurrency;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.StampedLock;
import java.util.stream.IntStream;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class StampedLockUsage {
    private final StampedLock stampedLock = new StampedLock();

    private int count;

    private void increment() {
        long stamp = 0;
        try {
            stamp = stampedLock.writeLock();
            ++count;
        } finally {
            stampedLock.unlockWrite(stamp);
        }
    }

    private void print() {
        long stamp = 0;
        try {
            stamp = stampedLock.readLock();
            System.out.println(count);
        } finally {
            stampedLock.unlockRead(stamp);
        }
    }

    public static void main(String[] args) {
        final var usage = new StampedLockUsage();

        IntStream.range(0, 100).forEach(i -> {
            new Thread(() -> {
                if (i % 2 != 0) {
                    usage.increment();
                } else {
                    usage.print();
                }
            }).start();
        });
    }
}
