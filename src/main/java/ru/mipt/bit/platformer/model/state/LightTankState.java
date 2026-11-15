package ru.mipt.bit.platformer.model.state;

/**
 * Лёгкое состояние танка, слой domain: обычная скорость и разрешённая стрельба.
 * В стандартной политике выбирается при здоровье выше 70% исходного.
 */
public final class LightTankState implements TankState {

    public static final LightTankState INSTANCE = new LightTankState();

    private LightTankState() {
    }

    @Override
    public float movementDurationMultiplier() {
        return 1f;
    }

    @Override
    public boolean canShoot() {
        return true;
    }
}
