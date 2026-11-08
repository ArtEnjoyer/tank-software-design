package ru.mipt.bit.platformer.model;

import com.badlogic.gdx.math.GridPoint2;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static java.util.Collections.singletonList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static ru.mipt.bit.platformer.model.TestTanks.tankAt;

class ShootCommandTest {

    @Test
    void asksShooterToShootOnTheGivenField() {
        Level level = new Level(5, 5);
        List<Battlefield> fieldsShotAt = new ArrayList<>();

        new ShootCommand(fieldsShotAt::add, level).execute();

        assertEquals(singletonList(level), fieldsShotAt);
    }

    @Test
    void makesTankFire() {
        Level level = new Level(5, 5);
        Tank tank = tankAt(1, 1, Direction.UP);
        level.add(tank);

        new ShootCommand(tank, level).execute();

        assertTrue(level.objectAt(new GridPoint2(1, 2)).orElse(null) instanceof Bullet);
    }

    @Test
    void doesNothingUntilExecuted() {
        Level level = new Level(5, 5);
        Tank tank = tankAt(1, 1);
        level.add(tank);

        new ShootCommand(tank, level);

        assertFalse(level.objectAt(new GridPoint2(2, 1)).isPresent());
    }
}
