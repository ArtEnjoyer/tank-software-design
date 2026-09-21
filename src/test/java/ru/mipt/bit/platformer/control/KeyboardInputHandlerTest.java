package ru.mipt.bit.platformer.control;

import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import static com.badlogic.gdx.Input.Keys.SPACE;
import static com.badlogic.gdx.Input.Keys.UP;
import static com.badlogic.gdx.Input.Keys.W;
import static java.util.Arrays.asList;
import static java.util.Collections.singletonMap;
import static org.junit.jupiter.api.Assertions.assertEquals;

class KeyboardInputHandlerTest {

    private static class CountingCommand implements Command {

        private int executions;

        @Override
        public void execute() {
            executions++;
        }
    }

    private static Keyboard pressed(Integer... keys) {
        Set<Integer> pressedKeys = new HashSet<>(asList(keys));
        return pressedKeys::contains;
    }

    @Test
    void executesCommandBoundToPressedKey() {
        CountingCommand command = new CountingCommand();
        KeyboardInputHandler handler = new KeyboardInputHandler(pressed(UP), singletonMap(UP, command));

        handler.handleInput();

        assertEquals(1, command.executions);
    }

    @Test
    void doesNothingWhileKeyIsNotPressed() {
        CountingCommand command = new CountingCommand();
        KeyboardInputHandler handler = new KeyboardInputHandler(pressed(), singletonMap(UP, command));

        handler.handleInput();

        assertEquals(0, command.executions);
    }

    @Test
    void ignoresPressedKeysWithoutBinding() {
        CountingCommand command = new CountingCommand();
        KeyboardInputHandler handler = new KeyboardInputHandler(pressed(SPACE), singletonMap(UP, command));

        handler.handleInput();

        assertEquals(0, command.executions);
    }

    @Test
    void executesEveryCommandWhoseKeyIsPressed() {
        CountingCommand move = new CountingCommand();
        CountingCommand shoot = new CountingCommand();
        Map<Integer, Command> bindings = new LinkedHashMap<>();
        bindings.put(UP, move);
        bindings.put(SPACE, shoot);
        KeyboardInputHandler handler = new KeyboardInputHandler(pressed(UP, SPACE), bindings);

        handler.handleInput();

        assertEquals(1, move.executions);
        assertEquals(1, shoot.executions);
    }

    @Test
    void severalKeysMayBeBoundToTheSameCommand() {
        CountingCommand move = new CountingCommand();
        Map<Integer, Command> bindings = new LinkedHashMap<>();
        bindings.put(UP, move);
        bindings.put(W, move);
        KeyboardInputHandler handler = new KeyboardInputHandler(pressed(W), bindings);

        handler.handleInput();

        assertEquals(1, move.executions);
    }

    @Test
    void bindingsCannotBeChangedAfterHandlerIsCreated() {
        CountingCommand added = new CountingCommand();
        Map<Integer, Command> bindings = new LinkedHashMap<>();
        KeyboardInputHandler handler = new KeyboardInputHandler(pressed(SPACE), bindings);

        bindings.put(SPACE, added);
        handler.handleInput();

        assertEquals(0, added.executions);
    }
}
