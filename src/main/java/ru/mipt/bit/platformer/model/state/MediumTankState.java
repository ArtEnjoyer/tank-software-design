package ru.mipt.bit.platformer.model.state;

/**
 * Среднее состояние танка, слой domain: движение вдвое медленнее,
 * стрельба разрешена. В стандартной политике соответствует здоровью
 * от 15% до 70% исходного включительно.
 */
public final class MediumTankState implements TankState {

    public static final MediumTankState INSTANCE = new MediumTankState();

    private MediumTankState() {
    }

    @Override
    public float movementDurationMultiplier() {
        return 2f;
    }

    @Override
    public boolean canShoot() {
        return true;
    }
}
