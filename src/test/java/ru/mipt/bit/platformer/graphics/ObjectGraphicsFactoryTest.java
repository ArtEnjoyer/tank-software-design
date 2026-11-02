package ru.mipt.bit.platformer.graphics;

import com.badlogic.gdx.math.GridPoint2;
import org.junit.jupiter.api.Test;
import ru.mipt.bit.platformer.model.Direction;
import ru.mipt.bit.platformer.model.Tank;
import ru.mipt.bit.platformer.model.Tree;

import java.util.ArrayList;
import java.util.List;

import static java.util.Collections.singletonList;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static ru.mipt.bit.platformer.model.TestTanks.tankAt;

class ObjectGraphicsFactoryTest {

    private final ObjectGraphicsFactory factory = new ObjectGraphicsFactory();

    @Test
    void createsGraphicsRegisteredForTheObjectType() {
        Renderable treeGraphics = batch -> { };
        Renderable tankGraphics = batch -> { };
        factory.register(Tree.class, tree -> treeGraphics);
        factory.register(Tank.class, tank -> tankGraphics);

        assertSame(treeGraphics, factory.create(new Tree(new GridPoint2(0, 0))));
        assertSame(tankGraphics, factory.create(tankAt(1, 1)));
    }

    @Test
    void passesTheObjectItselfToTheCreator() {
        List<Tank> drawnTanks = new ArrayList<>();
        factory.register(Tank.class, tank -> {
            drawnTanks.add(tank);
            return batch -> { };
        });
        Tank tank = tankAt(3, 2, Direction.UP);

        factory.create(tank);

        assertEquals(singletonList(tank), drawnTanks);
    }

    @Test
    void laterRegistrationReplacesTheEarlierOne() {
        Renderable newer = batch -> { };
        factory.register(Tree.class, tree -> batch -> { });
        factory.register(Tree.class, tree -> newer);

        assertSame(newer, factory.create(new Tree(new GridPoint2(0, 0))));
    }

    @Test
    void refusesObjectsWithoutRegisteredGraphics() {
        factory.register(Tree.class, tree -> batch -> { });

        assertThrows(IllegalArgumentException.class, () -> factory.create(tankAt(1, 1)));
    }
}
