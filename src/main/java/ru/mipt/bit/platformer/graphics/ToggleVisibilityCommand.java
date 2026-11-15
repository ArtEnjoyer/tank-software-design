package ru.mipt.bit.platformer.graphics;

import ru.mipt.bit.platformer.Command;

/**
 * Команда представления в слое инфраструктуры (infrastructure).
 * Реализует общий контракт {@code Command} и переключает видимость элемента.
 * Не является доменным сценарием или отдельным адаптером внешней системы.
 */
public class ToggleVisibilityCommand implements Command {

    private final VisibilityToggle visibility;

    public ToggleVisibilityCommand(VisibilityToggle visibility) {
        this.visibility = visibility;
    }

    @Override
    public void execute() {
        visibility.toggle();
    }
}
