package ru.mipt.bit.platformer.model;

import com.badlogic.gdx.math.GridPoint2;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

class MoveCommandTest {

    private static final float TIME_TO_PASS_A_TILE = 0.4f;
    private static final GameField FREE_FIELD = coordinates -> true;
    private static final GameField OCCUPIED_FIELD = coordinates -> false;

    private final Tank tank = new Tank(new GridPoint2(1, 1), Direction.RIGHT, TIME_TO_PASS_A_TILE);

    @Test
    void sendsObjectToTheGivenDirection() {
        new MoveCommand(tank, Direction.LEFT, FREE_FIELD).execute();
        tank.update(TIME_TO_PASS_A_TILE);

        assertEquals(new GridPoint2(0, 1), tank.getCoordinates());
    }

    @Test
    void turnsObjectEvenWhenTargetTileIsOccupied() {
        new MoveCommand(tank, Direction.DOWN, OCCUPIED_FIELD).execute();

        assertEquals(Direction.DOWN, tank.getDirection());
        assertFalse(tank.isMoving());
    }

    @Test
    void doesNothingUntilExecuted() {
        new MoveCommand(tank, Direction.LEFT, FREE_FIELD);

        assertEquals(new GridPoint2(1, 1), tank.getCoordinates());
        assertEquals(Direction.RIGHT, tank.getDirection());
    }
}
