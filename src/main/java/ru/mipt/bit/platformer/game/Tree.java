package ru.mipt.bit.platformer.game;

import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.GridPoint2;

public class Tree extends GameObject {

    public Tree(String texturePath, GridPoint2 coordinates, TiledMapTileLayer groundLayer) {
        super(texturePath, coordinates, groundLayer);
    }
}
