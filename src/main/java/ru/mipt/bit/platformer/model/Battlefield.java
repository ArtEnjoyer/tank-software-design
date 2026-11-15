package ru.mipt.bit.platformer.model;

import com.badlogic.gdx.math.GridPoint2;

import java.util.Optional;

/**
 * Внутренний контракт доменной модели (domain) для стрельбы и столкновений.
 * Расширяет {@link GameField} поиском, добавлением и удалением объектов.
 * Реализуется доменным {@link Level}; это абстракция игрового мира,
 * а не адаптер внешнего хранилища.
 */
public interface Battlefield extends GameField {

    boolean isInside(GridPoint2 coordinates);

    Optional<GameObject> objectAt(GridPoint2 coordinates);

    void add(GameObject object);

    void remove(GameObject object);
}
