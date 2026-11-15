package ru.mipt.bit.platformer.input.keyboard;

import com.badlogic.gdx.Input;
import ru.mipt.bit.platformer.input.Keyboard;

/**
 * Вызываемый адаптер инфраструктуры (infrastructure) для порта {@code Keyboard}.
 * Делегирует libGDX проверку удержания клавиши, изолируя библиотеку
 * от контроллера привязок клавиш к командам.
 */
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
