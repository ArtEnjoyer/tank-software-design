package ru.mipt.bit.platformer.model;

import com.badlogic.gdx.math.GridPoint2;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static ru.mipt.bit.platformer.model.TestTanks.HEALTH;
import static ru.mipt.bit.platformer.model.TestTanks.TIME_TO_PASS_A_TILE;
import static ru.mipt.bit.platformer.model.TestTanks.readyGun;
import static ru.mipt.bit.platformer.model.TestTanks.tankAt;

class BulletTest {

    private static final float BULLET_TIME = 0.1f;
    private static final int DAMAGE = 25;

    private final List<GameObject> removed = new ArrayList<>();
    private Level level;

    @BeforeEach
    void createLevel() {
        level = new Level(10, 8);
        level.subscribe(new LevelListener() {
            @Override
            public void onObjectAdded(GameObject object) {
            }

            @Override
            public void onObjectRemoved(GameObject object) {
                removed.add(object);
            }
        });
    }

    private Bullet shoot(int x, int y, Direction direction) {
        Bullet.shoot(new GridPoint2(x, y), direction, BULLET_TIME, DAMAGE, level);
        return (Bullet) level.objectAt(new GridPoint2(x, y)).orElse(null);
    }

    private void tick(int times) {
        for (int i = 0; i < times; i++) {
            level.update(BULLET_TIME);
        }
    }

    @Test
    void appearsOnTheGivenTileLookingInItsDirection() {
        Bullet bullet = shoot(2, 1, Direction.UP);

        assertEquals(new GridPoint2(2, 1), bullet.getCoordinates());
        assertEquals(Direction.UP, bullet.getDirection());
        assertTrue(bullet.isAlive());
    }

    @Test
    void fliesTileByTileInItsDirection() {
        Bullet bullet = shoot(2, 1, Direction.RIGHT);

        tick(3);

        assertEquals(new GridPoint2(5, 1), bullet.getCoordinates());
    }

    @Test
    void occupiesBothTilesWhileFlyingBetweenThem() {
        Bullet bullet = shoot(2, 1, Direction.RIGHT);

        level.update(BULLET_TIME / 2);

        assertTrue(bullet.occupies(new GridPoint2(2, 1)));
        assertTrue(bullet.occupies(new GridPoint2(3, 1)));
        assertFalse(level.isFree(new GridPoint2(3, 1)));
    }

    @Test
    void removesItselfFromTheLevelAtTheBorder() {
        Bullet bullet = shoot(8, 1, Direction.RIGHT);

        tick(2);

        assertFalse(bullet.isAlive());
        assertTrue(removed.contains(bullet));
        assertTrue(level.isFree(new GridPoint2(9, 1)));
    }

    @Test
    void shotStraightIntoTheBorderCreatesNoBullet() {
        Bullet.shoot(new GridPoint2(10, 1), Direction.RIGHT, BULLET_TIME, DAMAGE, level);

        assertTrue(level.isFree(new GridPoint2(9, 1)));
        assertFalse(level.objectAt(new GridPoint2(10, 1)).isPresent());
    }

    @Test
    void vanishesOnTreeWhichStaysInPlace() {
        Tree tree = new Tree(new GridPoint2(4, 1));
        level.add(tree);
        Bullet bullet = shoot(2, 1, Direction.RIGHT);

        tick(3);

        assertFalse(bullet.isAlive());
        assertTrue(removed.contains(bullet));
        assertFalse(removed.contains(tree));
        assertEquals(tree, level.objectAt(new GridPoint2(4, 1)).orElse(null));
    }

    @Test
    void damagesTankItHitsAndVanishes() {
        Tank target = tankAt(4, 1);
        level.add(target);
        Bullet bullet = shoot(2, 1, Direction.RIGHT);

        tick(3);

        assertEquals(HEALTH - DAMAGE, target.getHealth());
        assertTrue(target.isAlive());
        assertFalse(bullet.isAlive());
        assertFalse(removed.contains(target));
    }

    @Test
    void removesTankWhoseHealthIsOver() {
        Tank target = new Tank(new GridPoint2(4, 1), Direction.LEFT, TIME_TO_PASS_A_TILE, DAMAGE, readyGun());
        level.add(target);
        shoot(2, 1, Direction.RIGHT);

        tick(3);

        assertFalse(target.isAlive());
        assertTrue(removed.contains(target));
        assertTrue(level.isFree(new GridPoint2(4, 1)));
    }

    @Test
    void hitsTankStandingRightNextToTheShooterWithoutFlying() {
        Tank target = tankAt(2, 1);
        level.add(target);

        Bullet.shoot(new GridPoint2(2, 1), Direction.RIGHT, BULLET_TIME, DAMAGE, level);

        assertEquals(HEALTH - DAMAGE, target.getHealth());
        assertEquals(target, level.objectAt(new GridPoint2(2, 1)).orElse(null));
    }

    @Test
    void pointBlankShotFinishesTankOff() {
        Tank target = new Tank(new GridPoint2(2, 1), Direction.LEFT, TIME_TO_PASS_A_TILE, DAMAGE, readyGun());
        level.add(target);

        Bullet.shoot(new GridPoint2(2, 1), Direction.RIGHT, BULLET_TIME, DAMAGE, level);

        assertTrue(removed.contains(target));
        assertTrue(level.isFree(new GridPoint2(2, 1)));
    }

    @Test
    void twoBulletsFlyingTowardsEachOtherBothVanish() {
        Bullet first = shoot(2, 1, Direction.RIGHT);
        Bullet second = shoot(6, 1, Direction.LEFT);

        tick(3);

        assertFalse(first.isAlive());
        assertFalse(second.isAlive());
        assertTrue(removed.contains(first));
        assertTrue(removed.contains(second));
    }

    @Test
    void hitsTankMovingIntoTheBulletsWay() {
        Tank target = tankAt(4, 0);
        level.add(target);
        target.move(Direction.UP, level);
        Bullet bullet = shoot(2, 1, Direction.RIGHT);

        tick(2);

        assertEquals(HEALTH - DAMAGE, target.getHealth());
        assertFalse(bullet.isAlive());
    }

    @Test
    void secondsPerTileMustBePositive() {
        assertThrows(IllegalArgumentException.class,
                () -> Bullet.shoot(new GridPoint2(2, 1), Direction.RIGHT, 0f, DAMAGE, level));
    }
}
