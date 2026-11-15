package ru.mipt.bit.platformer.graphics;

import com.badlogic.gdx.graphics.g2d.Batch;

/**
 * Базовый декоратор представления внутри графического адаптера.
 * Слой инфраструктуры (infrastructure): делегирует отрисовку вложенному
 * {@link Renderable} и позволяет добавлять визуальные эффекты.
 */
public abstract class RenderableDecorator implements Renderable {

    private final Renderable wrapped;

    protected RenderableDecorator(Renderable wrapped) {
        this.wrapped = wrapped;
    }

    @Override
    public void render(Batch batch) {
        wrapped.render(batch);
    }
}
