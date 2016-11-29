package jorander.tantrixstpr.model;

import com.github.javactic.Bad;
import com.github.javactic.Every;
import com.github.javactic.Good;
import com.github.javactic.One;
import com.github.javactic.Or;
import static javaslang.Function1.identity;
import javaslang.collection.HashMap;
import javaslang.collection.List;
import javaslang.collection.Map;
import static jorander.tantrixstpr.model.TantrixTile.BandColor.*;
import static jorander.tantrixstpr.model.TantrixTile.TileEdge.*;
import static lombok.AccessLevel.PRIVATE;
import lombok.AllArgsConstructor;
import lombok.Value;

@Value
@AllArgsConstructor(access = PRIVATE)
public final class TantrixTile {

    public static enum BandColor {
        RED, GREEN, BLUE, YELLOW;
    }

    public static enum TileEdge {
        // Ordered
        TOP_RIGHT_EDGE,
        RIGHT_EDGE,
        BOTTOM_RIGHT_EDGE,
        BOTTOM_LEFT_EDGE,
        LEFT_EDGE,
        TOP_LEFT_EDGE
    }

    int tileNumber;
    Map<TileEdge, BandColor> edgeColors;

    public BandColor edgeColor(TileEdge edge) {
        return edgeColors.get(edge).getOrElseThrow(() -> new IllegalStateException("This should never happen. Missing BandColor for " + edge));
    }

    public static Or<TantrixTile, One<String>> tantrixTile(
            int tileNumber,
            BandColor topRightEdgeColor,
            BandColor rightEdgeColor,
            BandColor bottomRightEdgeColor,
            BandColor bottomLeftEdgeColor,
            BandColor leftEdgeColor,
            BandColor topLeftEdgeColor) {
        return validateHasThreeColorsSpecifiedForTwoEdgesEach(
                HashMap.of(
                        TOP_RIGHT_EDGE, topRightEdgeColor,
                        RIGHT_EDGE, rightEdgeColor,
                        BOTTOM_RIGHT_EDGE, bottomRightEdgeColor,
                        BOTTOM_LEFT_EDGE, bottomLeftEdgeColor,
                        LEFT_EDGE, leftEdgeColor,
                        TOP_LEFT_EDGE, topLeftEdgeColor)
        ).map(edgeColors -> new TantrixTile(tileNumber, edgeColors));
    }

    private static Or<Map<TileEdge, BandColor>, One<String>> validateHasThreeColorsSpecifiedForTwoEdgesEach(Map<TileEdge, BandColor> edgeColors) {
        return List.of(RED, GREEN, BLUE, YELLOW).map(c -> edgeColors.values().filter(c::equals).length()).filter(l -> l == 2).length() == 3
                ? Good.of(edgeColors)
                : Bad.of(One.of("Should have three colors specified for two edges each."));
    }

    private static final Map<Integer, TantrixTile> TILES = List.of(
            tantrixTile(1, BLUE, RED, YELLOW, YELLOW, BLUE, RED),
            tantrixTile(7, YELLOW, RED, BLUE, BLUE, YELLOW, RED),
            tantrixTile(8, RED, YELLOW, BLUE, BLUE, RED, YELLOW),
            tantrixTile(9, YELLOW, RED, YELLOW, BLUE, RED, BLUE),
            tantrixTile(11, RED, BLUE, YELLOW, BLUE, YELLOW, RED),
            tantrixTile(13, BLUE, YELLOW, RED, RED, YELLOW, BLUE),
            tantrixTile(17, YELLOW, GREEN, RED, GREEN, RED, YELLOW),
            tantrixTile(18, YELLOW, RED, GREEN, RED, GREEN, YELLOW),
            tantrixTile(22, YELLOW, GREEN, RED, RED, GREEN, YELLOW),
            tantrixTile(23, GREEN, YELLOW, YELLOW, RED, RED, GREEN),
            tantrixTile(32, YELLOW, GREEN, YELLOW, RED, GREEN, RED),
            tantrixTile(34, YELLOW, RED, YELLOW, GREEN, RED, GREEN),
            tantrixTile(38, BLUE, RED, GREEN, RED, GREEN, BLUE),
            tantrixTile(40, GREEN, BLUE, GREEN, RED, BLUE, RED),
            tantrixTile(44, GREEN, YELLOW, GREEN, BLUE, YELLOW, BLUE),
            tantrixTile(55, BLUE, GREEN, YELLOW, GREEN, YELLOW, BLUE),
            tantrixTile(56, BLUE, YELLOW, GREEN, YELLOW, GREEN, BLUE))
            .map(Or::get)
            .toMap(t -> t.tileNumber, identity());

    public static Or<TantrixTile, One<String>> tile(int number) {
        return Or.from(TILES.get(number), One.of("Tile number " + number + " does not exist."));
    }
}
