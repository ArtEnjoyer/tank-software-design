package ru.mipt.bit.platformer.graphics;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Rectangle;
import ru.mipt.bit.platformer.util.TileMovement;

import static ru.mipt.bit.platformer.util.GdxGameUtils.createBoundingRectangle;
import static ru.mipt.bit.platformer.util.GdxGameUtils.drawTextureRegionUnscaled;
import static ru.mipt.bit.platformer.util.GdxGameUtils.moveRectangleAtTileCenter;

public abstract class GameObjectGraphics implements Renderable {

    private final TextureRegion region;
    private final Rectangle rectangle;

    protected GameObjectGraphics(Texture texture, GridPoint2 coordinates, TiledMapTileLayer groundLayer) {
        this.region = new TextureRegion(texture);
        this.rectangle = createBoundingRectangle(region);
        moveRectangleAtTileCenter(groundLayer, rectangle, coordinates);
    }

    protected final void draw(Batch batch, float rotation) {
        drawTextureRegionUnscaled(batch, region, rectangle, rotation);
    }

    protected final void placeBetweenTiles(TileMovement tileMovement, GridPoint2 from, GridPoint2 to, float progress) {
        tileMovement.moveRectangleBetweenTileCenters(rectangle, from, to, progress);
    }
}
