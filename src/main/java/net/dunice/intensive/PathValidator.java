package net.dunice.intensive;

import java.util.Arrays;
import java.util.List;

public class PathValidator {

    public static void main(String[] args) {
        final var paths = List.of(
                new boolean[][]{
                        {true, true, true},
                        {true, true, true},
                        {true, false, false},
                        {true, true, true},
                        {true, true, true}
                },
                new boolean[][]{
                        {true, false, true, true},
                        {false, false, true, true},
                        {true, true, false, false},
                        {false, true, true, true},
                        {false, true, false, true}
                },
                new boolean[][]{
                        {true, true, true, true},
                        {true, true, true, true},
                        {false, false, false, false}
                },
                new boolean[][]{
                        {true},
                        {true},
                        {false}
                },
                new boolean[][]{
                        {true},
                        {true},
                        {true}
                },
                new boolean[][]{
                        {true, true},
                        {true, true},
                        {true, true}
                }
        );

        final var playersPositions = List.of(
                new int[]{0, 1},
                new int[]{0, 3},
                new int[]{0, 1},
                new int[]{0, 0},
                new int[]{0, 0},
                new int[]{0, 1}
        );

        for (int i = 0; i < paths.size(); i++) {
            final var path = paths.get(i);
            final var playersPosition = playersPositions.get(i);

            final var reachableFields = findAvailablePaths(
                    path, path.length, path[0].length, playersPosition[0], playersPosition[1], 0
            );
            System.out.printf("There are %d paths in field %n%s%n", reachableFields, Arrays.deepToString(path));
        }
    }

    public static int findAvailablePaths(
            final boolean[][] grid,
            final int rows,
            final int columns,
            int startRow,
            int startColumn,
            int compensation
    ) {
        if (startRow >= rows || startColumn < 0 || startColumn >= columns || !grid[startRow][startColumn]) {
            return 0;
        }

        if (startRow == rows - 1 && grid[startRow][startColumn]) {
            return 1;
        }

        final var nextColumn = startColumn + 1;
        final var previousColumn = startColumn - 1;
        final var nextRow = startRow + 1;

        return findAvailablePaths(grid, rows, columns, nextRow, startColumn, 0) +
               (compensation != -1 ? findAvailablePaths(grid, rows, columns, startRow, nextColumn, 1) : 0) +
               (compensation != 1 ? findAvailablePaths(grid, rows, columns, startRow, previousColumn, -1) : 0);
    }
}
