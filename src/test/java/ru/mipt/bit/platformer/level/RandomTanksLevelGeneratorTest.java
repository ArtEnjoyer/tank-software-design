package ru.mipt.bit.platformer.level;

import com.badlogic.gdx.math.GridPoint2;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class RandomTanksLevelGeneratorTest {

    private static final LevelGenerator FIXED_LEVEL = (width, height) ->
            new LevelLayout(width, height, new GridPoint2(0, 0), asList(new GridPoint2(1, 0), new GridPoint2(2, 0)));

    @Test
    void addsGivenNumberOfTanksToTheLevelOfAnotherGenerator() {
        LevelLayout layout = new RandomTanksLevelGenerator(FIXED_LEVEL, 3, new Random(1)).generate(4, 4);

        assertEquals(new GridPoint2(0, 0), layout.getPlayerStart());
        assertEquals(asList(new GridPoint2(1, 0), new GridPoint2(2, 0)), layout.getTreeCoordinates());
        assertEquals(3, layout.getEnemyTankCoordinates().size());
    }

    @Test
    void putsTanksOnlyOnFreeDistinctCells() {
        LevelLayout layout = new RandomTanksLevelGenerator(FIXED_LEVEL, 5, new Random(2)).generate(3, 3);

        List<GridPoint2> tanks = layout.getEnemyTankCoordinates();
        Set<GridPoint2> distinctTanks = new HashSet<>(tanks);
        assertEquals(5, distinctTanks.size());
        assertFalse(tanks.contains(layout.getPlayerStart()));
        for (GridPoint2 tree : layout.getTreeCoordinates()) {
            assertFalse(tanks.contains(tree));
        }
        for (GridPoint2 tank : tanks) {
            assertTrue(tank.x >= 0 && tank.x < 3 && tank.y >= 0 && tank.y < 3);
        }
    }

    @Test
    void canFillEveryFreeCell() {
        LevelLayout layout = new RandomTanksLevelGenerator(FIXED_LEVEL, 6, new Random(3)).generate(3, 3);

        assertTrue(layout.getFreeCells().isEmpty());
    }

    @Test
    void worksWithRandomObstaclesToo() {
        LevelGenerator generator = new RandomTanksLevelGenerator(new RandomLevelGenerator(4, new Random(4)), 2, new Random(5));

        LevelLayout layout = generator.generate(5, 5);

        assertEquals(4, layout.getTreeCoordinates().size());
        assertEquals(2, layout.getEnemyTankCoordinates().size());
    }

    @Test
    void sameSeedProducesTheSameTanks() {
        LevelLayout first = new RandomTanksLevelGenerator(FIXED_LEVEL, 3, new Random(42)).generate(5, 5);
        LevelLayout second = new RandomTanksLevelGenerator(FIXED_LEVEL, 3, new Random(42)).generate(5, 5);

        assertEquals(first.getEnemyTankCoordinates(), second.getEnemyTankCoordinates());
    }

    @Test
    void zeroTanksKeepsLevelAsIs() {
        LevelLayout layout = new RandomTanksLevelGenerator(FIXED_LEVEL, 0, new Random()).generate(3, 3);

        assertEquals(emptyList(), layout.getEnemyTankCoordinates());
    }

    @Test
    void rejectsNegativeTankCount() {
        assertThrows(IllegalArgumentException.class, () -> new RandomTanksLevelGenerator(FIXED_LEVEL, -1, new Random()));
    }

    @Test
    void rejectsMoreTanksThanFreeCells() {
        assertThrows(IllegalArgumentException.class, () -> new RandomTanksLevelGenerator(FIXED_LEVEL, 7, new Random()).generate(3, 3));
    }
}
