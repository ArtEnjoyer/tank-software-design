package ru.mipt.bit.platformer.model;

import com.badlogic.gdx.math.GridPoint2;

public class Tank implements Obstacle {

    private static final float SECONDS_PER_TILE = 0.4f;

    private final GridPoint2 coordinates;
    private final GridPoint2 destinationCoordinates;
    private Direction direction = Direction.RIGHT;
    private float movementProgress = 1f;

    public Tank(GridPoint2 coordinates) {
        this.coordinates = new GridPoint2(coordinates);
        this.destinationCoordinates = new GridPoint2(coordinates);
    }

    public void move(Direction toDirection, GameField field) {
        if (isMoving()) {
            return;
        }
        direction = toDirection;
        GridPoint2 neighbour = toDirection.neighbourOf(coordinates);
        if (field.isFree(neighbour)) {
            destinationCoordinates.set(neighbour);
            movementProgress = 0f;
        }
    }

    public void update(float deltaTime) {
        if (!isMoving()) {
            return;
        }
        movementProgress = Math.min(1f, movementProgress + deltaTime / SECONDS_PER_TILE);
        if (!isMoving()) {
            coordinates.set(destinationCoordinates);
        }
    }

    @Override
    public boolean occupies(GridPoint2 tileCoordinates) {
        return coordinates.equals(tileCoordinates) || destinationCoordinates.equals(tileCoordinates);
    }

    public boolean isMoving() {
        return movementProgress < 1f;
    }

    public GridPoint2 getCoordinates() {
        return new GridPoint2(coordinates);
    }

    public GridPoint2 getDestinationCoordinates() {
        return new GridPoint2(destinationCoordinates);
    }

    public float getMovementProgress() {
        return movementProgress;
    }

    public Direction getDirection() {
        return direction;
    }
}
