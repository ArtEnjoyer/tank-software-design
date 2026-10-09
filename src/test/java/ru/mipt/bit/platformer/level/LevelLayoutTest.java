package ru.mipt.bit.platformer.level;

import com.badlogic.gdx.math.GridPoint2;
import org.junit.jupiter.api.Test;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class LevelLayoutTest {

    @Test
    void keepsPlayerStartAndTrees() {
        LevelLayout layout = new LevelLayout(10, 8, new GridPoint2(5, 1), singletonList(new GridPoint2(3, 7)));

        assertEquals(10, layout.getWidth());
        assertEquals(8, layout.getHeight());
        assertEquals(new GridPoint2(5, 1), layout.getPlayerStart());
        assertEquals(singletonList(new GridPoint2(3, 7)), layout.getTreeCoordinates());
    }

    @Test
    void copiesCoordinatesSoOutsideCodeCannotSpoilThem() {
        GridPoint2 start = new GridPoint2(1, 1);
        GridPoint2 tree = new GridPoint2(2, 2);
        LevelLayout layout = new LevelLayout(4, 4, start, singletonList(tree));

        start.set(0, 0);
        tree.set(0, 0);
        layout.getPlayerStart().set(3, 3);
        layout.getTreeCoordinates().get(0).set(3, 3);
        layout.getTreeCoordinates().add(new GridPoint2(1, 2));

        assertEquals(new GridPoint2(1, 1), layout.getPlayerStart());
        assertEquals(singletonList(new GridPoint2(2, 2)), layout.getTreeCoordinates());
    }

    @Test
    void rejectsNonPositiveSize() {
        assertThrows(IllegalArgumentException.class, () -> new LevelLayout(0, 8, new GridPoint2(0, 0), emptyList()));
        assertThrows(IllegalArgumentException.class, () -> new LevelLayout(10, -1, new GridPoint2(0, 0), emptyList()));
    }

    @Test
    void rejectsCoordinatesOutsideTheField() {
        assertThrows(IllegalArgumentException.class, () -> new LevelLayout(2, 2, new GridPoint2(2, 0), emptyList()));
        assertThrows(IllegalArgumentException.class, () -> new LevelLayout(2, 2, new GridPoint2(0, 0), singletonList(new GridPoint2(0, 2))));
    }

    @Test
    void rejectsTreeOnPlayerStart() {
        assertThrows(IllegalArgumentException.class,
                () -> new LevelLayout(3, 3, new GridPoint2(1, 1), asList(new GridPoint2(0, 0), new GridPoint2(1, 1))));
    }
}
