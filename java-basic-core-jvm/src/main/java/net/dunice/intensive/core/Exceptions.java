package net.dunice.intensive.core;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;
import java.io.IOException;
import java.net.SocketException;
import java.util.stream.IntStream;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class Exceptions {

    private static void throwCheckedException() throws IOException {
        throw new SocketException("I'm checked at compile time");
    }

    @SneakyThrows
    private static void sneakyThrowCheckedException() {
        throw new SocketException("The compiler thinks that I'm already checked by user :)");
    }

    private static void throwRuntimeException() {
        throw new IllegalStateException("Why did you call me?");
    }

    @SuppressWarnings("InfiniteRecursion")
    private static void hurtMyTinyStack() {
        hurtMyTinyStack();
    }

    public static void main(String[] args) {
        IntStream.range(1, 5).forEach(i -> {
            try {
                switch (i) {
                    case 1 -> {
                        try {
                            throwCheckedException();
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }
                    case 2 -> sneakyThrowCheckedException();
                    case 3 -> throwRuntimeException();
                    case 4 -> hurtMyTinyStack();
                    default -> System.out.println("Unknown idx was provided");
                }
            } catch (Throwable t) {
                System.out.printf("Exception: %s, message: %s%n", t.getClass().getSimpleName(), t.getMessage());
            }
        });
    }
}
