package ru.mipt.bit.platformer.model;

import com.badlogic.gdx.math.GridPoint2;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class Level implements GameField {

    private final List<Tank> tanks;
    private final List<Obstacle> obstacles;

    public Level(Collection<Tank> tanks, Collection<Tree> trees) {
        this.tanks = new ArrayList<>(tanks);
        this.obstacles = new ArrayList<>(trees);
        this.obstacles.addAll(tanks);
    }

    @Override
    public boolean isFree(GridPoint2 coordinates) {
        for (Obstacle obstacle : obstacles) {
            if (obstacle.occupies(coordinates)) {
                return false;
            }
        }
        return true;
    }

    public void update(float deltaTime) {
        for (Tank tank : tanks) {
            tank.update(deltaTime);
        }
    }
}
