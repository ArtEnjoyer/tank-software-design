package ru.mipt.bit.platformer.level;

import com.badlogic.gdx.math.GridPoint2;

import java.util.ArrayList;
import java.util.List;

public class LevelParser {

    private static final char TREE = 'T';
    private static final char PLAYER = 'X';
    private static final char EMPTY = '_';

    public LevelLayout parse(List<String> lines) {
        List<String> rows = nonBlankRows(lines);
        if (rows.isEmpty()) {
            throw new IllegalArgumentException("Level has no rows");
        }
        int width = rows.get(0).length();
        int height = rows.size();
        List<GridPoint2> playerStarts = new ArrayList<>();
        List<GridPoint2> trees = new ArrayList<>();

        for (int row = 0; row < height; row++) {
            String line = rows.get(row);
            if (line.length() != width) {
                throw new IllegalArgumentException("Row " + (row + 1) + " has width " + line.length() + ", expected " + width);
            }
            int y = height - 1 - row;
            for (int x = 0; x < width; x++) {
                parseCell(line.charAt(x), new GridPoint2(x, y), playerStarts, trees);
            }
        }

        if (playerStarts.size() != 1) {
            throw new IllegalArgumentException("Level must have exactly one player start '" + PLAYER + "', found " + playerStarts.size());
        }
        return new LevelLayout(width, height, playerStarts.get(0), trees);
    }

    private void parseCell(char symbol, GridPoint2 coordinates, List<GridPoint2> playerStarts, List<GridPoint2> trees) {
        if (symbol == TREE) {
            trees.add(coordinates);
        } else if (symbol == PLAYER) {
            playerStarts.add(coordinates);
        } else if (symbol != EMPTY) {
            throw new IllegalArgumentException("Unknown level symbol '" + symbol + "' at " + coordinates);
        }
    }

    private List<String> nonBlankRows(List<String> lines) {
        List<String> rows = new ArrayList<>();
        for (String line : lines) {
            String row = line.replaceAll("\\s+$", "");
            if (!row.isEmpty()) {
                rows.add(row);
            }
        }
        return rows;
    }
}
