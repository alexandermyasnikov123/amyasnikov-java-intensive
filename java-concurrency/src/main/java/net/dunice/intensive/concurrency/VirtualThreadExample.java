package net.dunice.intensive.concurrency;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import java.util.concurrent.locks.StampedLock;
import java.util.stream.IntStream;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class VirtualThreadExample {
    private int sharedCounter;

    public static void main(String[] args) {
        final var lock = new StampedLock();

        final var example = new VirtualThreadExample();

        IntStream.range(0, 100_000).forEach(i -> Thread.ofVirtual().start(() -> {
            if (i % 2 == 0) {
                long stamp = lock.readLock();
                try {
                    System.out.println(example.sharedCounter);
                } finally {
                    lock.unlockRead(stamp);
                }
            } else {
                long stamp = lock.writeLock();
                try {
                    ++example.sharedCounter;
                } finally {
                    lock.unlockWrite(stamp);
                }
            }
        }));
    }
}
