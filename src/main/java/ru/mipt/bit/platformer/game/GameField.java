package ru.mipt.bit.platformer.game;

import com.badlogic.gdx.math.GridPoint2;

public interface GameField {

    boolean isFree(GridPoint2 coordinates);
}
