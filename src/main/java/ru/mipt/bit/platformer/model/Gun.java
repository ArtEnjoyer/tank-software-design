package ru.mipt.bit.platformer.model;

import com.badlogic.gdx.math.GridPoint2;

public class Gun {

    private final float bulletSecondsPerTile;
    private final int damage;
    private final float reloadSeconds;
    private float timeSinceShot;

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
        this.timeSinceShot = reloadSeconds;
    }

    public void update(float deltaTime) {
        timeSinceShot = Math.min(reloadSeconds, timeSinceShot + deltaTime);
    }

    public boolean isReady() {
        return timeSinceShot >= reloadSeconds;
    }

    public void fire(GridPoint2 shooterCoordinates, Direction direction, Battlefield field) {
        if (!isReady()) {
            return;
        }
        timeSinceShot = 0f;
        Bullet.shoot(direction.neighbourOf(shooterCoordinates), direction, bulletSecondsPerTile, damage, field);
    }
}
