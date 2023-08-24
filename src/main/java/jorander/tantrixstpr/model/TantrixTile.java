package jorander.tantrixstpr.model;

import com.github.javactic.Bad;
import com.github.javactic.Good;
import com.github.javactic.One;
import com.github.javactic.Or;
import io.vavr.collection.HashMap;
import io.vavr.collection.List;
import io.vavr.collection.Map;
import lombok.AllArgsConstructor;
import lombok.Value;

import static io.vavr.Function1.identity;
import static jorander.tantrixstpr.model.TantrixTile.BandColor.*;
import static jorander.tantrixstpr.model.TantrixTile.TileEdge.*;
import static lombok.AccessLevel.PRIVATE;

@Value
@AllArgsConstructor(access = PRIVATE)
public class TantrixTile {

    public enum BandColor {
        RED, GREEN, BLUE, YELLOW
    }

    public enum TileEdge {
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
                    tantrixTile(2, RED, BLUE, YELLOW, YELLOW, BLUE, RED),
                    tantrixTile(3, YELLOW, YELLOW, RED, RED, BLUE, BLUE),
                    tantrixTile(4, YELLOW, BLUE, YELLOW, RED, BLUE, RED),
                    tantrixTile(5, YELLOW, RED, BLUE, BLUE, RED, YELLOW),
                    tantrixTile(6, RED, YELLOW, RED, BLUE, YELLOW, BLUE),
                    tantrixTile(7, YELLOW, RED, BLUE, BLUE, YELLOW, RED),
                    tantrixTile(8, RED, YELLOW, BLUE, BLUE, RED, YELLOW),
                    tantrixTile(9, YELLOW, RED, YELLOW, BLUE, RED, BLUE),
                    tantrixTile(10, YELLOW, RED, BLUE, RED, BLUE, YELLOW),
                    tantrixTile(11, RED, BLUE, YELLOW, BLUE, YELLOW, RED),
                    tantrixTile(12, RED, YELLOW, BLUE, YELLOW, BLUE, RED),
                    tantrixTile(13, BLUE, YELLOW, RED, RED, YELLOW, BLUE),
                    tantrixTile(14, BLUE, RED, RED, YELLOW, YELLOW, BLUE),
                    tantrixTile(15, GREEN, RED, YELLOW, YELLOW, RED, GREEN),
                    tantrixTile(16, RED, YELLOW, GREEN, GREEN, YELLOW, RED),
                    tantrixTile(17, YELLOW, GREEN, RED, GREEN, RED, YELLOW),
                    tantrixTile(18, YELLOW, RED, GREEN, RED, GREEN, YELLOW),
                    tantrixTile(19, RED, GREEN, YELLOW, GREEN, YELLOW, RED),
                    tantrixTile(20, RED, YELLOW, GREEN, YELLOW, GREEN, RED),
                    tantrixTile(21, GREEN, RED, RED, YELLOW, YELLOW, GREEN),
                    tantrixTile(22, YELLOW, GREEN, RED, RED, GREEN, YELLOW),
                    tantrixTile(23, GREEN, YELLOW, YELLOW, RED, RED, GREEN),
                    tantrixTile(24, GREEN, BLUE, RED, RED, BLUE, GREEN),
                    tantrixTile(25, BLUE, GREEN, GREEN, RED, RED, BLUE),
                    tantrixTile(26, BLUE, GREEN, RED, RED, GREEN, BLUE),
                    tantrixTile(27, RED, BLUE, GREEN, BLUE, GREEN, RED),
                    tantrixTile(28, GREEN, BLUE, BLUE, RED, RED, GREEN),
                    tantrixTile(29, RED, GREEN, BLUE, GREEN, BLUE, RED),
                    tantrixTile(30, GREEN, RED, BLUE, BLUE, RED, GREEN),
                    tantrixTile(31, GREEN, RED, YELLOW, RED, YELLOW, GREEN),
                    tantrixTile(32, YELLOW, GREEN, YELLOW, RED, GREEN, RED),
                    tantrixTile(33, GREEN, YELLOW, RED, YELLOW, RED, GREEN),
                    tantrixTile(34, YELLOW, RED, YELLOW, GREEN, RED, GREEN),
                    tantrixTile(35, RED, YELLOW, RED, GREEN, YELLOW, GREEN),
                    tantrixTile(36, GREEN, RED, BLUE, RED, BLUE, GREEN),
                    tantrixTile(37, BLUE, GREEN, RED, GREEN, RED, BLUE),
                    tantrixTile(38, BLUE, RED, GREEN, RED, GREEN, BLUE),
                    tantrixTile(39, BLUE, GREEN, BLUE, RED, GREEN, RED),
                    tantrixTile(40, GREEN, BLUE, GREEN, RED, BLUE, RED),
                    tantrixTile(41, GREEN, BLUE, RED, BLUE, RED, GREEN),
                    tantrixTile(42, GREEN, RED, GREEN, BLUE, RED, BLUE),
                    tantrixTile(43, GREEN, BLUE, BLUE, YELLOW, YELLOW, GREEN),
                    tantrixTile(44, GREEN, YELLOW, GREEN, BLUE, YELLOW, BLUE),
                    tantrixTile(45, YELLOW, BLUE, BLUE, GREEN, GREEN, YELLOW),
                    tantrixTile(46, GREEN, BLUE, YELLOW, BLUE, YELLOW, GREEN),
                    tantrixTile(47, GREEN, YELLOW, BLUE, BLUE, YELLOW, GREEN),
                    tantrixTile(48, YELLOW, BLUE, GREEN, GREEN, BLUE, YELLOW),
                    tantrixTile(49, YELLOW, GREEN, BLUE, BLUE, GREEN, YELLOW),
                    tantrixTile(50, GREEN, BLUE, GREEN, YELLOW, BLUE, YELLOW),
                    tantrixTile(51, YELLOW, GREEN, YELLOW, BLUE, GREEN, BLUE),
                    tantrixTile(52, GREEN, YELLOW, BLUE, YELLOW, BLUE, GREEN),
                    tantrixTile(53, YELLOW, GREEN, BLUE, GREEN, BLUE, YELLOW),
                    tantrixTile(54, YELLOW, BLUE, GREEN, BLUE, GREEN, YELLOW),
                    tantrixTile(55, BLUE, GREEN, YELLOW, GREEN, YELLOW, BLUE),
                    tantrixTile(56, BLUE, YELLOW, GREEN, YELLOW, GREEN, BLUE))
            .map(Or::get)
            .toMap(t -> t.tileNumber, identity());

    public static Or<TantrixTile, One<String>> tile(int number) {
        return Or.from(TILES.get(number), One.of("Tile number " + number + " does not exist."));
    }
}
