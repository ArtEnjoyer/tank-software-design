package ru.mipt.bit.platformer.input;

import ru.mipt.bit.platformer.Command;
import ru.mipt.bit.platformer.InputController;

import java.util.ArrayList;
import java.util.List;

public class CompositeInputController implements InputController {

    private final List<InputController> controllers = new ArrayList<>();

    public void add(InputController controller) {
        controllers.add(controller);
    }

    @Override
    public List<Command> createCommands() {
        List<Command> commands = new ArrayList<>();
        for (InputController controller : new ArrayList<>(controllers)) {
            commands.addAll(controller.createCommands());
        }
        return commands;
    }
}
