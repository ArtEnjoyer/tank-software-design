package ru.mipt.bit.platformer.graphics;

import com.badlogic.gdx.graphics.g2d.Batch;

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
