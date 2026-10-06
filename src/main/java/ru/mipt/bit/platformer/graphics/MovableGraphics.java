package ru.mipt.bit.platformer.graphics;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import ru.mipt.bit.platformer.model.Movable;
import ru.mipt.bit.platformer.util.TileMovement;

public class MovableGraphics extends GameObjectGraphics {

    private final Movable movable;
    private final TileMovement tileMovement;

    public MovableGraphics(Movable movable, String texturePath, TiledMapTileLayer groundLayer, TileMovement tileMovement) {
        super(texturePath, movable.getCoordinates(), groundLayer);
        this.movable = movable;
        this.tileMovement = tileMovement;
    }

    @Override
    public void render(Batch batch) {
        placeBetweenTiles(tileMovement, movable.getCoordinates(), movable.getDestinationCoordinates(), movable.getMovementProgress());
        draw(batch, movable.getDirection().getRotation());
    }
}
