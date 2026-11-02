package ru.mipt.bit.platformer.model;

import com.badlogic.gdx.math.GridPoint2;

public interface GameField {

    boolean isFree(GridPoint2 coordinates);
}
