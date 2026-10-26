package ru.mipt.bit.platformer.model;

import com.badlogic.gdx.math.GridPoint2;

public class Tank implements Movable {

    private final float secondsPerTile;
    private final GridPoint2 coordinates;
    private final GridPoint2 destinationCoordinates;
    private Direction direction;
    private float movementProgress = 1f;

    public Tank(GridPoint2 coordinates, Direction direction, float secondsPerTile) {
        if (secondsPerTile <= 0f) {
            throw new IllegalArgumentException("Seconds per tile must be positive");
        }
        this.secondsPerTile = secondsPerTile;
        this.coordinates = new GridPoint2(coordinates);
        this.destinationCoordinates = new GridPoint2(coordinates);
        this.direction = direction;
    }

    @Override
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

    @Override
    public void update(float deltaTime) {
        if (!isMoving()) {
            return;
        }
        movementProgress = Math.min(1f, movementProgress + deltaTime / secondsPerTile);
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
