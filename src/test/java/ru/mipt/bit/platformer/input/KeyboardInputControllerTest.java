package ru.mipt.bit.platformer.input;

import org.junit.jupiter.api.Test;
import ru.mipt.bit.platformer.Command;

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

class KeyboardInputControllerTest {

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
        KeyboardInputController controller = new KeyboardInputController(pressed(UP), singletonMap(UP, command));

        controller.handleInput();

        assertEquals(1, command.executions);
    }

    @Test
    void doesNothingWhileKeyIsNotPressed() {
        CountingCommand command = new CountingCommand();
        KeyboardInputController controller = new KeyboardInputController(pressed(), singletonMap(UP, command));

        controller.handleInput();

        assertEquals(0, command.executions);
    }

    @Test
    void ignoresPressedKeysWithoutBinding() {
        CountingCommand command = new CountingCommand();
        KeyboardInputController controller = new KeyboardInputController(pressed(SPACE), singletonMap(UP, command));

        controller.handleInput();

        assertEquals(0, command.executions);
    }

    @Test
    void executesEveryCommandWhoseKeyIsPressed() {
        CountingCommand move = new CountingCommand();
        CountingCommand shoot = new CountingCommand();
        Map<Integer, Command> bindings = new LinkedHashMap<>();
        bindings.put(UP, move);
        bindings.put(SPACE, shoot);
        KeyboardInputController controller = new KeyboardInputController(pressed(UP, SPACE), bindings);

        controller.handleInput();

        assertEquals(1, move.executions);
        assertEquals(1, shoot.executions);
    }

    @Test
    void severalKeysMayBeBoundToTheSameCommand() {
        CountingCommand move = new CountingCommand();
        Map<Integer, Command> bindings = new LinkedHashMap<>();
        bindings.put(UP, move);
        bindings.put(W, move);
        KeyboardInputController controller = new KeyboardInputController(pressed(W), bindings);

        controller.handleInput();

        assertEquals(1, move.executions);
    }

    @Test
    void bindingsCannotBeChangedAfterControllerIsCreated() {
        CountingCommand added = new CountingCommand();
        Map<Integer, Command> bindings = new LinkedHashMap<>();
        KeyboardInputController controller = new KeyboardInputController(pressed(SPACE), bindings);

        bindings.put(SPACE, added);
        controller.handleInput();

        assertEquals(0, added.executions);
    }
}
