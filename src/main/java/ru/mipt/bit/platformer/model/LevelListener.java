package ru.mipt.bit.platformer.model;

/**
 * Выходной порт доменной модели (domain) для уведомлений об объектах уровня.
 * Реализации получают события появления и удаления объектов.
 * Графический адаптер подписывается на них, поэтому {@link Level}
 * не должен знать о libGDX или конкретном представлении.
 */
public interface LevelListener {

    void onObjectAdded(GameObject object);

    void onObjectRemoved(GameObject object);
}
