package ru.mipt.bit.platformer.graphics;

import ru.mipt.bit.platformer.model.GameObject;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class ObjectGraphicsFactory {

    private final Map<Class<?>, Function<GameObject, Renderable>> creatorByType = new HashMap<>();

    public <T extends GameObject> void register(Class<T> type, Function<? super T, ? extends Renderable> creator) {
        creatorByType.put(type, object -> creator.apply(type.cast(object)));
    }

    public Renderable create(GameObject object) {
        Function<GameObject, Renderable> creator = creatorByType.get(object.getClass());
        if (creator == null) {
            throw new IllegalArgumentException("No graphics registered for " + object.getClass().getSimpleName());
        }
        return creator.apply(object);
    }
}
