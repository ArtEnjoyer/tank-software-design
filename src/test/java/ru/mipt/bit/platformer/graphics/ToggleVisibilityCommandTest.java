package ru.mipt.bit.platformer.graphics;

import org.junit.jupiter.api.Test;
import ru.mipt.bit.platformer.Command;
import ru.mipt.bit.platformer.input.Keyboard;
import ru.mipt.bit.platformer.input.KeyboardInputController;

import static com.badlogic.gdx.Input.Keys.L;
import static java.util.Collections.singletonMap;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ToggleVisibilityCommandTest {

    @Test
    void togglesVisibilityEveryTimeItIsExecuted() {
        VisibilityToggle healthBars = new VisibilityToggle(false);
        ToggleVisibilityCommand command = new ToggleVisibilityCommand(healthBars);

        command.execute();
        assertTrue(healthBars.isVisible());

        command.execute();
        assertFalse(healthBars.isVisible());
    }

    @Test
    void doesNothingUntilExecuted() {
        VisibilityToggle healthBars = new VisibilityToggle(false);

        new ToggleVisibilityCommand(healthBars);

        assertFalse(healthBars.isVisible());
    }

    @Test
    void pressingLShowsAndHidesHealthBars() {
        VisibilityToggle healthBars = new VisibilityToggle(false);
        boolean[] lPressed = {false};
        Keyboard keyboard = key -> key == L && lPressed[0];
        KeyboardInputController controller =
                new KeyboardInputController(keyboard, singletonMap(L, new ToggleVisibilityCommand(healthBars)));

        lPressed[0] = true;
        controller.createCommands().forEach(Command::execute);
        assertTrue(healthBars.isVisible());

        lPressed[0] = false;
        controller.createCommands().forEach(Command::execute);
        assertTrue(healthBars.isVisible());

        lPressed[0] = true;
        controller.createCommands().forEach(Command::execute);
        assertFalse(healthBars.isVisible());
    }
}
