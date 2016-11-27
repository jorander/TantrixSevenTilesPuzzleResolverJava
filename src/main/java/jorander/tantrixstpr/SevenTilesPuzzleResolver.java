package jorander.tantrixstpr;

import static com.github.javactic.Accumulation.combined;
import static com.github.javactic.Accumulation.withGood;
import com.github.javactic.Every;
import com.github.javactic.One;
import com.github.javactic.Or;
import javaslang.Function1;
import static javaslang.Function1.identity;
import static javaslang.API.*;
import javaslang.Function2;
import javaslang.collection.List;
import javaslang.control.Option;
import javaslang.control.Try;
import jorander.tantrixstpr.model.PlacedTantrixTile;
import static jorander.tantrixstpr.model.PlacedTantrixTile.placedTantrixTile;
import jorander.tantrixstpr.model.SevenTilesPuzzle;
import jorander.tantrixstpr.model.SevenTilesPuzzle.TilePosition;
import static jorander.tantrixstpr.model.SevenTilesPuzzle.TilePosition.*;
import jorander.tantrixstpr.model.TantrixTile;

public class SevenTilesPuzzleResolver {

    public static void main(String[] args) {

        final Function1<String[], Or<List<SevenTilesPuzzle>, Every<String>>> resolvingAlgorithm
                = getTiles()
                        .andThen(startPuzzlesWithTryingAllTilesInCenterPosition())
                        .andThen(placeNextTileInPuzzles(TOP_RIGHT_POSITION))
                        .andThen(placeNextTileInPuzzles(RIGHT_POSITION))
                        .andThen(placeNextTileInPuzzles(BOTTOM_RIGHT_POSITION))
                        .andThen(placeNextTileInPuzzles(BOTTOM_LEFT_POSITION))
                        .andThen(placeNextTileInPuzzles(LEFT_POSITION))
                        .andThen(placeNextTileInPuzzles(TOP_LEFT_POSITION));

        printSolutions(resolvingAlgorithm.apply(args));
    }

    // Unit-testable building blocks
    protected static final class Functions {

        public static Or<List<TantrixTile>, Every<String>> getTiles(String[] args) {
            final List<Or<TantrixTile, One<String>>> map = List.of(args).map(Functions::parseInteger).map(t -> t.flatMap(TantrixTile::tile));
            return combined(map, List.collector());
        }

        private static Or<Integer, One<String>> parseInteger(String s) {
            return Or.from(
                    Try.of(() -> Integer.valueOf(s)).getOption(),
                    One.of("\"" + s + "\" is not a valid integer.")
            );
        }

        public static Or<List<SevenTilesPuzzle>, Every<String>> startPuzzlesWithTryingAllTilesInCenterPosition(List<TantrixTile> tiles) {
            return withGood(
                    sevenTilesPuzzle(tiles),
                    placedTantrixTiles(tiles),
                    (newPuzzle, tilesToPlace) -> combined(
                            tilesToPlace.map(tileToPlace -> SevenTilesPuzzle.placeNextTileInPuzzle(newPuzzle, CENTER_POSITION, tileToPlace)),
                            List.collector())
                            .map(results -> results.flatMap(identity())))
                    .flatMap(identity());
        }

        public static Or<List<SevenTilesPuzzle>, Every<String>> placeNextTileInPuzzles(TilePosition nextPosition, List<SevenTilesPuzzle> partialPossibleSolutions) {
            return combined(
                    partialPossibleSolutions
                            .flatMap(puzzle
                                    -> For(puzzle.getUnplacedTiles(), List.rangeClosed(0, 5)).yield((tile, nofRotationSteps) -> placedTantrixTile(tile, nofRotationSteps))
                                    .map(placedTile -> placeNextTileInPuzzle(puzzle, nextPosition, placedTile))
                            ),
                    List.collector())
                    .map(results -> results.flatMap(identity()));
        }
    }

    // Method with side-effect, for printing result
    private static void printSolutions(Or<List<SevenTilesPuzzle>, Every<String>> solvedPuzzlesOr) {
        solvedPuzzlesOr.forEach(
                solvedPuzzles -> solvedPuzzles.zipWithIndex().map(solution -> "Solution (no " + (solution._2 + 1) + "): " + format(solution._1)).forEach(System.out::println),
                failures -> failures.map(failure -> "Error: " + failure).forEach(System.out::println));
    }

    private static String format(SevenTilesPuzzle puzzle) {
        return puzzle.getPlacedTiles().toSortedMap(t -> t)
                .map(t -> "Position: " + t._1 + " Tile no: " + t._2.getTile().getTileNumber() + " Rotation steps: " + t._2.getNbrOfRotationSteps())
                .mkString(", ");
    }

    // Only boiler-plate below
    private static Function1<String[], Or<List<TantrixTile>, Every<String>>> getTiles() {
        return args -> Functions.getTiles(args);
    }

    private static Function1<Or<List<TantrixTile>, Every<String>>, Or<List<SevenTilesPuzzle>, Every<String>>> startPuzzlesWithTryingAllTilesInCenterPosition() {
        return tilesOr -> tilesOr.flatMap(tiles -> Functions.startPuzzlesWithTryingAllTilesInCenterPosition(tiles));
    }

    private static Function1<Or<List<SevenTilesPuzzle>, Every<String>>, Or<List<SevenTilesPuzzle>, Every<String>>> placeNextTileInPuzzles(final TilePosition nextPosition) {
        return puzzelsOr -> puzzelsOr.flatMap(Function2.of(Functions::placeNextTileInPuzzles).apply(nextPosition));
    }

    private static Or<SevenTilesPuzzle, Every<String>> sevenTilesPuzzle(List<TantrixTile> tiles) {
        return SevenTilesPuzzle.sevenTilesPuzzle(tiles.toSet()).badMap(o -> o.map(identity()));
    }

    private static Or<List<PlacedTantrixTile>, Every<String>> placedTantrixTiles(List<TantrixTile> tiles) {
        return combined(tiles.map(tile -> placedTantrixTile(tile, 0)), List.collector());
    }

    private static Or<Option<SevenTilesPuzzle>, Every<String>> placeNextTileInPuzzle(SevenTilesPuzzle puzzle, TilePosition nextPosition, Or<PlacedTantrixTile, Every<String>> placedTileOr) {
        return placedTileOr.flatMap(placedTile -> SevenTilesPuzzle.placeNextTileInPuzzle(puzzle, nextPosition, placedTile));
    }
}
