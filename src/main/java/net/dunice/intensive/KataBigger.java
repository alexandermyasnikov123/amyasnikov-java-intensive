package net.dunice.intensive;

import java.util.Arrays;
import java.util.List;

/**
 * Создайте функцию, которая принимает положительное целое число и
 * возвращает следующее большее число, которое можно получить,
 * переставив его цифры.
 * Например:
 * <p>
 * 12 ==> 21
 * 513 ==> 531
 * 2017 ==> 2071
 * 414 ==> 441
 * 10990 ==> 19009 --> 10990 -> 19090 -> 19009
 * 448816607, but was 448816706, <<-->> 448816076 --> 448816(0_76) --> 448816607      != 448816706
 * Если цифры нельзя переставить так,
 * чтобы получилось большее число,
 * верните -1 (или nil в Swift, None в Rust):
 * </p>
 * 9 ==> -1
 * 111 ==> -1
 * 531 ==> -1
 */
public final class KataBigger {
    private static final long NO_NEXT_BIGGER_NUMBER = -1;

    public static void main(String[] args) {
        final var testArguments = List.of(9, 111, 531, 414, 144, 12, 513, 2017, 10990, 448816076);
        testArguments.forEach(number -> {
            System.out.printf("Original = %d, bigger = %d%n", number, nextBiggerNumber(number));
        });
    }

    public static long nextBiggerNumber(long number) {
        final var stringRepresentation = new StringBuilder(Long.toString(number));

        if (stringRepresentation.length() == 1) {
            return NO_NEXT_BIGGER_NUMBER;
        }

        for (int i = stringRepresentation.length() - 2; i >= 0; --i) {
            final var current = stringRepresentation.charAt(i);
            final var previousIndex = findIndexOfMin(stringRepresentation, i + 1, current);
            final var previous = stringRepresentation.charAt(previousIndex);

            if (previous > current) {
                stringRepresentation.setCharAt(i, previous);
                stringRepresentation.setCharAt(previousIndex, current);

                if (i < stringRepresentation.length() - 2) {
                    moveLessNumbers(i + 1, stringRepresentation);
                }

                return Long.parseLong(stringRepresentation.toString());
            }
        }

        return NO_NEXT_BIGGER_NUMBER;
    }

    private static int findIndexOfMin(StringBuilder stringRepresentation, int offset, long greaterThan) {
        long min = stringRepresentation.charAt(offset);
        int index = offset;
        for (int i = offset + 1; i < stringRepresentation.length(); ++i) {
            final var current = stringRepresentation.charAt(i);
            if (current < min && current > greaterThan) {
                index = i;
                min = current;
            }
        }
        return index;
    }

    private static void moveLessNumbers(int currentIndex, StringBuilder stringRepresentation) {
        final var letters = new char[stringRepresentation.length() - currentIndex];
        stringRepresentation.getChars(currentIndex, stringRepresentation.length(), letters, 0);

        Arrays.sort(letters);

        stringRepresentation.replace(currentIndex, stringRepresentation.length(), new String(letters));
    }
}
