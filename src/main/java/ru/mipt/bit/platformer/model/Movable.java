package ru.mipt.bit.platformer.model;

/**
 * Доменный интерфейс (domain) объекта, способного начать движение.
 * Правила используют внутренний контракт {@link GameField}.
 * Это способность сущности, а не инфраструктурный порт.
 */
public interface Movable extends MovingObject {

    void move(Direction direction, GameField field);
}
