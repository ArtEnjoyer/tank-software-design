package ru.mipt.bit.platformer.model;

import com.badlogic.gdx.math.GridPoint2;

/**
 * Доменный тип (domain): направление и переход к соседней клетке.
 * Сейчас также хранит угол поворота для графики и использует {@code GridPoint2}
 * из libGDX. Для строгого разделения слоёв угол следует вычислять в графическом
 * адаптере, а координаты описывать собственным типом ядра.
 */
public enum Direction {

    UP(0, 1, 90f),
    LEFT(-1, 0, -180f),
    DOWN(0, -1, -90f),
    RIGHT(1, 0, 0f);

    private final GridPoint2 vector;
    private final float rotation;

    Direction(int xShift, int yShift, float rotation) {
        this.vector = new GridPoint2(xShift, yShift);
        this.rotation = rotation;
    }

    public GridPoint2 getVector() {
        return new GridPoint2(vector);
    }

    public float getRotation() {
        return rotation;
    }

    public GridPoint2 neighbourOf(GridPoint2 coordinates) {
        return new GridPoint2(coordinates).add(vector.x, vector.y);
    }
}
