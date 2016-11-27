package jorander.tantrixstpr.model;

import com.github.javactic.Accumulation;
import com.github.javactic.Every;
import com.github.javactic.Or;
import static javaslang.Function1.identity;
import static jorander.tantrixstpr.model.PlacedTantrixTile.placedTantrixTile;
import javaslang.collection.HashMap;
import javaslang.collection.HashSet;
import javaslang.control.Option;
import static jorander.tantrixstpr.model.SevenTilesPuzzle.TilePosition.*;
import static jorander.tantrixstpr.model.SevenTilesPuzzle.isNewTilePlacementValid;
import static jorander.tantrixstpr.model.SevenTilesPuzzle.placeNextTileInPuzzle;
import static jorander.tantrixstpr.model.SevenTilesPuzzle.sevenTilesPuzzle;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

public class SevenTilesPuzzleTest {

    @Test
    public void shouldConstructWithSevenTiles() {
        assertTrue(Accumulation.withGood(tile(1), tile(7), tile(8), tile(9), tile(11), tile(13), tile(17), (a, b, c, d, e, f, g) -> HashSet.of(a, b, c, d, e, f, g)).flatMap(SevenTilesPuzzle::sevenTilesPuzzle).isGood());
        assertTrue(sevenTilesPuzzle(null).isBad());
        assertTrue(Accumulation.withGood(tile(1), tile(7), (a, b) -> HashSet.of(a, b)).flatMap(SevenTilesPuzzle::sevenTilesPuzzle).isBad());
    }

    @Test
    public void shouldAlwaysAcceptTheFirstTileInTheCenterPosition() {
        assertTrue(tile(1)
                .flatMap(tile -> placedTantrixTile(tile, 0))
                .flatMap(placedTile -> isNewTilePlacementValid(HashMap.empty(), CENTER_POSITION, placedTile))
                .get());
    }

    @Test
    public void shouldAcceptTheTileInTheTopRightPositionIfItMatchesTheTileInTheCenterPosition() {
        assertTrue(tile(8)
                .flatMap(tile -> placedTantrixTile(tile, 0))
                .flatMap(placedTile
                        -> isNewTilePlacementValid(
                        HashMap.of(CENTER_POSITION, tile(1).flatMap(tile -> placedTantrixTile(tile, 0)).get()),
                        TOP_RIGHT_POSITION, placedTile))
                .get());

        assertFalse(tile(8)
                .flatMap(tile -> placedTantrixTile(tile, 0))
                .flatMap(placedTile
                        -> isNewTilePlacementValid(
                        HashMap.of(CENTER_POSITION, tile(1).flatMap(tile -> placedTantrixTile(tile, 1)).get()),
                        TOP_RIGHT_POSITION, placedTile))
                .get());
    }

    @Test
    public void shouldNotEvaluateATileInTheTopRightPositionIfNoTileHasBeenPlacedInCenterPosition() {
        assertTrue(tile(8)
                .flatMap(tile -> placedTantrixTile(tile, 0))
                .flatMap(placedTile
                        -> isNewTilePlacementValid(
                        HashMap.empty(),
                        TOP_RIGHT_POSITION, placedTile))
                .isBad());
    }

    @Test
    public void shouldAcceptTheTileInTheRightPositionIfItMatchesTheTileInTheCenterPositionAndTheTileInTheTopRightPosition() {
        assertTrue(tile(8)
                .flatMap(tile -> placedTantrixTile(tile, 5))
                .flatMap(placedTile
                        -> isNewTilePlacementValid(
                        HashMap.of(
                                CENTER_POSITION, tile(17).flatMap(tile -> placedTantrixTile(tile, 1)).get(),
                                TOP_RIGHT_POSITION, tile(1).flatMap(tile -> placedTantrixTile(tile, 1)).get()),
                        RIGHT_POSITION, placedTile))
                .get());

        assertFalse(tile(8)
                .flatMap(tile -> placedTantrixTile(tile, 3))
                .flatMap(placedTile
                        -> isNewTilePlacementValid(
                        HashMap.of(
                                CENTER_POSITION, tile(17).flatMap(tile -> placedTantrixTile(tile, 1)).get(),
                                TOP_RIGHT_POSITION, tile(1).flatMap(tile -> placedTantrixTile(tile, 1)).get()),
                        RIGHT_POSITION, placedTile))
                .get());

        assertFalse(tile(8)
                .flatMap(tile -> placedTantrixTile(tile, 1))
                .flatMap(placedTile
                        -> isNewTilePlacementValid(
                        HashMap.of(
                                CENTER_POSITION, tile(17).flatMap(tile -> placedTantrixTile(tile, 1)).get(),
                                TOP_RIGHT_POSITION, tile(1).flatMap(tile -> placedTantrixTile(tile, 1)).get()),
                        RIGHT_POSITION, placedTile))
                .get());
    }

