package ru.mipt.bit.platformer.model;

import com.badlogic.gdx.math.GridPoint2;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class TankTest {

    private static final float TIME_TO_PASS_A_TILE = 0.4f;
    private static final GameField FREE_FIELD = coordinates -> true;
    private static final GameField OCCUPIED_FIELD = coordinates -> false;

    private Tank tank;

    @BeforeEach
    void createTank() {
        tank = new Tank(new GridPoint2(1, 1), Direction.RIGHT, TIME_TO_PASS_A_TILE);
    }

    @Test
    void standsStillAndLooksToGivenDirectionWhenCreated() {
        Tank tankLookingDown = new Tank(new GridPoint2(1, 1), Direction.DOWN, TIME_TO_PASS_A_TILE);

        assertEquals(new GridPoint2(1, 1), tankLookingDown.getCoordinates());
        assertEquals(Direction.DOWN, tankLookingDown.getDirection());
        assertFalse(tankLookingDown.isMoving());
    }

    @Test
    void reachesNeighbourTileWhenItIsFree() {
        tank.move(Direction.UP, FREE_FIELD);
        tank.update(TIME_TO_PASS_A_TILE);

        assertEquals(new GridPoint2(1, 2), tank.getCoordinates());
        assertFalse(tank.isMoving());
    }

    @Test
    void staysOnItsTileUntilMovementIsFinished() {
        tank.move(Direction.UP, FREE_FIELD);
        tank.update(TIME_TO_PASS_A_TILE / 2);

        assertEquals(new GridPoint2(1, 1), tank.getCoordinates());
        assertEquals(new GridPoint2(1, 2), tank.getDestinationCoordinates());
        assertEquals(0.5f, tank.getMovementProgress(), 1e-6f);
        assertTrue(tank.isMoving());
    }

    @Test
    void movesWithTheSpeedGivenAtCreation() {
        Tank slowTank = new Tank(new GridPoint2(1, 1), Direction.RIGHT, 2f);

        slowTank.move(Direction.UP, FREE_FIELD);
        slowTank.update(1f);

        assertEquals(0.5f, slowTank.getMovementProgress(), 1e-6f);
        assertEquals(new GridPoint2(1, 1), slowTank.getCoordinates());
    }

    @Test
    void cannotBeCreatedWithNonPositiveSpeed() {
        assertThrows(IllegalArgumentException.class, () -> new Tank(new GridPoint2(1, 1), Direction.RIGHT, 0f));
        assertThrows(IllegalArgumentException.class, () -> new Tank(new GridPoint2(1, 1), Direction.RIGHT, -1f));
    }

    @Test
    void turnsButKeepsItsPlaceWhenNeighbourTileIsOccupied() {
        tank.move(Direction.UP, OCCUPIED_FIELD);

        assertEquals(Direction.UP, tank.getDirection());
        assertEquals(new GridPoint2(1, 1), tank.getCoordinates());
        assertEquals(new GridPoint2(1, 1), tank.getDestinationCoordinates());
        assertFalse(tank.isMoving());
    }

    @Test
    void ignoresNewOrdersWhileMoving() {
        tank.move(Direction.UP, FREE_FIELD);
        tank.update(TIME_TO_PASS_A_TILE / 4);

        tank.move(Direction.RIGHT, FREE_FIELD);

        assertEquals(Direction.UP, tank.getDirection());
        assertEquals(new GridPoint2(1, 2), tank.getDestinationCoordinates());
    }

    @Test
    void acceptsNewOrderWhenPreviousMovementIsFinished() {
        tank.move(Direction.UP, FREE_FIELD);
        tank.update(TIME_TO_PASS_A_TILE);

        tank.move(Direction.RIGHT, FREE_FIELD);
        tank.update(TIME_TO_PASS_A_TILE);

        assertEquals(new GridPoint2(2, 2), tank.getCoordinates());
    }

    @Test
    void movementProgressNeverGrowsAboveOne() {
        tank.move(Direction.UP, FREE_FIELD);
        tank.update(TIME_TO_PASS_A_TILE * 10);

        assertEquals(1f, tank.getMovementProgress(), 1e-6f);
    }

    @Test
    void occupiesBothTilesWhileTravellingBetweenThem() {
        tank.move(Direction.UP, FREE_FIELD);
        tank.update(TIME_TO_PASS_A_TILE / 2);

        assertTrue(tank.occupies(new GridPoint2(1, 1)));
        assertTrue(tank.occupies(new GridPoint2(1, 2)));
        assertFalse(tank.occupies(new GridPoint2(1, 3)));
    }

    @Test
    void doesNotLetOutsideCodeSpoilItsCoordinates() {
        GridPoint2 coordinates = tank.getCoordinates();
        coordinates.set(7, 7);

        assertEquals(new GridPoint2(1, 1), tank.getCoordinates());
    }
}
