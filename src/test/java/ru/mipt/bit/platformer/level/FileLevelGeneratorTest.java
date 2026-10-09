package ru.mipt.bit.platformer.level;

import com.badlogic.gdx.math.GridPoint2;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class FileLevelGeneratorTest {

    @Test
    void readsTheAssignmentExampleFromResources() {
        LevelLayout layout = new FileLevelGenerator("level.txt").generate(10, 8);

        assertEquals(10, layout.getWidth());
        assertEquals(6, layout.getHeight());
        assertEquals(new GridPoint2(5, 1), layout.getPlayerStart());
        assertEquals(15, layout.getTreeCoordinates().size());
        assertTrue(layout.getTreeCoordinates().contains(new GridPoint2(3, 5)));
        assertTrue(layout.getTreeCoordinates().contains(new GridPoint2(9, 2)));
    }

    @Test
    void rejectsALevelThatDoesNotFitTheField() {
        assertThrows(IllegalArgumentException.class, () -> new FileLevelGenerator("level.txt").generate(5, 8));
        assertThrows(IllegalArgumentException.class, () -> new FileLevelGenerator("level.txt").generate(10, 5));
    }

    @Test
    void rejectsMissingFile() {
        assertThrows(IllegalArgumentException.class, () -> new FileLevelGenerator("missing-level.txt").generate(10, 8));
    }
}
