package ru.mipt.bit.platformer.model.state;

/**
 * Тяжёлое состояние танка, слой domain: движение втрое медленнее,
 * стрельба запрещена. В стандартной политике выбирается ниже 15% здоровья.
 */
public final class HeavyTankState implements TankState {

    public static final HeavyTankState INSTANCE = new HeavyTankState();

    private HeavyTankState() {
    }

    @Override
    public float movementDurationMultiplier() {
        return 3f;
    }

    @Override
    public boolean canShoot() {
        return false;
    }
}
