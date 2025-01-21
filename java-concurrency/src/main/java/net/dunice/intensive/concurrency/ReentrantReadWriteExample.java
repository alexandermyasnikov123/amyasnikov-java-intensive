package net.dunice.intensive.concurrency;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.stream.IntStream;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ReentrantReadWriteExample {
    private final ReadWriteLock lock = new ReentrantReadWriteLock();

    private int value;

    private int getValue() {
        try {
            lock.readLock().lock();
            return value;
        } finally {
            lock.readLock().unlock();
        }
    }

    private void increment() {
        try {
            lock.writeLock().lock();
            ++value;
        } finally {
            lock.writeLock().unlock();
        }
    }

    public static void main(String[] args) {
        final var example = new ReentrantReadWriteExample();

        final var threads = IntStream.range(0, 100).mapToObj(i -> new Thread(() -> {
            if (i % 2 == 0) {
                System.out.printf("Thread - %s, value = %d%n", Thread.currentThread().getName(), example.getValue());
            } else {
                example.increment();
            }
        })).peek(Thread::start).toList();

        threads.forEach(thread -> {
            try {
                thread.join();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });

        if (example.getValue() != 50) {
            throw new RuntimeException("Threads did not increment properly, actual is " + example.getValue());
        }
    }
}
