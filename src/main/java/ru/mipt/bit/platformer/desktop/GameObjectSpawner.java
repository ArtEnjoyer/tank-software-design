package ru.mipt.bit.platformer.desktop;

import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.GridPoint2;
import ru.mipt.bit.platformer.graphics.LevelGraphics;
import ru.mipt.bit.platformer.graphics.MovableGraphics;
import ru.mipt.bit.platformer.graphics.StaticObjectGraphics;
import ru.mipt.bit.platformer.graphics.Textures;
import ru.mipt.bit.platformer.model.Direction;
import ru.mipt.bit.platformer.model.Level;
import ru.mipt.bit.platformer.model.Tank;
import ru.mipt.bit.platformer.model.Tree;
import ru.mipt.bit.platformer.util.TileMovement;

public class GameObjectSpawner {

    private static final String TANK_TEXTURE = "images/tank_blue.png";
    private static final String TREE_TEXTURE = "images/greenTree.png";

    private final Level level;
    private final LevelGraphics levelGraphics;
    private final Textures textures;
    private final TiledMapTileLayer groundLayer;
    private final TileMovement tileMovement;

    public GameObjectSpawner(Level level, LevelGraphics levelGraphics, Textures textures,
                             TiledMapTileLayer groundLayer, TileMovement tileMovement) {
        this.level = level;
        this.levelGraphics = levelGraphics;
        this.textures = textures;
        this.groundLayer = groundLayer;
        this.tileMovement = tileMovement;
    }

    public Tank spawnTank(GridPoint2 coordinates, Direction direction, float secondsPerTile) {
        Tank tank = new Tank(coordinates, direction, secondsPerTile);
        level.add(tank);
        levelGraphics.add(new MovableGraphics(tank, textures.get(TANK_TEXTURE), groundLayer, tileMovement));
        return tank;
    }

    public Tree spawnTree(GridPoint2 coordinates) {
        Tree tree = new Tree(coordinates);
        level.add(tree);
        levelGraphics.add(new StaticObjectGraphics(tree, textures.get(TREE_TEXTURE), groundLayer));
        return tree;
    }
}
