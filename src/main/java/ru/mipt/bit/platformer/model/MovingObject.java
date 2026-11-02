package ru.mipt.bit.platformer.model;

import com.badlogic.gdx.math.GridPoint2;

public interface MovingObject extends GameObject {

    GridPoint2 getDestinationCoordinates();

    float getMovementProgress();

    Direction getDirection();
}
