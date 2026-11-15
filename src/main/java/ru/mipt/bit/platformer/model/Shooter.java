package ru.mipt.bit.platformer.model;

/**
 * Доменный интерфейс (domain) объекта, способного стрелять.
 * Операция использует внутренний контракт {@link Battlefield};
 * не является портом внешней системы.
 */
public interface Shooter {

    void shoot(Battlefield field);
}
