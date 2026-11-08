package ru.mipt.bit.platformer.graphics;

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
