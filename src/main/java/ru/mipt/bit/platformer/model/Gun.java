package ru.mipt.bit.platformer.model;

import com.badlogic.gdx.math.GridPoint2;

/**
 * Доменный объект (domain): правила перезарядки и создания выстрела.
 * Работает с игровым временем и внутренним контрактом {@link Battlefield},
 * не зависит от реальных часов, ввода или отрисовки.
 * Хранит прогресс перезарядки; получает только числовой множитель её длительности,
 * не зная о конкретных состояниях танка.
 */
public class Gun {

    private final float bulletSecondsPerTile;
    private final int damage;
    private final float reloadSeconds;
    private float reloadProgress = 1f;

    public Gun(float bulletSecondsPerTile, int damage, float reloadSeconds) {
        if (bulletSecondsPerTile <= 0f) {
            throw new IllegalArgumentException("Bullet seconds per tile must be positive");
        }
        if (damage <= 0) {
            throw new IllegalArgumentException("Damage must be positive, got " + damage);
        }
        if (reloadSeconds < 0f) {
            throw new IllegalArgumentException("Reload time must not be negative");
        }
        this.bulletSecondsPerTile = bulletSecondsPerTile;
        this.damage = damage;
        this.reloadSeconds = reloadSeconds;
    }

    public void update(float deltaTime) {
        update(deltaTime, 1f);
    }

    /**
     * Продвигает перезарядку с заданным множителем базовой длительности.
     * При смене множителя сохраняется уже выполненная доля перезарядки:
     * новое значение влияет только на дальнейшее накопление прогресса.
     * Готовое оружие остаётся готовым до выстрела.
     *
     * @param deltaTime прошедшее игровое время в секундах
     * @param reloadDurationMultiplier положительный конечный множитель длительности
     * @throws IllegalArgumentException если множитель неположительный или неконечный
     */
    public void update(float deltaTime, float reloadDurationMultiplier) {
        if (reloadDurationMultiplier <= 0f || !Float.isFinite(reloadDurationMultiplier)) {
            throw new IllegalArgumentException("Reload duration multiplier must be positive and finite");
        }
        if (reloadSeconds == 0f) {
            return;
        }
        float reloadDuration = reloadSeconds * reloadDurationMultiplier;
        reloadProgress = Math.min(1f, reloadProgress + deltaTime / reloadDuration);
    }

    public boolean isReady() {
        return reloadProgress >= 1f;
    }

    public void fire(GridPoint2 shooterCoordinates, Direction direction, Battlefield field) {
        if (!isReady()) {
            return;
        }
        reloadProgress = reloadSeconds == 0f ? 1f : 0f;
        Bullet.shoot(direction.neighbourOf(shooterCoordinates), direction, bulletSecondsPerTile, damage, field);
    }
}