    @Test
    public void shouldNotEvaluateATileInTheRightPositionIfNoTileHasBeenPlacedInCenterPosition() {
        assertTrue(tile(8)
                .flatMap(tile -> placedTantrixTile(tile, 5))
                .flatMap(placedTile
                        -> isNewTilePlacementValid(
                        HashMap.of(TOP_RIGHT_POSITION, tile(1).flatMap(tile -> placedTantrixTile(tile, 1)).get()),
                        RIGHT_POSITION, placedTile))
                .isBad());
    }

    @Test
    public void shouldNotEvaluateATileInTheRightPositionIfNoTileHasBeenPlacedInTopRightPosition() {
        assertTrue(tile(8)
                .flatMap(tile -> placedTantrixTile(tile, 5))
                .flatMap(placedTile
                        -> isNewTilePlacementValid(
                        HashMap.of(CENTER_POSITION, tile(17).flatMap(tile -> placedTantrixTile(tile, 1)).get()),
                        RIGHT_POSITION, placedTile))
                .isBad());
    }

    @Test
    public void shouldAcceptTheTileInTheBottomRightPositionIfItMatchesTheTileInTheCenterPositionAndTheTileInTheRightPosition() {
        assertTrue(tile(8)
                .flatMap(tile -> placedTantrixTile(tile, 0))
                .flatMap(placedTile
                        -> isNewTilePlacementValid(
                        HashMap.of(
                                CENTER_POSITION, tile(17).flatMap(tile -> placedTantrixTile(tile, 2)).get(),
                                RIGHT_POSITION, tile(1).flatMap(tile -> placedTantrixTile(tile, 2)).get()),
                        BOTTOM_RIGHT_POSITION, placedTile))
                .get());

        assertFalse(tile(8)
                .flatMap(tile -> placedTantrixTile(tile, 4))
                .flatMap(placedTile
                        -> isNewTilePlacementValid(
                        HashMap.of(
                                CENTER_POSITION, tile(17).flatMap(tile -> placedTantrixTile(tile, 2)).get(),
                                RIGHT_POSITION, tile(1).flatMap(tile -> placedTantrixTile(tile, 2)).get()),
                        BOTTOM_RIGHT_POSITION, placedTile))
                .get());

        assertFalse(tile(8)
                .flatMap(tile -> placedTantrixTile(tile, 2))
                .flatMap(placedTile
                        -> isNewTilePlacementValid(
                        HashMap.of(
                                CENTER_POSITION, tile(17).flatMap(tile -> placedTantrixTile(tile, 2)).get(),
                                RIGHT_POSITION, tile(1).flatMap(tile -> placedTantrixTile(tile, 2)).get()),
                        BOTTOM_RIGHT_POSITION, placedTile))
                .get());
    }

    @Test
    public void shouldAcceptTheTileInTheLeftPositionIfItMatchesTheTileInTheCenterPositionAndTheTileInTheBottomLeftPosition() {
        assertTrue(tile(8)
                .flatMap(tile -> placedTantrixTile(tile, 2))
                .flatMap(placedTile
                        -> isNewTilePlacementValid(
                        HashMap.of(
                                CENTER_POSITION, tile(17).flatMap(tile -> placedTantrixTile(tile, 4)).get(),
                                BOTTOM_LEFT_POSITION, tile(1).flatMap(tile -> placedTantrixTile(tile, 4)).get()),
                        LEFT_POSITION, placedTile))
                .get());

        assertFalse(tile(8)
                .flatMap(tile -> placedTantrixTile(tile, 0))
                .flatMap(placedTile
                        -> isNewTilePlacementValid(
                        HashMap.of(
                                CENTER_POSITION, tile(17).flatMap(tile -> placedTantrixTile(tile, 4)).get(),
                                BOTTOM_LEFT_POSITION, tile(1).flatMap(tile -> placedTantrixTile(tile, 4)).get()),
                        LEFT_POSITION, placedTile))
                .get());

        assertFalse(tile(8)
                .flatMap(tile -> placedTantrixTile(tile, 4))
                .flatMap(placedTile
                        -> isNewTilePlacementValid(
                        HashMap.of(
                                CENTER_POSITION, tile(17).flatMap(tile -> placedTantrixTile(tile, 4)).get(),
                                BOTTOM_LEFT_POSITION, tile(1).flatMap(tile -> placedTantrixTile(tile, 4)).get()),
                        LEFT_POSITION, placedTile))
                .get());
    }

