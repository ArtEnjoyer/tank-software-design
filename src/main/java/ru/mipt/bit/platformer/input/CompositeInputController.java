package ru.mipt.bit.platformer.input;

import ru.mipt.bit.platformer.InputController;

import java.util.ArrayList;
import java.util.List;

public class CompositeInputController implements InputController {

    private final List<InputController> controllers = new ArrayList<>();

    public void add(InputController controller) {
        controllers.add(controller);
    }

    @Override
    public void handleInput() {
        for (InputController controller : new ArrayList<>(controllers)) {
            controller.handleInput();
        }
    }
}
