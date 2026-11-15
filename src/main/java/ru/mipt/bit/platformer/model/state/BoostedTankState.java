package ru.mipt.bit.platformer.model.state;

/**
 * Пример дополнительного состояния в слое domain: движение вдвое быстрее,
 * стрельба разрешена. Устанавливается явно через {@code Tank.changeState}.
 * Частоту стрельбы не меняет; подбор бустера и срок его действия здесь не реализованы.
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
    public boolean canShoot() {
        return true;
    }
}
