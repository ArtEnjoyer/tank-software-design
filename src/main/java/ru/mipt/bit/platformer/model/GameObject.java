package ru.mipt.bit.platformer.model;

import com.badlogic.gdx.math.GridPoint2;
import ru.mipt.bit.platformer.Updatable;

public interface GameObject extends Updatable {

    GridPoint2 getCoordinates();

    boolean occupies(GridPoint2 tileCoordinates);

    @Override
    default void update(float deltaTime) {
    }
}
