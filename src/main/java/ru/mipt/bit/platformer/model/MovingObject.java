package ru.mipt.bit.platformer.model;

import com.badlogic.gdx.math.GridPoint2;

/**
 * Доменный интерфейс (domain) объекта с направлением и прогрессом движения.
 * Описывает состояние модели, которое может читать графический адаптер;
 * сам не является интерфейсом отрисовки или портом внешней системы.
 */
public interface MovingObject extends GameObject {

    GridPoint2 getDestinationCoordinates();

    float getMovementProgress();

    Direction getDirection();
}
