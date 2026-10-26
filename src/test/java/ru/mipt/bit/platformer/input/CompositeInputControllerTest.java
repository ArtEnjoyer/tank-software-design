package ru.mipt.bit.platformer.input;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static java.util.Arrays.asList;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;

class CompositeInputControllerTest {

    private final List<String> calls = new ArrayList<>();
    private final CompositeInputController composite = new CompositeInputController();

    @Test
    void doesNothingWithoutControllers() {
        assertDoesNotThrow(composite::handleInput);
    }

    @Test
    void asksEveryControllerInTheOrderTheyWereAdded() {
        composite.add(() -> calls.add("player"));
        composite.add(() -> calls.add("enemy 1"));
        composite.add(() -> calls.add("enemy 2"));

        composite.handleInput();

        assertEquals(asList("player", "enemy 1", "enemy 2"), calls);
    }

    @Test
    void asksControllersOnEveryTurn() {
        composite.add(() -> calls.add("player"));

        composite.handleInput();
        composite.handleInput();

        assertEquals(asList("player", "player"), calls);
    }

    @Test
    void controllerAddedDuringTurnStartsOnTheNextTurn() {
        composite.add(() -> {
            calls.add("spawner");
            composite.add(() -> calls.add("newcomer"));
        });

        composite.handleInput();
        assertEquals(asList("spawner"), calls);

        calls.clear();
        composite.handleInput();
        assertEquals(asList("spawner", "newcomer"), calls);
    }
}
