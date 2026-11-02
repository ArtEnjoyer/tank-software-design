package ru.mipt.bit.platformer.model;

public interface LevelListener {

    void onObjectAdded(GameObject object);

    void onObjectRemoved(GameObject object);
}
