package ru.mipt.bit.platformer.game;

import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.GridPoint2;
import ru.mipt.bit.platformer.util.TileMovement;

import static com.badlogic.gdx.math.MathUtils.isEqual;
import static ru.mipt.bit.platformer.util.GdxGameUtils.continueProgress;

public class Tank extends GameObject {

    private static final float MOVEMENT_SPEED = 0.4f;

    private final GridPoint2 destinationCoordinates;
    private Direction direction = Direction.RIGHT;
    private float movementProgress = 1f;

    public Tank(String texturePath, GridPoint2 coordinates, TiledMapTileLayer groundLayer) {
        super(texturePath, coordinates, groundLayer);
        this.destinationCoordinates = new GridPoint2(coordinates);
    }

    public void move(Direction toDirection, GameField field) {
        if (isMoving()) {
            return;
        }
        direction = toDirection;
        GridPoint2 destination = toDirection.neighbourOf(getCoordinates());
        if (field.isFree(destination)) {
            destinationCoordinates.set(destination);
            movementProgress = 0f;
        }
    }

    public void update(float deltaTime, TileMovement tileMovement) {
        placeBetweenTiles(tileMovement, destinationCoordinates, movementProgress);
        movementProgress = continueProgress(movementProgress, deltaTime, MOVEMENT_SPEED);
        if (!isMoving()) {
            changeCoordinates(destinationCoordinates);
        }
    }

    @Override
    protected float getRotation() {
        return direction.getRotation();
    }

    private boolean isMoving() {
        return !isEqual(movementProgress, 1f);
    }
}
