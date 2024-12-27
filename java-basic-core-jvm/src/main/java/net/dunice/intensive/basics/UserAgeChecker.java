package net.dunice.intensive.basics;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import java.util.Scanner;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class UserAgeChecker {

    public static void main(String[] args) {
        try (var scanner = new Scanner(System.in)) {
            final var username = System.getProperty("user.name");
            System.out.printf("Hello, %s. Input your age: ", username);

            final Integer age = scanner.nextInt();
            switch (age) {
                case Integer a when a < 0 -> throw new NegativeNumberException("A negative value was provided");
                case Integer a when a < 18 -> throw new InvalidAgeException("Can't register non adult person");
                default -> System.out.printf("You are welcome, %s%n", username);
            }
        } catch (InvalidAgeException | NegativeNumberException e) {
            System.out.println(e.getMessage());
        }
    }
}
