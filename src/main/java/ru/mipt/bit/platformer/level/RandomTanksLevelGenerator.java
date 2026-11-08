package ru.mipt.bit.platformer.level;

import com.badlogic.gdx.math.GridPoint2;

import java.util.Collections;
import java.util.List;
import java.util.Random;

public class RandomTanksLevelGenerator implements LevelGenerator {

    private final LevelGenerator levelGenerator;
    private final int tankCount;
    private final Random random;

    public RandomTanksLevelGenerator(LevelGenerator levelGenerator, int tankCount, Random random) {
        if (tankCount < 0) {
            throw new IllegalArgumentException("Tank count must not be negative, got " + tankCount);
        }
        this.levelGenerator = levelGenerator;
        this.tankCount = tankCount;
        this.random = random;
    }

    @Override
    public LevelLayout generate(int fieldWidth, int fieldHeight) {
        LevelLayout layout = levelGenerator.generate(fieldWidth, fieldHeight);
        List<GridPoint2> freeCells = layout.getFreeCells();
        if (tankCount > freeCells.size()) {
            throw new IllegalArgumentException("Level has " + freeCells.size() + " free cells, not enough for " + tankCount + " tanks");
        }
        Collections.shuffle(freeCells, random);
        return layout.withEnemyTanks(freeCells.subList(0, tankCount));
    }
}
