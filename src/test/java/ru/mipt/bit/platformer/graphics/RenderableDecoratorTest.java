package ru.mipt.bit.platformer.graphics;

import com.badlogic.gdx.graphics.g2d.Batch;
import org.junit.jupiter.api.Test;
import ru.mipt.bit.platformer.model.Tank;

import java.util.ArrayList;
import java.util.List;

import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static ru.mipt.bit.platformer.model.TestTanks.tankAt;

class RenderableDecoratorTest {

    private final List<String> drawn = new ArrayList<>();
    private final Renderable tankSprite = batch -> drawn.add("tank");

    @Test
    void baseDecoratorJustDrawsWrappedGraphics() {
        Renderable decorated = new RenderableDecorator(tankSprite) {
        };

        decorated.render(null);

        assertEquals(singletonList("tank"), drawn);
    }

    @Test
    void decoratorsCanBeStackedAndKeepTheOrder() {
        Renderable inner = new RenderableDecorator(tankSprite) {
            @Override
            public void render(Batch batch) {
                super.render(batch);
                drawn.add("inner extra");
            }
        };
        Renderable outer = new RenderableDecorator(inner) {
            @Override
            public void render(Batch batch) {
                super.render(batch);
                drawn.add("outer extra");
            }
        };

        outer.render(null);

        assertEquals(asList("tank", "inner extra", "outer extra"), drawn);
    }

    @Test
    void hiddenHealthBarDrawsOnlyTheWrappedGraphics() {
        Tank tank = tankAt(1, 1);
        Renderable withHealthBar = new HealthBarDecorator(tankSprite, tank, null, null, new VisibilityToggle(false));

        withHealthBar.render(null);

        assertEquals(singletonList("tank"), drawn);
    }
}
