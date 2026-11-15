package ru.mipt.bit.platformer.input;

import ru.mipt.bit.platformer.Command;
import ru.mipt.bit.platformer.InputController;

import java.util.ArrayList;
import java.util.List;

/**
 * Составная реализация выходного порта {@code InputController}.
 * Объединяет команды нескольких источников в один список, сохраняя их порядок.
 * Инфраструктурная композиция адаптеров ввода; нового внешнего канала не добавляет.
 */
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
