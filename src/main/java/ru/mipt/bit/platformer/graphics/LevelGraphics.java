package ru.mipt.bit.platformer.graphics;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.maps.MapRenderer;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.utils.Disposable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class LevelGraphics implements Disposable {

    private final TiledMap tiledMap;
    private final MapRenderer tiledMapRenderer;
    private final Batch batch;
    private final List<Renderable> objects;

    public LevelGraphics(TiledMap tiledMap, MapRenderer tiledMapRenderer, Batch batch, Collection<Renderable> objects) {
        this.tiledMap = tiledMap;
        this.tiledMapRenderer = tiledMapRenderer;
        this.batch = batch;
        this.objects = new ArrayList<>(objects);
    }

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
