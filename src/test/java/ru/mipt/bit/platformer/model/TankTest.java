package ru.mipt.bit.platformer.model;

import com.badlogic.gdx.math.GridPoint2;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static ru.mipt.bit.platformer.model.TestTanks.BULLET_DAMAGE;
import static ru.mipt.bit.platformer.model.TestTanks.HEALTH;
import static ru.mipt.bit.platformer.model.TestTanks.TIME_TO_PASS_A_TILE;
import static ru.mipt.bit.platformer.model.TestTanks.readyGun;
import static ru.mipt.bit.platformer.model.TestTanks.tankAt;

class TankTest {

    private static final GameField FREE_FIELD = coordinates -> true;
    private static final GameField OCCUPIED_FIELD = coordinates -> false;

    private Tank tank;
    private Level level;

    @BeforeEach
    void createTank() {
        tank = tankAt(1, 1);
        level = new Level(10, 8);
    }

    @Test
    void standsStillAndLooksToGivenDirectionWhenCreated() {
        Tank tankLookingDown = tankAt(1, 1, Direction.DOWN);

        assertEquals(new GridPoint2(1, 1), tankLookingDown.getCoordinates());
        assertEquals(Direction.DOWN, tankLookingDown.getDirection());
        assertFalse(tankLookingDown.isMoving());
        assertTrue(tankLookingDown.isAlive());
        assertEquals(HEALTH, tankLookingDown.getHealth());
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
        Tank slowTank = new Tank(new GridPoint2(1, 1), Direction.RIGHT, 2f, HEALTH, readyGun());

        slowTank.move(Direction.UP, FREE_FIELD);
        slowTank.update(1f);

        assertEquals(0.5f, slowTank.getMovementProgress(), 1e-6f);
        assertEquals(new GridPoint2(1, 1), slowTank.getCoordinates());
    }

    @Test
    void cannotBeCreatedWithNonPositiveSpeedOrHealth() {
        assertThrows(IllegalArgumentException.class, () -> new Tank(new GridPoint2(1, 1), Direction.RIGHT, 0f, HEALTH, readyGun()));
        assertThrows(IllegalArgumentException.class, () -> new Tank(new GridPoint2(1, 1), Direction.RIGHT, -1f, HEALTH, readyGun()));
        assertThrows(IllegalArgumentException.class, () -> new Tank(new GridPoint2(1, 1), Direction.RIGHT, 1f, 0, readyGun()));
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

    @Test
    void losesHealthWhenDamaged() {
        tank.takeDamage(BULLET_DAMAGE);

        assertEquals(HEALTH - BULLET_DAMAGE, tank.getHealth());
        assertTrue(tank.isAlive());
    }

    @Test
    void isDestroyedWhenHealthIsOver() {
        tank.takeDamage(HEALTH);

        assertFalse(tank.isAlive());
    }

    @Test
    void destroyedTankNeitherMovesNorShoots() {
        level.add(tank);
        tank.takeDamage(HEALTH);

        tank.move(Direction.UP, level);
        tank.shoot(level);

        assertFalse(tank.isMoving());
        assertFalse(level.objectAt(new GridPoint2(2, 1)).isPresent());
    }

    @Test
    void firesABulletToTheNextTileInItsDirection() {
        level.add(tank);

        tank.shoot(level);

        GameObject bullet = level.objectAt(new GridPoint2(2, 1)).orElse(null);
        assertTrue(bullet instanceof Bullet);
        assertEquals(Direction.RIGHT, ((Bullet) bullet).getDirection());
    }

    @Test
    void firesWhereItLooksAfterTurning() {
        level.add(tank);
        tank.move(Direction.DOWN, OCCUPIED_FIELD);

        tank.shoot(level);

        assertTrue(level.objectAt(new GridPoint2(1, 0)).orElse(null) instanceof Bullet);
    }

    @Test
    void doesNotShootWhileMoving() {
        level.add(tank);
        tank.move(Direction.UP, level);
        tank.update(TIME_TO_PASS_A_TILE / 2);

        tank.shoot(level);

        assertFalse(level.objectAt(new GridPoint2(1, 3)).isPresent());
        assertFalse(level.objectAt(new GridPoint2(2, 1)).isPresent());
    }

    @Test
    void waitsForReloadBetweenShots() {
        Tank slowGunner = new Tank(new GridPoint2(1, 1), Direction.RIGHT, TIME_TO_PASS_A_TILE, HEALTH, new Gun(10f, BULLET_DAMAGE, 1f));
        level.add(slowGunner);

        slowGunner.shoot(level);
        level.remove(level.objectAt(new GridPoint2(2, 1)).orElse(null));
        slowGunner.update(0.5f);
        slowGunner.shoot(level);

        assertFalse(level.objectAt(new GridPoint2(2, 1)).isPresent());

        slowGunner.update(0.5f);
        slowGunner.shoot(level);

        assertTrue(level.objectAt(new GridPoint2(2, 1)).isPresent());
    }
}
