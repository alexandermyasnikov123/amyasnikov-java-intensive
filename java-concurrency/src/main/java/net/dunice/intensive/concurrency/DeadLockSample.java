package net.dunice.intensive.concurrency;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class DeadLockSample {

    public static void main(String[] args) {
        final var firstSharedResource = new Object();
        final var secondSharedResource = new Object();

        Thread.ofPlatform().start(() -> {
            synchronized (firstSharedResource) {
                System.out.println("First shared resource's monitor is acquired");
                synchronized (secondSharedResource) {
                    System.out.println("#1 - will never be called");
                }
            }
            System.out.println("First shared resource's monitor is released");
        });

        Thread.ofPlatform().start(() -> {
            synchronized (secondSharedResource) {
                System.out.println("Second shared resource's monitor is acquired");
                synchronized (firstSharedResource) {
                    System.out.println("#2 - will never be called");
                }
            }
            System.out.println("Second shared resource's monitor is acquired");
        });
    }
}
