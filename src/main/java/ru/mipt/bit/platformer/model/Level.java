package ru.mipt.bit.platformer.model;

import com.badlogic.gdx.math.GridPoint2;
import ru.mipt.bit.platformer.Updatable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Level implements Battlefield, Updatable {

    private final int width;
    private final int height;
    private final List<GameObject> objects = new ArrayList<>();
    private final List<LevelListener> listeners = new ArrayList<>();

    public Level(int width, int height) {
        if (width <= 0 || height <= 0) {
            throw new IllegalArgumentException("Level size must be positive, got " + width + "x" + height);
        }
        this.width = width;
        this.height = height;
    }

    public void subscribe(LevelListener listener) {
        listeners.add(listener);
    }

    public void unsubscribe(LevelListener listener) {
        listeners.remove(listener);
    }

    @Override
    public void add(GameObject object) {
        objects.add(object);
        for (LevelListener listener : new ArrayList<>(listeners)) {
            listener.onObjectAdded(object);
        }
    }

    @Override
    public void remove(GameObject object) {
        if (!objects.remove(object)) {
            return;
        }
        for (LevelListener listener : new ArrayList<>(listeners)) {
            listener.onObjectRemoved(object);
        }
    }

    @Override
    public boolean isInside(GridPoint2 coordinates) {
        return coordinates.x >= 0 && coordinates.x < width && coordinates.y >= 0 && coordinates.y < height;
    }

    @Override
    public Optional<GameObject> objectAt(GridPoint2 coordinates) {
        for (GameObject object : objects) {
            if (object.occupies(coordinates)) {
                return Optional.of(object);
            }
        }
        return Optional.empty();
    }

    @Override
    public boolean isFree(GridPoint2 coordinates) {
        return isInside(coordinates) && !objectAt(coordinates).isPresent();
    }

    @Override
    public void update(float deltaTime) {
        for (GameObject object : new ArrayList<>(objects)) {
            if (objects.contains(object)) {
                object.update(deltaTime);
            }
        }
    }
}
