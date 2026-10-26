package ru.mipt.bit.platformer.model;

import com.badlogic.gdx.math.GridPoint2;

public interface Movable extends GameObject {

    void move(Direction direction, GameField field);

    GridPoint2 getDestinationCoordinates();

    float getMovementProgress();

    Direction getDirection();
}