    @Test
    public void shouldAcceptTheTileInTheTopLeftPositionIfItMatchesTheTileInTheCenterPositionTheTileInTheLeftPositionAndTheTileInTheTopRightPosition() {
        assertTrue(tile(8)
                .flatMap(tile -> placedTantrixTile(tile, 3))
                .flatMap(placedTile
                        -> isNewTilePlacementValid(
                        HashMap.of(
                                CENTER_POSITION, tile(17).flatMap(tile -> placedTantrixTile(tile, 5)).get(),
                                LEFT_POSITION, tile(1).flatMap(tile -> placedTantrixTile(tile, 5)).get(),
                                TOP_RIGHT_POSITION, tile(22).flatMap(tile -> placedTantrixTile(tile, 2)).get()),
                        TOP_LEFT_POSITION, placedTile))
                .get());

        assertFalse(tile(8)
                .flatMap(tile -> placedTantrixTile(tile, 1))
                .flatMap(placedTile
                        -> isNewTilePlacementValid(
                        HashMap.of(
                                CENTER_POSITION, tile(17).flatMap(tile -> placedTantrixTile(tile, 5)).get(),
                                LEFT_POSITION, tile(1).flatMap(tile -> placedTantrixTile(tile, 5)).get(),
                                TOP_RIGHT_POSITION, tile(22).flatMap(tile -> placedTantrixTile(tile, 2)).get()),
                        TOP_LEFT_POSITION, placedTile))
                .get());

        assertFalse(tile(8)
                .flatMap(tile -> placedTantrixTile(tile, 5))
                .flatMap(placedTile
                        -> isNewTilePlacementValid(
                        HashMap.of(
                                CENTER_POSITION, tile(17).flatMap(tile -> placedTantrixTile(tile, 5)).get(),
                                LEFT_POSITION, tile(1).flatMap(tile -> placedTantrixTile(tile, 5)).get(),
                                TOP_RIGHT_POSITION, tile(56).flatMap(tile -> placedTantrixTile(tile, 5)).get()),
                        TOP_LEFT_POSITION, placedTile))
                .get());

        assertFalse(tile(8)
                .flatMap(tile -> placedTantrixTile(tile, 3))
                .flatMap(placedTile
                        -> isNewTilePlacementValid(
                        HashMap.of(
                                CENTER_POSITION, tile(17).flatMap(tile -> placedTantrixTile(tile, 5)).get(),
                                LEFT_POSITION, tile(1).flatMap(tile -> placedTantrixTile(tile, 5)).get(),
                                TOP_RIGHT_POSITION, tile(22).flatMap(tile -> placedTantrixTile(tile, 5)).get()),
                        TOP_LEFT_POSITION, placedTile))
                .get());
    }

    @Test
    public void shouldBePossibleToPlaceFittingTileFromSetOfUnplacedTiles(){
        final SevenTilesPuzzle puzzle = Accumulation.withGood(tile(1), tile(7), tile(8), tile(9), tile(11), tile(13), tile(17), (a, b, c, d, e, f, g) -> HashSet.of(a, b, c, d, e, f, g)).flatMap(SevenTilesPuzzle::sevenTilesPuzzle).get();
        final TantrixTile tileToPlace = tile(11).get();
        final Or<Option<SevenTilesPuzzle>, Every<String>> result = placeNextTileInPuzzle(puzzle, CENTER_POSITION, placedTantrixTile(tileToPlace, 5).get());
        assertTrue(result.isGood());
        assertTrue(result.get().isDefined());
        assertEquals(1, result.get().get().getPlacedTiles().size());
        assertEquals(6, result.get().get().getUnplacedTiles().size());
        assertFalse(result.get().get().getUnplacedTiles().contains(tileToPlace));
    }

    @Test
    public void shouldNotBePossibleToPlaceFittingTileNotInSetOfUnplacedTiles(){
        final SevenTilesPuzzle puzzle = Accumulation.withGood(tile(1), tile(7), tile(8), tile(9), tile(11), tile(13), tile(17), (a, b, c, d, e, f, g) -> HashSet.of(a, b, c, d, e, f, g)).flatMap(SevenTilesPuzzle::sevenTilesPuzzle).get();
        final TantrixTile tileToPlace = tile(56).get();
        assertFalse(puzzle.getUnplacedTiles().contains(tileToPlace));
        assertTrue(placeNextTileInPuzzle(puzzle, CENTER_POSITION, placedTantrixTile(tileToPlace, 5).get()).isBad());
    }

    @Test
    public void shouldNotBePossibleToPlaceNonFittingTile(){
        final SevenTilesPuzzle puzzle = Accumulation.withGood(tile(1), tile(7), tile(8), tile(9), tile(11), tile(13), tile(17), (a, b, c, d, e, f, g) -> HashSet.of(a, b, c, d, e, f, g)).flatMap(SevenTilesPuzzle::sevenTilesPuzzle).get();
        final SevenTilesPuzzle puzzleWithFirstTilePlaced = placeNextTileInPuzzle(puzzle, CENTER_POSITION, tile(8).flatMap(tileToPlace -> placedTantrixTile(tileToPlace, 0)).get()).get().get();

        final Or<Option<SevenTilesPuzzle>, Every<String>> result = placeNextTileInPuzzle(puzzleWithFirstTilePlaced, TOP_RIGHT_POSITION, tile(1).flatMap(tileToPlace -> placedTantrixTile(tileToPlace, 1)).get());
        assertTrue(result.isGood());
        assertTrue(result.get().isEmpty());
    }

    private static Or<TantrixTile, Every<String>> tile(int tileNumber) {
        return TantrixTile.tile(tileNumber).badMap(o -> o.map(identity()));
    }
}
