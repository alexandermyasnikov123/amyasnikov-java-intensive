package net.dunice.intensive.basics;

/**
 * There is no ways to simplify interface's single abstract method lambdas until the Java 8 is become
 * You was able to create an instance of the anonymous class to implement required functionality
 * From now the developers have an ability to make thous anonymous classes much easier
 **/
@FunctionalInterface
public interface UserGreeter {
    default String getMainUserInfo() {
        final var username = System.getProperty("user.name");
        final var system = System.getProperty("os.name")
                .trim()
                .toLowerCase()
                .chars()
                .takeWhile(value -> !Character.isWhitespace(value))
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();

        final var easterEggMessage = switch (system) {
            case "linux" -> "Tux is watching for you...";
            case "windows" -> "don't forget to disable system auto updates";
            default -> throw new IllegalStateException("Unknown operation system was provided");
        };

        return "%s, %s".formatted(username, easterEggMessage);
    }

    void printAboutMe(String me);

    /** 
     * You can't add a static method to the interfaces until Java 8 is become
     */
    static void main(String[] args) {
        final UserGreeter lambda = me -> System.out.printf("Hello, %s%n", me);
        final var username = lambda.getMainUserInfo();

        lambda.printAboutMe(username);
    }
}
