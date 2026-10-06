package ru.mipt.bit.platformer.graphics;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.maps.MapRenderer;
import com.badlogic.gdx.maps.tiled.TiledMap;
import ru.mipt.bit.platformer.Renderer;

import java.util.ArrayList;
import java.util.List;

public class LevelGraphics implements Renderer {

    private final TiledMap tiledMap;
    private final MapRenderer tiledMapRenderer;
    private final Batch batch;
    private final List<Renderable> objects = new ArrayList<>();

    public LevelGraphics(TiledMap tiledMap, MapRenderer tiledMapRenderer, Batch batch) {
        this.tiledMap = tiledMap;
        this.tiledMapRenderer = tiledMapRenderer;
        this.batch = batch;
    }

    public void add(Renderable object) {
        objects.add(object);
    }

    @Override
    public void render() {
        tiledMapRenderer.render();

        batch.begin();
        for (Renderable object : objects) {
            object.render(batch);
        }
        batch.end();
    }

    @Override
    public void dispose() {
        for (Renderable object : objects) {
            object.dispose();
        }
        tiledMap.dispose();
    }
}
