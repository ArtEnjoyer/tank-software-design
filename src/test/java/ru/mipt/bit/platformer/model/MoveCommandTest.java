package ru.mipt.bit.platformer.model;

import com.badlogic.gdx.math.GridPoint2;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static ru.mipt.bit.platformer.model.TestTanks.TIME_TO_PASS_A_TILE;
import static ru.mipt.bit.platformer.model.TestTanks.tankAt;

class MoveCommandTest {

    private static final GameField FREE_FIELD = coordinates -> true;
    private static final GameField OCCUPIED_FIELD = coordinates -> false;

    private final Tank tank = tankAt(1, 1);

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
