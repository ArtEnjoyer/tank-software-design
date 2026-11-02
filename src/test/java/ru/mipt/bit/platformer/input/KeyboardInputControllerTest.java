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
import static java.util.Collections.singletonList;
import static java.util.Collections.singletonMap;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

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
    void createsCommandBoundToPressedKey() {
        CountingCommand command = new CountingCommand();
        KeyboardInputController controller = new KeyboardInputController(pressed(UP), singletonMap(UP, command));

        assertEquals(singletonList(command), controller.createCommands());
    }

    @Test
    void onlyCreatesCommandsWithoutExecutingThem() {
        CountingCommand command = new CountingCommand();
        KeyboardInputController controller = new KeyboardInputController(pressed(UP), singletonMap(UP, command));

        controller.createCommands();

        assertEquals(0, command.executions);
    }

    @Test
    void createsNothingWhileKeyIsNotPressed() {
        KeyboardInputController controller = new KeyboardInputController(pressed(), singletonMap(UP, new CountingCommand()));

        assertTrue(controller.createCommands().isEmpty());
    }

    @Test
    void ignoresPressedKeysWithoutBinding() {
        KeyboardInputController controller = new KeyboardInputController(pressed(SPACE), singletonMap(UP, new CountingCommand()));

        assertTrue(controller.createCommands().isEmpty());
    }

    @Test
    void createsEveryCommandWhoseKeyIsPressedInBindingOrder() {
        CountingCommand shoot = new CountingCommand();
        CountingCommand move = new CountingCommand();
        Map<Integer, Command> bindings = new LinkedHashMap<>();
        bindings.put(SPACE, shoot);
        bindings.put(UP, move);
        KeyboardInputController controller = new KeyboardInputController(pressed(UP, SPACE), bindings);

        assertEquals(asList(shoot, move), controller.createCommands());
    }

    @Test
    void severalKeysMayBeBoundToTheSameCommand() {
        CountingCommand move = new CountingCommand();
        Map<Integer, Command> bindings = new LinkedHashMap<>();
        bindings.put(UP, move);
        bindings.put(W, move);
        KeyboardInputController controller = new KeyboardInputController(pressed(W), bindings);

        assertEquals(singletonList(move), controller.createCommands());
    }

    @Test
    void bindingsCannotBeChangedAfterControllerIsCreated() {
        Map<Integer, Command> bindings = new LinkedHashMap<>();
        KeyboardInputController controller = new KeyboardInputController(pressed(SPACE), bindings);

        bindings.put(SPACE, new CountingCommand());

        assertTrue(controller.createCommands().isEmpty());
    }
}
