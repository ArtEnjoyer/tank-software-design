package ru.mipt.bit.platformer.level;

import com.badlogic.gdx.math.GridPoint2;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class RandomLevelGeneratorTest {

    @Test
    void placesPlayerAndGivenNumberOfTreesOnDifferentCells() {
        LevelLayout layout = new RandomLevelGenerator(5, new Random(1)).generate(4, 4);

        assertEquals(4, layout.getWidth());
        assertEquals(4, layout.getHeight());
        assertEquals(5, layout.getTreeCoordinates().size());
        assertFalse(layout.getTreeCoordinates().contains(layout.getPlayerStart()));

        Set<GridPoint2> occupied = new HashSet<>(layout.getTreeCoordinates());
        occupied.add(layout.getPlayerStart());
        assertEquals(6, occupied.size());
    }

    @Test
    void keepsEveryObjectInsideTheField() {
        LevelLayout layout = new RandomLevelGenerator(7, new Random(2)).generate(3, 5);

        assertTrue(inside(layout.getPlayerStart(), 3, 5));
        for (GridPoint2 tree : layout.getTreeCoordinates()) {
            assertTrue(inside(tree, 3, 5));
        }
    }

    @Test
    void sameSeedProducesTheSameLayout() {
        LevelLayout first = new RandomLevelGenerator(4, new Random(42)).generate(5, 5);
        LevelLayout second = new RandomLevelGenerator(4, new Random(42)).generate(5, 5);

        assertEquals(first.getPlayerStart(), second.getPlayerStart());
        assertEquals(first.getTreeCoordinates(), second.getTreeCoordinates());
    }

    @Test
    void differentSeedsProduceDifferentLayouts() {
        LevelLayout first = new RandomLevelGenerator(4, new Random(1)).generate(5, 5);
        LevelLayout second = new RandomLevelGenerator(4, new Random(2)).generate(5, 5);

        boolean sameLayout = first.getPlayerStart().equals(second.getPlayerStart())
                && first.getTreeCoordinates().equals(second.getTreeCoordinates());
        assertFalse(sameLayout);
    }

    @Test
    void allowsLevelWithoutTrees() {
        LevelLayout layout = new RandomLevelGenerator(0, new Random(7)).generate(2, 2);

        assertTrue(layout.getTreeCoordinates().isEmpty());
        assertTrue(inside(layout.getPlayerStart(), 2, 2));
    }

    @Test
    void rejectsNegativeTreeCount() {
        assertThrows(IllegalArgumentException.class, () -> new RandomLevelGenerator(-1, new Random()));
    }

    @Test
    void rejectsWhenThereIsNoRoomForThePlayer() {
        assertThrows(IllegalArgumentException.class, () -> new RandomLevelGenerator(4, new Random()).generate(2, 2));
    }

    private boolean inside(GridPoint2 coordinates, int width, int height) {
        return coordinates.x >= 0 && coordinates.x < width && coordinates.y >= 0 && coordinates.y < height;
    }
}
