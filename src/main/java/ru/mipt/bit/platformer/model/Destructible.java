package ru.mipt.bit.platformer.model;

public interface Destructible extends GameObject {

    int getHealth();

    int getMaxHealth();

    default float getHealthFraction() {
        return Math.max(0f, Math.min(1f, (float) getHealth() / getMaxHealth()));
    }
}
