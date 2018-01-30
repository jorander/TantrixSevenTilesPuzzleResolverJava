package jorander.tantrixstpr.model;

import com.github.javactic.*;
import io.vavr.Tuple;
import io.vavr.Tuple2;
import io.vavr.collection.HashMap;
import io.vavr.collection.List;
import io.vavr.collection.Map;
import io.vavr.collection.Set;
import io.vavr.control.Option;
import jorander.tantrixstpr.model.TantrixTile.TileEdge;
import lombok.AllArgsConstructor;
import lombok.Value;

import static com.github.javactic.Accumulation.withGood;
import static io.vavr.API.*;
import static jorander.tantrixstpr.model.SevenTilesPuzzle.TilePosition.*;
import static jorander.tantrixstpr.model.TantrixTile.TileEdge.*;
import static lombok.AccessLevel.PRIVATE;

@Value
@AllArgsConstructor(access = PRIVATE)
public class SevenTilesPuzzle {

    public static enum TilePosition {
        // Ordered
        CENTER_POSITION,
        TOP_RIGHT_POSITION,
        RIGHT_POSITION,
        BOTTOM_RIGHT_POSITION,
        BOTTOM_LEFT_POSITION,
        LEFT_POSITION,
        TOP_LEFT_POSITION;
    }

    Map<TilePosition, PlacedTantrixTile> placedTiles;
    Set<TantrixTile> unplacedTiles;

    public static Or<SevenTilesPuzzle, One<String>> sevenTilesPuzzle(Set<TantrixTile> tiles) {
        if (tiles == null) {
            return Bad.ofOne("Tiles could not be null");
        }
        if (tiles.length() != 7) {
            return Bad.ofOneString("SeventilesPuzzle should be constructed with 7 tiles, {} provided", tiles.length());
        }
        return Good.of(new SevenTilesPuzzle(HashMap.empty(), tiles));
    }

    public static Or<Option<SevenTilesPuzzle>, Every<String>> placeNextTileInPuzzle(SevenTilesPuzzle partiallySolvedPuzzle, TilePosition position, PlacedTantrixTile tileToPlace) {
        if (!partiallySolvedPuzzle.getUnplacedTiles().contains(tileToPlace.getTile())) {
            return Bad.of(Every.of("Tile no " + tileToPlace.getTile().getTileNumber() + " not in set of unplaced tiles."));
        }
        return isNewTilePlacementValid(partiallySolvedPuzzle.getPlacedTiles(), position, tileToPlace)
                .map(isValid
                        -> isValid
                        ? Option.of(new SevenTilesPuzzle(partiallySolvedPuzzle.getPlacedTiles().put(position, tileToPlace), partiallySolvedPuzzle.getUnplacedTiles().remove(tileToPlace.getTile())))
                        : Option.none());
    }

    private static final Map<TilePosition, List<Tuple2<TileEdge, Tuple2<TilePosition, TileEdge>>>> TILE_INTERSECTIONS_TO_CHECK = HashMap.of(
            CENTER_POSITION, List.empty(), // No checks for CenterPosition since it is the first tile
            TOP_RIGHT_POSITION, List.of(
                    Tuple.of(BOTTOM_LEFT_EDGE, Tuple.of(CENTER_POSITION, TOP_RIGHT_EDGE))),
            RIGHT_POSITION, List.of(
                    Tuple.of(LEFT_EDGE, Tuple.of(CENTER_POSITION, RIGHT_EDGE)),
                    Tuple.of(TOP_LEFT_EDGE, Tuple.of(TOP_RIGHT_POSITION, BOTTOM_RIGHT_EDGE))),
            BOTTOM_RIGHT_POSITION, List.of(
                    Tuple.of(TOP_LEFT_EDGE, Tuple.of(CENTER_POSITION, BOTTOM_RIGHT_EDGE)),
                    Tuple.of(TOP_RIGHT_EDGE, Tuple.of(RIGHT_POSITION, BOTTOM_LEFT_EDGE))),
            BOTTOM_LEFT_POSITION, List.of(
                    Tuple.of(TOP_RIGHT_EDGE, Tuple.of(CENTER_POSITION, BOTTOM_LEFT_EDGE)),
                    Tuple.of(RIGHT_EDGE, Tuple.of(BOTTOM_RIGHT_POSITION, LEFT_EDGE))),
            LEFT_POSITION, List.of(
                    Tuple.of(RIGHT_EDGE, Tuple.of(CENTER_POSITION, LEFT_EDGE)),
                    Tuple.of(BOTTOM_RIGHT_EDGE, Tuple.of(BOTTOM_LEFT_POSITION, TOP_LEFT_EDGE))),
            TOP_LEFT_POSITION, List.of(
                    Tuple.of(BOTTOM_RIGHT_EDGE, Tuple.of(CENTER_POSITION, TOP_LEFT_EDGE)),
                    Tuple.of(BOTTOM_LEFT_EDGE, Tuple.of(LEFT_POSITION, TOP_RIGHT_EDGE)),
                    Tuple.of(RIGHT_EDGE, Tuple.of(TOP_RIGHT_POSITION, LEFT_EDGE)))
    );

    public static Or<Boolean, Every<String>> isNewTilePlacementValid(Map<TilePosition, PlacedTantrixTile> placedTiles, TilePosition position, PlacedTantrixTile tile) {
        return Or.from(TILE_INTERSECTIONS_TO_CHECK.get(position), Every.of("Unknown position: " + position))
                .flatMap(tileIntersectionsToCheck
                        -> Match(tileIntersectionsToCheck)
                        .of(
                                Case($(List.empty()), () -> List.of(Or.<Boolean, Every<String>>good(true))),
                                Case($(), checksToMake
                                        -> checksToMake
                                        .map(checkToMake -> {
                                            TileEdge tileEdgeToCheck = checkToMake._1;
                                            TilePosition otherTilePosition = checkToMake._2._1;
                                            TileEdge otherTileEdge = checkToMake._2._2;
                                            return Or.from(placedTiles.get(otherTilePosition), Every.of("Tile in position " + otherTilePosition + " must be placed first.")).badMap(e -> e.map(s -> s)).map(otherTile -> otherTile
                                                    .bandColor(otherTileEdge) == tile.bandColor(tileEdgeToCheck));
                                        })
                                )
                        ).reduce((a, b) -> withGood(a, b, (c, d) -> c && d))
                );
    }
}
