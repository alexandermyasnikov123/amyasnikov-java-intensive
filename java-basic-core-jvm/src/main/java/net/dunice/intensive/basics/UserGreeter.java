package net.dunice.intensive.basics;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * There is no ways to simplify interface's single abstract method lambdas until the Java 8 is become
 * You was able to create an instance of the anonymous class to implement required functionality
 * From now the developers have an ability to make thous anonymous classes much easier
 **/
@FunctionalInterface
public interface UserGreeter {
    default String getMainUserInfo() {
        final var username = UserInfoProvider.getUsername();
        final Object system = UserInfoProvider.getUsersSystem();

        final var easterEggMessage = switch (system) {
            case String str when "Linux".equalsIgnoreCase(str) -> "Tux is watching for you...";
            case String str when "Windows".equalsIgnoreCase(str) -> "don't forget to disable system auto updates";
            case null -> throw new NullPointerException("Can't determine your operation system... What is it?");
            default -> throw new IllegalStateException("Unknown operation system was provided");
        };

        return "%s, %s".formatted(username, easterEggMessage);
    }

    void printAboutMe(String me);

    /**
     * You can't add a static method to the interfaces until Java 8 is become
     */
    static void main(String[] args) {
        final String dateTime = new SimpleDateFormat("dd-MMMM-yyyy HH:mm").format(new Date());
        final UserGreeter lambda = me -> System.out.printf("Hello, %s - %s%n", me, dateTime);
        final var username = lambda.getMainUserInfo();

        lambda.printAboutMe(username);
    }
}
