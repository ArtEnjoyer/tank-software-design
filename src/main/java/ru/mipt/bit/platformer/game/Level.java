package ru.mipt.bit.platformer.game;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.maps.MapRenderer;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.utils.Disposable;
import ru.mipt.bit.platformer.util.TileMovement;

import java.util.ArrayList;
import java.util.Collection;

import static ru.mipt.bit.platformer.util.GdxGameUtils.createSingleLayerMapRenderer;
import static ru.mipt.bit.platformer.util.GdxGameUtils.getSingleLayer;

public class Level implements GameField, Disposable {

    private static final String LEVEL_MAP = "level.tmx";
    private static final String TANK_TEXTURE = "images/tank_blue.png";
    private static final String TREE_TEXTURE = "images/greenTree.png";

    private final TiledMap tiledMap;
    private final MapRenderer tiledMapRenderer;
    private final TileMovement tileMovement;

    private final Collection<GameObject> obstacles = new ArrayList<>();
    private final Tank player;

    public Level(Batch batch) {
        tiledMap = new TmxMapLoader().load(LEVEL_MAP);
        tiledMapRenderer = createSingleLayerMapRenderer(tiledMap, batch);
        TiledMapTileLayer groundLayer = getSingleLayer(tiledMap);
        tileMovement = new TileMovement(groundLayer, Interpolation.smooth);

        obstacles.add(new Tree(TREE_TEXTURE, new GridPoint2(1, 3), groundLayer));
        player = new Tank(TANK_TEXTURE, new GridPoint2(1, 1), groundLayer);
    }

    @Override
    public boolean isFree(GridPoint2 coordinates) {
        for (GameObject obstacle : obstacles) {
            if (obstacle.occupies(coordinates)) {
                return false;
            }
        }
        return true;
    }

    public void movePlayer(Direction direction) {
        player.move(direction, this);
    }

    public void update(float deltaTime) {
        player.update(deltaTime, tileMovement);
    }

    public void render(Batch batch) {
        tiledMapRenderer.render();

        batch.begin();
        player.render(batch);
        for (GameObject obstacle : obstacles) {
            obstacle.render(batch);
        }
        batch.end();
    }

    @Override
    public void dispose() {
        for (GameObject obstacle : obstacles) {
            obstacle.dispose();
        }
        player.dispose();
        tiledMap.dispose();
    }
}
