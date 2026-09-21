package ru.mipt.bit.platformer.graphics;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import ru.mipt.bit.platformer.model.Tank;
import ru.mipt.bit.platformer.util.TileMovement;

public class TankGraphics extends GameObjectGraphics {

    private final Tank tank;
    private final TileMovement tileMovement;

    public TankGraphics(Tank tank, String texturePath, TiledMapTileLayer groundLayer, TileMovement tileMovement) {
        super(texturePath, tank.getCoordinates(), groundLayer);
        this.tank = tank;
        this.tileMovement = tileMovement;
    }

    @Override
    public void render(Batch batch) {
        placeBetweenTiles(tileMovement, tank.getCoordinates(), tank.getDestinationCoordinates(), tank.getMovementProgress());
        draw(batch, tank.getDirection().getRotation());
    }
}
