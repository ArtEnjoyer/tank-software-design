package ru.mipt.bit.platformer.model;

import com.badlogic.gdx.math.GridPoint2;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class DestructibleTest {

    private static Destructible withHealth(int health, int maxHealth) {
        return new Destructible() {
            @Override
            public int getHealth() {
                return health;
            }

            @Override
            public int getMaxHealth() {
                return maxHealth;
            }

            @Override
            public GridPoint2 getCoordinates() {
                return new GridPoint2();
            }

            @Override
            public boolean occupies(GridPoint2 tileCoordinates) {
                return false;
            }
        };
    }

    @Test
    void fractionIsHealthPartOfMaximum() {
        assertEquals(0.75f, withHealth(60, 80).getHealthFraction(), 1e-6f);
    }

    @Test
    void fractionIsFullForUntouchedObject() {
        assertEquals(1f, withHealth(90, 90).getHealthFraction(), 1e-6f);
    }

    @Test
    void fractionNeverGoesBelowZero() {
        assertEquals(0f, withHealth(-15, 100).getHealthFraction(), 1e-6f);
    }

    @Test
    void fractionNeverGoesAboveOne() {
        assertEquals(1f, withHealth(150, 100).getHealthFraction(), 1e-6f);
    }
}
