package ru.mipt.bit.platformer.model;

import com.badlogic.gdx.math.GridPoint2;

import java.util.Optional;

public class Bullet implements MovingObject {

    private final Direction direction;
    private final float secondsPerTile;
    private final int damage;
    private final Battlefield field;
    private final GridPoint2 coordinates;
    private final GridPoint2 destinationCoordinates;
    private float movementProgress = 1f;
    private boolean alive = true;

    private Bullet(GridPoint2 coordinates, Direction direction, float secondsPerTile, int damage, Battlefield field) {
        this.direction = direction;
        this.secondsPerTile = secondsPerTile;
        this.damage = damage;
        this.field = field;
        this.coordinates = new GridPoint2(coordinates);
        this.destinationCoordinates = new GridPoint2(coordinates);
    }

    public static void shoot(GridPoint2 start, Direction direction, float secondsPerTile, int damage, Battlefield field) {
        if (secondsPerTile <= 0f) {
            throw new IllegalArgumentException("Seconds per tile must be positive");
        }
        if (strikes(start, damage, field)) {
            return;
        }
        field.add(new Bullet(start, direction, secondsPerTile, damage, field));
    }

    @Override
    public void update(float deltaTime) {
        if (!alive) {
            return;
        }
        if (!isMoving()) {
            GridPoint2 next = direction.neighbourOf(coordinates);
            if (strikes(next, damage, field)) {
                vanish();
                return;
            }
            destinationCoordinates.set(next);
            movementProgress = 0f;
        }
        movementProgress = Math.min(1f, movementProgress + deltaTime / secondsPerTile);
        if (!isMoving()) {
            coordinates.set(destinationCoordinates);
        }
    }

    @Override
    public void takeDamage(int damage) {
        vanish();
    }

    @Override
    public boolean isAlive() {
        return alive;
    }

    @Override
    public boolean occupies(GridPoint2 tileCoordinates) {
        return coordinates.equals(tileCoordinates) || destinationCoordinates.equals(tileCoordinates);
    }

    @Override
    public GridPoint2 getCoordinates() {
        return new GridPoint2(coordinates);
    }

    @Override
    public GridPoint2 getDestinationCoordinates() {
        return new GridPoint2(destinationCoordinates);
    }

    @Override
    public float getMovementProgress() {
        return movementProgress;
    }

    @Override
    public Direction getDirection() {
        return direction;
    }

    private boolean isMoving() {
        return movementProgress < 1f;
    }

    private void vanish() {
        alive = false;
        field.remove(this);
    }

    private static boolean strikes(GridPoint2 tile, int damage, Battlefield field) {
        if (!field.isInside(tile)) {
            return true;
        }
        Optional<GameObject> target = field.objectAt(tile);
        if (!target.isPresent()) {
            return false;
        }
        target.get().takeDamage(damage);
        if (!target.get().isAlive()) {
            field.remove(target.get());
        }
        return true;
    }
}
