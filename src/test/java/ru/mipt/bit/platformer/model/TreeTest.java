package ru.mipt.bit.platformer.model;

import com.badlogic.gdx.math.GridPoint2;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class TreeTest {

    private final Tree tree = new Tree(new GridPoint2(2, 3));

    @Test
    void occupiesOnlyItsOwnTile() {
        assertTrue(tree.occupies(new GridPoint2(2, 3)));
        assertFalse(tree.occupies(new GridPoint2(3, 3)));
    }

    @Test
    void neverMoves() {
        tree.update(100f);

        assertEquals(new GridPoint2(2, 3), tree.getCoordinates());
    }

    @Test
    void doesNotLetOutsideCodeSpoilItsCoordinates() {
        GridPoint2 coordinates = tree.getCoordinates();
        coordinates.set(7, 7);

        assertEquals(new GridPoint2(2, 3), tree.getCoordinates());
    }
}
