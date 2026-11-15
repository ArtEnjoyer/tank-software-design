package ru.mipt.bit.platformer.model;

import ru.mipt.bit.platformer.Command;

/**
 * Сценарий слоя приложения (application), физически расположенный в model.
 * Реализует {@code Command} и передаёт запрос движения доменному {@link Movable}.
 * Правила доступности клетки остаются в domain; команда не является адаптером ввода.
 */
public class MoveCommand implements Command {

    private final Movable movable;
    private final Direction direction;
    private final GameField field;

    public MoveCommand(Movable movable, Direction direction, GameField field) {
        this.movable = movable;
        this.direction = direction;
        this.field = field;
    }

    @Override
    public void execute() {
        movable.move(direction, field);
    }
}
