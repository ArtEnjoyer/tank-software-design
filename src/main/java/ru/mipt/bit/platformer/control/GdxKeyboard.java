package ru.mipt.bit.platformer.control;

import com.badlogic.gdx.Input;

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
