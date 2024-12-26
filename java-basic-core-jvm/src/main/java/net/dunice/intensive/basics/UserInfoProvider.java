package net.dunice.intensive.basics;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class UserInfoProvider {

    public static String getUsername() {
        return System.getProperty("user.name");
    }

    public static String getUsersSystem() {
        return System.getProperty("os.name")
                .trim()
                .toLowerCase()
                .chars()
                .takeWhile(value -> !Character.isWhitespace(value))
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();
    }
}
