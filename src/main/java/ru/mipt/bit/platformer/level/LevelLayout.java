package ru.mipt.bit.platformer.level;

import com.badlogic.gdx.math.GridPoint2;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public final class LevelLayout {

    private final int width;
    private final int height;
    private final Set<GridPoint2> occupiedCells = new HashSet<>();
    private final GridPoint2 playerStart;
    private final List<GridPoint2> treeCoordinates;
    private final List<GridPoint2> enemyTankCoordinates;

    public LevelLayout(int width, int height, GridPoint2 playerStart, Collection<GridPoint2> treeCoordinates) {
        this(width, height, playerStart, treeCoordinates, Collections.<GridPoint2>emptyList());
    }

    public LevelLayout(int width, int height, GridPoint2 playerStart,
                       Collection<GridPoint2> treeCoordinates, Collection<GridPoint2> enemyTankCoordinates) {
        if (width <= 0 || height <= 0) {
            throw new IllegalArgumentException("Level size must be positive, got " + width + "x" + height);
        }
        this.width = width;
        this.height = height;
        this.playerStart = place(playerStart);
        this.treeCoordinates = placeAll(treeCoordinates);
        this.enemyTankCoordinates = placeAll(enemyTankCoordinates);
    }

    public LevelLayout withEnemyTanks(Collection<GridPoint2> tankCoordinates) {
        List<GridPoint2> allTanks = new ArrayList<>(enemyTankCoordinates);
        allTanks.addAll(tankCoordinates);
        return new LevelLayout(width, height, playerStart, treeCoordinates, allTanks);
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
        return copies(treeCoordinates);
    }

    public List<GridPoint2> getEnemyTankCoordinates() {
        return copies(enemyTankCoordinates);
    }

    public List<GridPoint2> getFreeCells() {
        List<GridPoint2> freeCells = new ArrayList<>();
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                GridPoint2 cell = new GridPoint2(x, y);
                if (!occupiedCells.contains(cell)) {
                    freeCells.add(cell);
                }
            }
        }
        return freeCells;
    }

    private List<GridPoint2> placeAll(Collection<GridPoint2> coordinates) {
        List<GridPoint2> placed = new ArrayList<>();
        for (GridPoint2 cell : coordinates) {
            placed.add(place(cell));
        }
        return placed;
    }

    private GridPoint2 place(GridPoint2 coordinates) {
        if (coordinates.x < 0 || coordinates.x >= width || coordinates.y < 0 || coordinates.y >= height) {
            throw new IllegalArgumentException(coordinates + " is outside of level " + width + "x" + height);
        }
        GridPoint2 cell = new GridPoint2(coordinates);
        if (!occupiedCells.add(cell)) {
            throw new IllegalArgumentException("Cell " + coordinates + " is taken by two objects");
        }
        return cell;
    }

    private List<GridPoint2> copies(List<GridPoint2> coordinates) {
        List<GridPoint2> result = new ArrayList<>();
        for (GridPoint2 cell : coordinates) {
            result.add(new GridPoint2(cell));
        }
        return result;
    }
}
