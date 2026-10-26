package ru.mipt.bit.platformer.input.keyboard;

import com.badlogic.gdx.Input;
import ru.mipt.bit.platformer.input.Keyboard;

public class GdxKeyboard implements Keyboard {

    private final Input input;

    public GdxKeyboard(Input input) {
        this.input = input;
    }

    @Override
    public boolean isKeyPressed(int key) {
        return input.isKeyPressed(key);
    }
}
