package ru.mipt.bit.platformer.game;

import com.badlogic.gdx.math.GridPoint2;

public enum Direction {

    UP(0, 1, 90f),
    LEFT(-1, 0, -180f),
    DOWN(0, -1, -90f),
    RIGHT(1, 0, 0f);

    private final int xShift;
    private final int yShift;
    private final float rotation;

    Direction(int xShift, int yShift, float rotation) {
        this.xShift = xShift;
        this.yShift = yShift;
        this.rotation = rotation;
    }

    public GridPoint2 neighbourOf(GridPoint2 coordinates) {
        return new GridPoint2(coordinates).add(xShift, yShift);
    }

    public float getRotation() {
        return rotation;
    }
}
