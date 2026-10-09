package ru.mipt.bit.platformer.graphics;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.Disposable;

import java.util.HashMap;
import java.util.Map;

public class Textures implements Disposable {

    private final Map<String, Texture> textureByPath = new HashMap<>();

    public Texture get(String texturePath) {
        Texture texture = textureByPath.get(texturePath);
        if (texture == null) {
            texture = new Texture(texturePath);
            textureByPath.put(texturePath, texture);
        }
        return texture;
    }

    @Override
    public void dispose() {
        for (Texture texture : textureByPath.values()) {
            texture.dispose();
        }
        textureByPath.clear();
    }
}
