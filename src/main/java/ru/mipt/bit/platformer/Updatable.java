package ru.mipt.bit.platformer;

/**
 * Контракт обновления модели по прошедшему игровому времени.
 * Связывает слой приложения (application) с доменным миром (domain)
 * и его объектами. Сам по себе не является портом внешней системы.
 */
public interface Updatable {

    void update(float deltaTime);
}
