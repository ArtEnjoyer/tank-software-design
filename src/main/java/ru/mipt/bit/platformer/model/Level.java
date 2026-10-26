package ru.mipt.bit.platformer.model;

import com.badlogic.gdx.math.GridPoint2;
import ru.mipt.bit.platformer.Updatable;

import java.util.ArrayList;
import java.util.List;

public class Level implements GameField, Updatable {

    private final int width;
    private final int height;
    private final List<GameObject> objects = new ArrayList<>();

    public Level(int width, int height) {
        if (width <= 0 || height <= 0) {
            throw new IllegalArgumentException("Level size must be positive, got " + width + "x" + height);
        }
        this.width = width;
        this.height = height;
    }

    public void add(GameObject object) {
        objects.add(object);
    }

    @Override
    public boolean isFree(GridPoint2 coordinates) {
        if (!isInside(coordinates)) {
            return false;
        }
        for (GameObject object : objects) {
            if (object.occupies(coordinates)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public void update(float deltaTime) {
        for (GameObject object : new ArrayList<>(objects)) {
            object.update(deltaTime);
        }
    }

    private boolean isInside(GridPoint2 coordinates) {
        return coordinates.x >= 0 && coordinates.x < width && coordinates.y >= 0 && coordinates.y < height;
    }
}
