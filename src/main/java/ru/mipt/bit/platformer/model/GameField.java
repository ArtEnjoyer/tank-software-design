package ru.mipt.bit.platformer.model;

import com.badlogic.gdx.math.GridPoint2;

/**
 * Внутренний контракт доменной модели (domain): проверка доступности клетки.
 * Позволяет правилам движения не зависеть от конкретного {@link Level}.
 * В текущей реализации обе стороны находятся в domain, поэтому это не порт
 * границы с инфраструктурой.
 */
public interface GameField {

    boolean isFree(GridPoint2 coordinates);
}
