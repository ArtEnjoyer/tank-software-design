package ru.mipt.bit.platformer.level;

import com.badlogic.gdx.math.GridPoint2;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public final class LevelLayout {

    private final int width;
    private final int height;
    private final GridPoint2 playerStart;
    private final List<GridPoint2> treeCoordinates = new ArrayList<>();

    public LevelLayout(int width, int height, GridPoint2 playerStart, Collection<GridPoint2> treeCoordinates) {
        if (width <= 0 || height <= 0) {
            throw new IllegalArgumentException("Level size must be positive, got " + width + "x" + height);
        }
        this.width = width;
        this.height = height;
        this.playerStart = inside(playerStart);
        for (GridPoint2 tree : treeCoordinates) {
            if (tree.equals(playerStart)) {
                throw new IllegalArgumentException("Tree cannot stand on player start " + playerStart);
            }
            this.treeCoordinates.add(inside(tree));
        }
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public GridPoint2 getPlayerStart() {
        return new GridPoint2(playerStart);
    }

    public List<GridPoint2> getTreeCoordinates() {
        List<GridPoint2> copies = new ArrayList<>();
        for (GridPoint2 tree : treeCoordinates) {
            copies.add(new GridPoint2(tree));
        }
        return copies;
    }

    private GridPoint2 inside(GridPoint2 coordinates) {
        if (coordinates.x < 0 || coordinates.x >= width || coordinates.y < 0 || coordinates.y >= height) {
            throw new IllegalArgumentException(coordinates + " is outside of level " + width + "x" + height);
        }
        return new GridPoint2(coordinates);
    }
}
