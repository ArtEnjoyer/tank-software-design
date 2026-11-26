package ru.mipt.bit.platformer.model.state;

/**
 * Доменный объект (domain): диапазон здоровья и связанное с ним поведение.
 * Проверяет принадлежность диапазону и делегирует операции {@link TankState}
 * вложенному состоянию. Не является портом или инфраструктурным адаптером.
 */
public final class HealthRangeTankState implements TankState {

    private final float min;
    private final float max;
    private final boolean minInclusive;
    private final boolean maxInclusive;
    private final TankState behavior;

    public HealthRangeTankState(float min, boolean minInclusive, float max, boolean maxInclusive, TankState behavior) {
        if (min > max) {
            throw new IllegalArgumentException("Health range min must not exceed max");
        }
        this.min = min;
        this.max = max;
        this.minInclusive = minInclusive;
        this.maxInclusive = maxInclusive;
        this.behavior = behavior;
    }

    public boolean matches(float healthFraction) {
        boolean aboveMin = minInclusive ? healthFraction >= min : healthFraction > min;
        boolean belowMax = maxInclusive ? healthFraction <= max : healthFraction < max;
        return aboveMin && belowMax;
    }

    public TankState behavior() {
        return behavior;
    }

    @Override
    public float movementDurationMultiplier() {
        return behavior.movementDurationMultiplier();
    }

    @Override
    public float reloadDurationMultiplier() {
        return behavior.reloadDurationMultiplier();
    }

    @Override
    public boolean canShoot() {
        return behavior.canShoot();
    }
}

