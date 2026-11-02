package ru.mipt.bit.platformer.model;

import com.badlogic.gdx.math.GridPoint2;

public final class TestTanks {

    public static final float TIME_TO_PASS_A_TILE = 0.4f;
    public static final int HEALTH = 100;
    public static final float BULLET_TIME_TO_PASS_A_TILE = 0.1f;
    public static final int BULLET_DAMAGE = 25;

    private TestTanks() {
    }

    public static Tank tankAt(int x, int y) {
        return tankAt(x, y, Direction.RIGHT);
    }

    public static Tank tankAt(int x, int y, Direction direction) {
        return new Tank(new GridPoint2(x, y), direction, TIME_TO_PASS_A_TILE, HEALTH, readyGun());
    }

    public static Gun readyGun() {
        return new Gun(BULLET_TIME_TO_PASS_A_TILE, BULLET_DAMAGE, 0f);
    }
}
