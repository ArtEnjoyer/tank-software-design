package ru.mipt.bit.platformer;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;
import static org.junit.jupiter.api.Assertions.assertEquals;

class GameTest {

    private final List<String> calls = new ArrayList<>();

    private final Renderer renderer = new Renderer() {
        @Override
        public void render() {
            calls.add("render");
        }

        @Override
        public void dispose() {
            calls.add("dispose");
        }
    };

    private final Game game = new Game(
            () -> calls.add("input"),
            deltaTime -> calls.add("update " + deltaTime),
            renderer);

    @Test
    void handlesInputBeforeUpdatingTheWorld() {
        game.update(0.5f);

        assertEquals(asList("input", "update 0.5"), calls);
    }

    @Test
    void renderingDoesNotTouchInputAndWorld() {
        game.render();

        assertEquals(singletonList("render"), calls);
    }

    @Test
    void releasesRendererResourcesOnDispose() {
        game.dispose();

        assertEquals(singletonList("dispose"), calls);
    }
}
