package ru.mipt.bit.platformer.model;

import com.badlogic.gdx.math.GridPoint2;
import org.junit.jupiter.api.Test;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class LevelTest {

    private static final float TIME_TO_PASS_A_TILE = 0.4f;

    @Test
    void tileWithoutObjectsIsFree() {
        Level level = new Level(emptyList(), singletonList(new Tree(new GridPoint2(1, 3))));

        assertTrue(level.isFree(new GridPoint2(5, 5)));
    }

    @Test
    void tileWithTreeIsNotFree() {
        Level level = new Level(emptyList(), singletonList(new Tree(new GridPoint2(1, 3))));

        assertFalse(level.isFree(new GridPoint2(1, 3)));
    }

    @Test
    void tileWithTankIsNotFree() {
        Level level = new Level(singletonList(new Tank(new GridPoint2(1, 1))), emptyList());

        assertFalse(level.isFree(new GridPoint2(1, 1)));
    }

    @Test
    void tankDoesNotDriveThroughTree() {
        Tank tank = new Tank(new GridPoint2(1, 1));
        Level level = new Level(singletonList(tank), singletonList(new Tree(new GridPoint2(1, 2))));

        tank.move(Direction.UP, level);
        level.update(TIME_TO_PASS_A_TILE);

        assertEquals(new GridPoint2(1, 1), tank.getCoordinates());
        assertEquals(Direction.UP, tank.getDirection());
    }

    @Test
    void tankDoesNotDriveThroughAnotherTank() {
        Tank player = new Tank(new GridPoint2(1, 1));
        Tank enemy = new Tank(new GridPoint2(2, 1));
        Level level = new Level(asList(player, enemy), emptyList());

        player.move(Direction.RIGHT, level);
        level.update(TIME_TO_PASS_A_TILE);

        assertEquals(new GridPoint2(1, 1), player.getCoordinates());
    }

    @Test
    void updateMovesEveryTankOfTheLevel() {
        Tank first = new Tank(new GridPoint2(1, 1));
        Tank second = new Tank(new GridPoint2(5, 5));
        Level level = new Level(asList(first, second), emptyList());

        first.move(Direction.UP, level);
        second.move(Direction.LEFT, level);
        level.update(TIME_TO_PASS_A_TILE);

        assertEquals(new GridPoint2(1, 2), first.getCoordinates());
        assertEquals(new GridPoint2(4, 5), second.getCoordinates());
    }
}
