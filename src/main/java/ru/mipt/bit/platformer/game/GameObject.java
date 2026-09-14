package ru.mipt.bit.platformer.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Disposable;
import ru.mipt.bit.platformer.util.TileMovement;

import static ru.mipt.bit.platformer.util.GdxGameUtils.createBoundingRectangle;
import static ru.mipt.bit.platformer.util.GdxGameUtils.drawTextureRegionUnscaled;
import static ru.mipt.bit.platformer.util.GdxGameUtils.moveRectangleAtTileCenter;

public abstract class GameObject implements Disposable {

    private final Texture texture;
    private final TextureRegion graphics;
    private final Rectangle rectangle;
    private final GridPoint2 coordinates;

    protected GameObject(String texturePath, GridPoint2 coordinates, TiledMapTileLayer groundLayer) {
        this.texture = new Texture(texturePath);
        this.graphics = new TextureRegion(texture);
        this.rectangle = createBoundingRectangle(graphics);
        this.coordinates = new GridPoint2(coordinates);
        moveRectangleAtTileCenter(groundLayer, rectangle, this.coordinates);
    }

    public boolean occupies(GridPoint2 tileCoordinates) {
        return coordinates.equals(tileCoordinates);
    }

    public void render(Batch batch) {
        drawTextureRegionUnscaled(batch, graphics, rectangle, getRotation());
    }

    @Override
    public void dispose() {
        texture.dispose();
    }

    protected float getRotation() {
        return 0f;
    }

    protected GridPoint2 getCoordinates() {
        return new GridPoint2(coordinates);
    }

    protected final void placeBetweenTiles(TileMovement tileMovement, GridPoint2 destination, float progress) {
        tileMovement.moveRectangleBetweenTileCenters(rectangle, coordinates, destination, progress);
    }

    protected final void changeCoordinates(GridPoint2 newCoordinates) {
        coordinates.set(newCoordinates);
    }
}
