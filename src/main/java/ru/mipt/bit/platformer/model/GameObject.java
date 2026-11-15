package ru.mipt.bit.platformer.model;

import com.badlogic.gdx.math.GridPoint2;
import ru.mipt.bit.platformer.Updatable;

/**
 * Базовый контракт доменных сущностей (domain): позиция, занятые клетки,
 * обновление и реакция на урон. Не является портом внешней системы.
 * Тип {@code GridPoint2} в этом контракте создаёт зависимость domain от libGDX;
 * для полной изоляции ядра нужен собственный тип координат.
 */
public interface GameObject extends Updatable {

    GridPoint2 getCoordinates();

    boolean occupies(GridPoint2 tileCoordinates);

    @Override
    default void update(float deltaTime) {
    }

    default void takeDamage(int damage) {
    }

    default boolean isAlive() {
        return true;
    }
}
