package ru.mipt.bit.platformer.model.state;

/**
 * Дополнительное состояние в слое domain: движение и перезарядка вдвое быстрее,
 * стрельба разрешена. Устанавливается явно через {@code Tank.changeState}.
 * Подбор бустера и срок его действия здесь не реализованы.
 */
public final class BoostedTankState implements TankState {

    public static final BoostedTankState INSTANCE = new BoostedTankState();

    private BoostedTankState() {
    }

    @Override
    public float movementDurationMultiplier() {
        return 0.5f;
    }

    @Override
    public float reloadDurationMultiplier() {
        return 0.5f;
    }

    @Override
    public boolean canShoot() {
        return true;
    }
}
