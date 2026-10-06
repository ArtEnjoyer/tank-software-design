package ru.mipt.bit.platformer.model;

import com.badlogic.gdx.math.GridPoint2;
import ru.mipt.bit.platformer.Updatable;

import java.util.ArrayList;
import java.util.List;

public class Level implements GameField, Updatable {

    private final List<GameObject> objects = new ArrayList<>();

    public void add(GameObject object) {
        objects.add(object);
    }

    @Override
    public boolean isFree(GridPoint2 coordinates) {
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
}
