package ru.mipt.bit.platformer.control;

import ru.mipt.bit.platformer.model.Direction;
import ru.mipt.bit.platformer.model.GameField;
import ru.mipt.bit.platformer.model.Tank;

public class MoveCommand implements Command {

    private final Tank tank;
    private final Direction direction;
    private final GameField field;

    public MoveCommand(Tank tank, Direction direction, GameField field) {
        this.tank = tank;
        this.direction = direction;
        this.field = field;
    }

    @Override
    public void execute() {
        tank.move(direction, field);
    }
}
