package ru.mipt.bit.platformer.model;

import com.badlogic.gdx.math.GridPoint2;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

class DirectionTest {

    private final GridPoint2 origin = new GridPoint2(3, 4);

    @Test
    void upLeadsToTheTileAbove() {
        assertEquals(new GridPoint2(3, 5), Direction.UP.neighbourOf(origin));
    }

    @Test
    void downLeadsToTheTileBelow() {
        assertEquals(new GridPoint2(3, 3), Direction.DOWN.neighbourOf(origin));
    }

    @Test
    void leftLeadsToTheTileOnTheLeft() {
        assertEquals(new GridPoint2(2, 4), Direction.LEFT.neighbourOf(origin));
    }

    @Test
    void rightLeadsToTheTileOnTheRight() {
        assertEquals(new GridPoint2(4, 4), Direction.RIGHT.neighbourOf(origin));
    }

    @Test
    void neighbourIsCalculatedWithoutChangingGivenCoordinates() {
        Direction.UP.neighbourOf(origin);

        assertEquals(new GridPoint2(3, 4), origin);
    }

    @Test
    void vectorOfDirectionCannotBeSpoiledFromOutside() {
        GridPoint2 vector = Direction.UP.getVector();
        vector.set(100, 100);

        assertEquals(new GridPoint2(0, 1), Direction.UP.getVector());
    }

    @Test
    void oppositeDirectionsHaveOppositeVectors() {
        assertEquals(new GridPoint2(0, 0), Direction.UP.getVector().add(Direction.DOWN.getVector()));
        assertEquals(new GridPoint2(0, 0), Direction.LEFT.getVector().add(Direction.RIGHT.getVector()));
    }

    @Test
    void everyDirectionHasItsOwnRotation() {
        assertEquals(0f, Direction.RIGHT.getRotation());
        assertEquals(90f, Direction.UP.getRotation());
        assertEquals(-90f, Direction.DOWN.getRotation());
        assertNotEquals(Direction.RIGHT.getRotation(), Direction.LEFT.getRotation());
    }
}
