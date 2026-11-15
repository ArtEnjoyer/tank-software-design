package ru.mipt.bit.platformer.input;

/**
 * Абстракция доступа к клавиатуре на стороне инфраструктуры (infrastructure).
 * Это локальный выходной порт адаптера {@code KeyboardInputController}:
 * позволяет опрашивать клавиши без прямой зависимости от libGDX.
 * Клавиатура не является частью доменной модели.
 */
public interface Keyboard {

    boolean isKeyPressed(int key);
}
