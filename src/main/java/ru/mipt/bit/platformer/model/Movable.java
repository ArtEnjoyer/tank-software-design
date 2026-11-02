package ru.mipt.bit.platformer.model;

public interface Movable extends MovingObject {

    void move(Direction direction, GameField field);
}
