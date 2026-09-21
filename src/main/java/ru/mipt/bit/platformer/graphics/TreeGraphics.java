package ru.mipt.bit.platformer.graphics;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import ru.mipt.bit.platformer.model.Tree;

public class TreeGraphics extends GameObjectGraphics {

    private static final float NO_ROTATION = 0f;

    public TreeGraphics(Tree tree, String texturePath, TiledMapTileLayer groundLayer) {
        super(texturePath, tree.getCoordinates(), groundLayer);
    }

    @Override
    public void render(Batch batch) {
        draw(batch, NO_ROTATION);
    }
}
