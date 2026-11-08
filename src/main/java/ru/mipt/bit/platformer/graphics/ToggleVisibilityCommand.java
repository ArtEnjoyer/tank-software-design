package ru.mipt.bit.platformer.graphics;

import ru.mipt.bit.platformer.Command;

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
