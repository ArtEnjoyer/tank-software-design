package ru.mipt.bit.platformer.model;

import com.badlogic.gdx.math.GridPoint2;

public class Tank implements Movable, Shooter {

    private final float secondsPerTile;
    private final Gun gun;
    private final GridPoint2 coordinates;
    private final GridPoint2 destinationCoordinates;
    private Direction direction;
    private float movementProgress = 1f;
    private int health;

    public Tank(GridPoint2 coordinates, Direction direction, float secondsPerTile, int health, Gun gun) {
        if (secondsPerTile <= 0f) {
            throw new IllegalArgumentException("Seconds per tile must be positive");
        }
        if (health <= 0) {
            throw new IllegalArgumentException("Health must be positive, got " + health);
        }
        this.secondsPerTile = secondsPerTile;
        this.gun = gun;
        this.coordinates = new GridPoint2(coordinates);
        this.destinationCoordinates = new GridPoint2(coordinates);
        this.direction = direction;
        this.health = health;
    }

    @Override
    public void move(Direction toDirection, GameField field) {
        if (!isAlive() || isMoving()) {
            return;
        }
        direction = toDirection;
        GridPoint2 neighbour = toDirection.neighbourOf(coordinates);
        if (field.isFree(neighbour)) {
            destinationCoordinates.set(neighbour);
            movementProgress = 0f;
        }
    }

    @Override
    public void shoot(Battlefield field) {
        if (!isAlive() || isMoving()) {
            return;
        }
        gun.fire(coordinates, direction, field);
    }

    @Override
    public void update(float deltaTime) {
        gun.update(deltaTime);
        if (!isMoving()) {
            return;
        }
        movementProgress = Math.min(1f, movementProgress + deltaTime / secondsPerTile);
        if (!isMoving()) {
            coordinates.set(destinationCoordinates);
        }
    }

    @Override
    public void takeDamage(int damage) {
        health -= damage;
    }

    @Override
    public boolean isAlive() {
        return health > 0;
    }

    @Override
    public boolean occupies(GridPoint2 tileCoordinates) {
        return coordinates.equals(tileCoordinates) || destinationCoordinates.equals(tileCoordinates);
    }

    public boolean isMoving() {
        return movementProgress < 1f;
    }

    public int getHealth() {
        return health;
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
}
