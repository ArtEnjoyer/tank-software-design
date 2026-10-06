package ru.mipt.bit.platformer.input;

import ru.mipt.bit.platformer.Command;
import ru.mipt.bit.platformer.InputController;

import java.util.LinkedHashMap;
import java.util.Map;

public class KeyboardInputController implements InputController {

    private final Keyboard keyboard;
    private final Map<Integer, Command> commandByKey;

    public KeyboardInputController(Keyboard keyboard, Map<Integer, ? extends Command> commandByKey) {
        this.keyboard = keyboard;
        this.commandByKey = new LinkedHashMap<>(commandByKey);
    }

    @Override
    public void handleInput() {
        for (Map.Entry<Integer, Command> binding : commandByKey.entrySet()) {
            if (keyboard.isKeyPressed(binding.getKey())) {
                binding.getValue().execute();
            }
        }
    }
}
