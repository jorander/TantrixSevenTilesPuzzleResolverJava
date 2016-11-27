package jorander.tantrixstpr.model;

import static com.github.javactic.Accumulation.withGood;
import com.github.javactic.Bad;
import com.github.javactic.Every;
import com.github.javactic.Good;
import com.github.javactic.Or;
import javaslang.collection.List;
import jorander.tantrixstpr.model.TantrixTile.BandColor;
import jorander.tantrixstpr.model.TantrixTile.TileEdge;
import static lombok.AccessLevel.PRIVATE;
import lombok.AllArgsConstructor;
import lombok.Value;

@Value
@AllArgsConstructor(access = PRIVATE)
public final class PlacedTantrixTile {

    TantrixTile tile;
    int nbrOfRotationSteps;

    public BandColor bandColor(TileEdge edgeAsPlaced) {
        return tile.edgeColor(edgeOfPlacedTile(edgeAsPlaced));
    }

    private TileEdge edgeOfPlacedTile(TileEdge edgeAsPlaced) {
        int edgeIndex = EDGE_POSITIONS.indexOf(edgeAsPlaced) - nbrOfRotationSteps;
        return EDGE_POSITIONS.get((edgeIndex >= 0) ? edgeIndex : edgeIndex + 6);
    }

    private static final List<TileEdge> EDGE_POSITIONS = List.of(TileEdge.values());

    public static Or<PlacedTantrixTile, Every< String>> placedTantrixTile(TantrixTile tile, int nbrOfRotationSteps) {
        return withGood(tile != null ? Good.of(tile) : Bad.ofOne("Tile must not be null"),
                (nbrOfRotationSteps >= 0 && nbrOfRotationSteps <= 5) ? Good.of(nbrOfRotationSteps) : Bad.ofOne("Number of Rotation Steps must be between 0 and 5 (inclusive)"),
                (validTile, validNbrOfRotationSteps) -> new PlacedTantrixTile(validTile, validNbrOfRotationSteps)
        );
    }
}
