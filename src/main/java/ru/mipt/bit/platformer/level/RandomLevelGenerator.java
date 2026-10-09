package ru.mipt.bit.platformer.level;

import com.badlogic.gdx.math.GridPoint2;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class RandomLevelGenerator implements LevelGenerator {

    private final int treeCount;
    private final Random random;

    public RandomLevelGenerator(int treeCount, Random random) {
        if (treeCount < 0) {
            throw new IllegalArgumentException("Tree count must not be negative, got " + treeCount);
        }
        this.treeCount = treeCount;
        this.random = random;
    }

    @Override
    public LevelLayout generate(int fieldWidth, int fieldHeight) {
        List<GridPoint2> cells = allCells(fieldWidth, fieldHeight);
        if (treeCount >= cells.size()) {
            throw new IllegalArgumentException(
                    "Field " + fieldWidth + "x" + fieldHeight + " has no room for " + treeCount + " trees and a player");
        }
        Collections.shuffle(cells, random);
        return new LevelLayout(fieldWidth, fieldHeight, cells.get(0), cells.subList(1, treeCount + 1));
    }

    private List<GridPoint2> allCells(int width, int height) {
        List<GridPoint2> cells = new ArrayList<>();
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                cells.add(new GridPoint2(x, y));
            }
        }
        return cells;
    }
}
