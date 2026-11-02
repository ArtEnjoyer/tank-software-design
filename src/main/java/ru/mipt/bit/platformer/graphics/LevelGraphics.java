package ru.mipt.bit.platformer.graphics;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.maps.MapRenderer;
import com.badlogic.gdx.maps.tiled.TiledMap;
import ru.mipt.bit.platformer.Renderer;
import ru.mipt.bit.platformer.model.GameObject;
import ru.mipt.bit.platformer.model.LevelListener;

import java.util.LinkedHashMap;
import java.util.Map;

public class LevelGraphics implements Renderer, LevelListener {

    private final TiledMap tiledMap;
    private final MapRenderer tiledMapRenderer;
    private final Batch batch;
    private final Textures textures;
    private final ObjectGraphicsFactory graphicsFactory;
    private final Map<GameObject, Renderable> graphicsByObject = new LinkedHashMap<>();

    public LevelGraphics(TiledMap tiledMap, MapRenderer tiledMapRenderer, Batch batch,
                         Textures textures, ObjectGraphicsFactory graphicsFactory) {
        this.tiledMap = tiledMap;
        this.tiledMapRenderer = tiledMapRenderer;
        this.batch = batch;
        this.textures = textures;
        this.graphicsFactory = graphicsFactory;
    }

    @Override
    public void onObjectAdded(GameObject object) {
        graphicsByObject.put(object, graphicsFactory.create(object));
    }

    @Override
    public void onObjectRemoved(GameObject object) {
        graphicsByObject.remove(object);
    }

    @Override
    public void render() {
        tiledMapRenderer.render();

        batch.begin();
        for (Renderable graphics : graphicsByObject.values()) {
            graphics.render(batch);
        }
        batch.end();
    }

    @Override
    public void dispose() {
        textures.dispose();
        tiledMap.dispose();
    }
}
