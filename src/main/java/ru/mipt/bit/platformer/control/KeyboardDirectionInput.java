package ru.mipt.bit.platformer.control;

import com.badlogic.gdx.Input;
import ru.mipt.bit.platformer.game.Direction;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

import static com.badlogic.gdx.Input.Keys.A;
import static com.badlogic.gdx.Input.Keys.D;
import static com.badlogic.gdx.Input.Keys.DOWN;
import static com.badlogic.gdx.Input.Keys.LEFT;
import static com.badlogic.gdx.Input.Keys.RIGHT;
import static com.badlogic.gdx.Input.Keys.S;
import static com.badlogic.gdx.Input.Keys.UP;
import static com.badlogic.gdx.Input.Keys.W;

public class KeyboardDirectionInput {

    private static final Map<Integer, Direction> DIRECTION_BY_KEY = directionByKey();

    private final Input input;

    public KeyboardDirectionInput(Input input) {
        this.input = input;
    }

    public Optional<Direction> getPressedDirection() {
        for (Map.Entry<Integer, Direction> binding : DIRECTION_BY_KEY.entrySet()) {
            if (input.isKeyPressed(binding.getKey())) {
                return Optional.of(binding.getValue());
            }
        }
        return Optional.empty();
    }

    private static Map<Integer, Direction> directionByKey() {
        Map<Integer, Direction> bindings = new LinkedHashMap<>();
        bindings.put(UP, Direction.UP);
        bindings.put(W, Direction.UP);
        bindings.put(LEFT, Direction.LEFT);
        bindings.put(A, Direction.LEFT);
        bindings.put(DOWN, Direction.DOWN);
        bindings.put(S, Direction.DOWN);
        bindings.put(RIGHT, Direction.RIGHT);
        bindings.put(D, Direction.RIGHT);
        return Collections.unmodifiableMap(bindings);
    }
}
