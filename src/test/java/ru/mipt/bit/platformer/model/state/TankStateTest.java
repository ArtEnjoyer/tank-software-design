package ru.mipt.bit.platformer.model.state;

import com.badlogic.gdx.math.GridPoint2;
import org.junit.jupiter.api.Test;
import ru.mipt.bit.platformer.model.Direction;
import ru.mipt.bit.platformer.model.GameField;
import ru.mipt.bit.platformer.model.Level;
import ru.mipt.bit.platformer.model.Tank;

import java.util.Arrays;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static ru.mipt.bit.platformer.model.TestTanks.HEALTH;
import static ru.mipt.bit.platformer.model.TestTanks.TIME_TO_PASS_A_TILE;
import static ru.mipt.bit.platformer.model.TestTanks.readyGun;
import static ru.mipt.bit.platformer.model.TestTanks.tankAt;

class TankStateTest {

    private static final GameField FREE_FIELD = coordinates -> true;

    @Test
    void startsInLightStateAtFullHealth() {
        Tank tank = tankAt(1, 1);

        assertSame(LightTankState.INSTANCE, tank.getState());
        assertTrue(tank.getState().canShoot());
        assertEquals(1f, tank.getState().movementDurationMultiplier(), 1e-6f);
    }

    @Test
    void becomesMediumWhenHealthIsBetweenFifteenAndSeventyPercent() {
        Tank tank = tankAt(1, 1);

        tank.takeDamage(30);

        assertEquals(70, tank.getHealth());
        assertSame(MediumTankState.INSTANCE, tank.getState());
        assertEquals(2f, tank.getState().movementDurationMultiplier(), 1e-6f);
        assertTrue(tank.getState().canShoot());
    }

    @Test
    void staysMediumAtFifteenPercentHealth() {
        Tank tank = tankAt(1, 1);

        tank.takeDamage(85);

        assertEquals(15, tank.getHealth());
        assertSame(MediumTankState.INSTANCE, tank.getState());
    }

    @Test
    void becomesHeavyBelowFifteenPercentAndCannotShoot() {
        Tank tank = tankAt(1, 1);
        Level level = new Level(10, 8);
        level.add(tank);

        tank.takeDamage(86);

        assertEquals(14, tank.getHealth());
        assertSame(HeavyTankState.INSTANCE, tank.getState());
        assertFalse(tank.getState().canShoot());
        assertEquals(3f, tank.getState().movementDurationMultiplier(), 1e-6f);

        tank.shoot(level);

        assertFalse(level.objectAt(new GridPoint2(2, 1)).isPresent());
    }


    @Test
    void movesTwiceAsSlowInMediumState() {
        Tank tank = tankAt(1, 1);
        tank.takeDamage(30);

        tank.move(Direction.UP, FREE_FIELD);
        tank.update(TIME_TO_PASS_A_TILE);

        assertEquals(0.5f, tank.getMovementProgress(), 1e-6f);
        assertEquals(new GridPoint2(1, 1), tank.getCoordinates());
        assertTrue(tank.isMoving());
    }

    @Test
    void movesThreeTimesAsSlowInHeavyState() {
        Tank tank = tankAt(1, 1);
        tank.takeDamage(86);

        tank.move(Direction.UP, FREE_FIELD);
        tank.update(TIME_TO_PASS_A_TILE);

        assertEquals(1f / 3f, tank.getMovementProgress(), 1e-6f);
        assertTrue(tank.isMoving());
    }

    @Test
    void acceptsBoosterStateWithoutChangingResolver() {
        Tank tank = tankAt(1, 1);

        tank.changeState(BoostedTankState.INSTANCE);

        assertSame(BoostedTankState.INSTANCE, tank.getState());
        assertEquals(0.5f, tank.getState().movementDurationMultiplier(), 1e-6f);

        tank.move(Direction.UP, FREE_FIELD);
        tank.update(TIME_TO_PASS_A_TILE / 2f);

        assertEquals(1f, tank.getMovementProgress(), 1e-6f);
        assertEquals(new GridPoint2(1, 2), tank.getCoordinates());
    }

    @Test
    void returnsToHealthBasedStateAfterBoosterEnds() {
        Tank tank = tankAt(1, 1);
        tank.takeDamage(40);
        tank.changeState(BoostedTankState.INSTANCE);

        tank.refreshStateFromHealth();

        assertSame(MediumTankState.INSTANCE, tank.getState());
    }

    @Test
    void staysLightJustAboveSeventyPercent() {
        Tank tank = new Tank(new GridPoint2(1, 1), Direction.RIGHT, TIME_TO_PASS_A_TILE, 100, readyGun());

        tank.takeDamage(29);

        assertEquals(71, tank.getHealth());
        assertSame(LightTankState.INSTANCE, tank.getState());
    }

    @Test
    void resolverRejectsEmptyStateList() {
        assertThrows(IllegalArgumentException.class, () -> new HealthBasedTankStateResolver(Collections.emptyList()));
    }

    @Test
    void customResolverCanIntroduceAdditionalHealthBand() {
        TankStateResolver resolver = new HealthBasedTankStateResolver(Arrays.asList(
                new HealthRangeTankState(0.9f, false, 1f, true, LightTankState.INSTANCE),
                new HealthRangeTankState(0.15f, true, 0.9f, true, MediumTankState.INSTANCE),
                new HealthRangeTankState(Float.NEGATIVE_INFINITY, true, 0.15f, false, HeavyTankState.INSTANCE)
        ));
        Tank tank = new Tank(new GridPoint2(1, 1), Direction.RIGHT, TIME_TO_PASS_A_TILE, HEALTH, readyGun(), resolver);

        tank.takeDamage(11);

        assertEquals(89, tank.getHealth());
        assertSame(MediumTankState.INSTANCE, tank.getState());
    }
}

