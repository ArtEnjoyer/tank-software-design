package ru.mipt.bit.platformer.graphics;

import com.badlogic.gdx.graphics.g2d.Batch;

/**
 * Внутренний интерфейс графического адаптера в слое инфраструктуры (infrastructure).
 * Описывает отрисовку одного представления. Параметр {@code Batch} относится
 * к libGDX, поэтому этот интерфейс не служит независимым портом ядра.
 * Граница с приложением проходит через {@code Renderer}.
 */
public interface Renderable {

    void render(Batch batch);
}
