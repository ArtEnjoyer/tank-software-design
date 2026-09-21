package ru.mipt.bit.platformer.control;

import java.util.LinkedHashMap;
import java.util.Map;

public class KeyboardInputHandler {

    private final Keyboard keyboard;
    private final Map<Integer, Command> commandByKey;

    public KeyboardInputHandler(Keyboard keyboard, Map<Integer, Command> commandByKey) {
        this.keyboard = keyboard;
        this.commandByKey = new LinkedHashMap<>(commandByKey);
    }

    public void handleInput() {
        for (Map.Entry<Integer, Command> binding : commandByKey.entrySet()) {
            if (keyboard.isKeyPressed(binding.getKey())) {
                binding.getValue().execute();
            }
        }
    }
}
