package ru.mipt.bit.platformer.level;

import com.badlogic.gdx.math.GridPoint2;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class LevelParserTest {

    private final LevelParser parser = new LevelParser();

    @Test
    void parsesTheExampleFromTheAssignment() {
        List<String> lines = asList(
                "___T__T___",
                "__TT__TTTT",
                "_________T",
                "TTTT__T__T",
                "_____X____",
                "__________");

        LevelLayout layout = parser.parse(lines);

        assertEquals(10, layout.getWidth());
        assertEquals(6, layout.getHeight());
        assertEquals(new GridPoint2(5, 1), layout.getPlayerStart());
        assertEquals(asList(
                new GridPoint2(3, 5), new GridPoint2(6, 5),
                new GridPoint2(2, 4), new GridPoint2(3, 4),
                new GridPoint2(6, 4), new GridPoint2(7, 4), new GridPoint2(8, 4), new GridPoint2(9, 4),
                new GridPoint2(9, 3),
                new GridPoint2(0, 2), new GridPoint2(1, 2), new GridPoint2(2, 2), new GridPoint2(3, 2),
                new GridPoint2(6, 2), new GridPoint2(9, 2)
        ), layout.getTreeCoordinates());
    }

    @Test
    void treatsTheFirstLineAsTheTopRow() {
        LevelLayout layout = parser.parse(asList("X_", "__"));

        assertEquals(new GridPoint2(0, 1), layout.getPlayerStart());
    }

    @Test
    void ignoresBlankLines() {
        LevelLayout layout = parser.parse(asList("", "X", "", "T", " "));

        assertEquals(1, layout.getWidth());
        assertEquals(2, layout.getHeight());
        assertEquals(new GridPoint2(0, 1), layout.getPlayerStart());
        assertEquals(singletonList(new GridPoint2(0, 0)), layout.getTreeCoordinates());
    }

    @Test
    void rejectsEmptyInput() {
        assertThrows(IllegalArgumentException.class, () -> parser.parse(emptyList()));
        assertThrows(IllegalArgumentException.class, () -> parser.parse(singletonList("   ")));
    }

    @Test
    void rejectsRowsOfDifferentWidth() {
        assertThrows(IllegalArgumentException.class, () -> parser.parse(asList("X__", "_")));
    }

    @Test
    void rejectsUnknownSymbols() {
        assertThrows(IllegalArgumentException.class, () -> parser.parse(singletonList("X#")));
    }

    @Test
    void rejectsMissingOrExtraPlayer() {
        assertThrows(IllegalArgumentException.class, () -> parser.parse(singletonList("T_")));
        assertThrows(IllegalArgumentException.class, () -> parser.parse(singletonList("XX")));
    }

    @Test
    void acceptsLevelWithoutTrees() {
        LevelLayout layout = parser.parse(Arrays.asList("___", "_X_"));

        assertEquals(new GridPoint2(1, 0), layout.getPlayerStart());
        assertTrue(layout.getTreeCoordinates().isEmpty());
    }
}
