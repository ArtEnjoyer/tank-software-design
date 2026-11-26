package ru.mipt.bit.platformer.model;

import com.badlogic.gdx.math.GridPoint2;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static ru.mipt.bit.platformer.model.TestTanks.HEALTH;
import static ru.mipt.bit.platformer.model.TestTanks.tankAt;

class GunTest {

    private static final GridPoint2 SHOOTER = new GridPoint2(1, 1);
    private static final GridPoint2 IN_FRONT = new GridPoint2(2, 1);

    private final Level level = new Level(10, 8);

    @Test
    void isLoadedFromTheStart() {
        Gun gun = new Gun(0.1f, 10, 1f);

        assertTrue(gun.isReady());
    }

    @Test
    void putsBulletOnTheNextTileInTheGivenDirection() {
        new Gun(0.1f, 10, 1f).fire(SHOOTER, Direction.RIGHT, level);

        assertTrue(level.objectAt(IN_FRONT).orElse(null) instanceof Bullet);
    }

    @Test
    void needsReloadAfterEveryShot() {
        Gun gun = new Gun(0.1f, 10, 1f);

        gun.fire(SHOOTER, Direction.RIGHT, level);
        gun.update(0.9f);

        assertFalse(gun.isReady());

        gun.update(0.1f);

        assertTrue(gun.isReady());
    }

    @Test
    void doesNotFireWhileReloading() {
        Gun gun = new Gun(0.1f, 10, 1f);
        gun.fire(SHOOTER, Direction.RIGHT, level);

        gun.fire(SHOOTER, Direction.UP, level);

        assertFalse(level.objectAt(new GridPoint2(1, 2)).isPresent());
    }

    @Test
    void withoutReloadTimeIsAlwaysReady() {
        Gun gun = new Gun(0.1f, 10, 0f);

        gun.fire(SHOOTER, Direction.RIGHT, level);
        gun.update(0f, 0.5f);
        gun.fire(SHOOTER, Direction.UP, level);

        assertTrue(gun.isReady());
        assertTrue(level.objectAt(new GridPoint2(1, 2)).orElse(null) instanceof Bullet);
    }

    @Test
    void reloadMultiplierCanMakeTheGunSlower() {
        Gun gun = new Gun(0.1f, 10, 1f);
        gun.fire(SHOOTER, Direction.RIGHT, level);

        gun.update(1f, 2f);
        gun.fire(SHOOTER, Direction.UP, level);

        assertFalse(gun.isReady());
        assertFalse(level.objectAt(new GridPoint2(1, 2)).isPresent());

        gun.update(1f, 2f);
        gun.fire(SHOOTER, Direction.UP, level);

        assertTrue(level.objectAt(new GridPoint2(1, 2)).orElse(null) instanceof Bullet);
    }

    @Test
    void idleTimeDoesNotAllowSeveralShotsWithoutReloading() {
        Gun gun = new Gun(0.1f, 10, 1f);
        gun.update(10f, 0.5f);

        gun.fire(SHOOTER, Direction.RIGHT, level);
        gun.fire(SHOOTER, Direction.UP, level);

        assertFalse(gun.isReady());
        assertFalse(level.objectAt(new GridPoint2(1, 2)).isPresent());
    }

    @Test
    void rejectsInvalidReloadMultipliersEvenWithoutReloadTime() {
        for (float reloadSeconds : new float[]{0f, 1f}) {
            Gun gun = new Gun(0.1f, 10, reloadSeconds);
            for (float multiplier : new float[]{0f, -1f, Float.NaN,
                    Float.NEGATIVE_INFINITY, Float.POSITIVE_INFINITY}) {
                assertThrows(IllegalArgumentException.class, () -> gun.update(0.1f, multiplier));
            }
        }
    }

    @Test
    void damagesNeighbourWithItsOwnDamage() {
        Tank neighbour = tankAt(2, 1);
        level.add(neighbour);

        new Gun(0.1f, 30, 0f).fire(SHOOTER, Direction.RIGHT, level);

        assertEquals(HEALTH - 30, neighbour.getHealth());
    }

    @Test
    void rejectsInvalidSettings() {
        assertThrows(IllegalArgumentException.class, () -> new Gun(0f, 10, 1f));
        assertThrows(IllegalArgumentException.class, () -> new Gun(0.1f, 0, 1f));
        assertThrows(IllegalArgumentException.class, () -> new Gun(0.1f, 10, -1f));
    }
}
