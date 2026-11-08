package ru.mipt.bit.platformer.input.keyboard;

import com.badlogic.gdx.Input;
import ru.mipt.bit.platformer.input.Keyboard;

public class GdxJustPressedKeyboard implements Keyboard {

    private final Input input;

    public GdxJustPressedKeyboard(Input input) {
        this.input = input;
    }

    @Override
    public boolean isKeyPressed(int key) {
        return input.isKeyJustPressed(key);
    }
}
