package ru.mipt.bit.platformer.ai;

import ru.mipt.bit.platformer.Command;
import ru.mipt.bit.platformer.InputController;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;

public class RandomAiController implements InputController {

    private final List<Command> commands;
    private final Random random;

    public RandomAiController(Collection<? extends Command> commands, Random random) {
        if (commands.isEmpty()) {
            throw new IllegalArgumentException("AI needs at least one command to choose from");
        }
        this.commands = new ArrayList<>(commands);
        this.random = random;
    }

    @Override
    public void handleInput() {
        commands.get(random.nextInt(commands.size())).execute();
    }
}
