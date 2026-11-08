package ru.mipt.bit.platformer.model;

import com.badlogic.gdx.math.GridPoint2;

import java.util.Optional;

public interface Battlefield extends GameField {

    boolean isInside(GridPoint2 coordinates);

    Optional<GameObject> objectAt(GridPoint2 coordinates);

    void add(GameObject object);

    void remove(GameObject object);
}
