package ru.mipt.bit.platformer.graphics;

/**
 * Состояние представления в слое инфраструктуры (infrastructure).
 * Хранит видимость визуального элемента; к игровым сущностям (domain) не относится.
 */
public class VisibilityToggle {

    private boolean visible;

    public VisibilityToggle(boolean visible) {
        this.visible = visible;
    }

    public void toggle() {
        visible = !visible;
    }

    public boolean isVisible() {
        return visible;
    }
}
