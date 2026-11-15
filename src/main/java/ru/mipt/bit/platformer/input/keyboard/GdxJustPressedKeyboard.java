package ru.mipt.bit.platformer.input.keyboard;

import com.badlogic.gdx.Input;
import ru.mipt.bit.platformer.input.Keyboard;

/**
 * Вызываемый адаптер инфраструктуры (infrastructure) для порта {@code Keyboard}.
 * Использует libGDX для обнаружения нового нажатия: удержание клавиши
 * не должно повторять команду на каждом кадре.
 */
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
