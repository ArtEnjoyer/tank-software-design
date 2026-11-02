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

    private final InputController inputController = () -> {
        calls.add("create commands");
        return asList(() -> calls.add("execute move"), () -> calls.add("execute shoot"));
    };

    private final Game game = new Game(inputController, deltaTime -> calls.add("update " + deltaTime), renderer);

    @Test
    void createsCommandsThenExecutesThemThenUpdatesTheWorld() {
        game.update(0.5f);

        assertEquals(asList("create commands", "execute move", "execute shoot", "update 0.5"), calls);
    }

    @Test
    void createsAndExecutesCommandsOncePerTurn() {
        game.update(0.1f);
        game.update(0.2f);

        assertEquals(asList(
                "create commands", "execute move", "execute shoot", "update 0.1",
                "create commands", "execute move", "execute shoot", "update 0.2"), calls);
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
