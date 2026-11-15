package ru.mipt.bit.platformer.model;

/**
 * Доменный интерфейс (domain) объекта с запасом здоровья.
 * Предоставляет текущее и исходное здоровье и вычисляет оставшуюся долю.
 * Это характеристика сущности, а не отдельный архитектурный порт.
 */
public interface Destructible extends GameObject {

    int getHealth();

    int getMaxHealth();

    default float getHealthFraction() {
        return Math.max(0f, Math.min(1f, (float) getHealth() / getMaxHealth()));
    }
}
