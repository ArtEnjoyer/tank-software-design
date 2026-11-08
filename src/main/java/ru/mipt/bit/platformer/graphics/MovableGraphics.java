package ru.mipt.bit.platformer.graphics;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import ru.mipt.bit.platformer.model.MovingObject;
import ru.mipt.bit.platformer.util.TileMovement;

public class MovableGraphics extends GameObjectGraphics {

    private final MovingObject object;
    private final TileMovement tileMovement;

    public MovableGraphics(MovingObject object, Texture texture, TiledMapTileLayer groundLayer, TileMovement tileMovement) {
        super(texture, object.getCoordinates(), groundLayer);
        this.object = object;
        this.tileMovement = tileMovement;
    }

    @Override
    public void render(Batch batch) {
        placeBetweenTiles(tileMovement, object.getCoordinates(), object.getDestinationCoordinates(), object.getMovementProgress());
        draw(batch, object.getDirection().getRotation());
    }
}
