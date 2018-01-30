package jorander.tantrixstpr.model;

import org.junit.Test;

import static io.vavr.Function1.identity;
import static jorander.tantrixstpr.model.PlacedTantrixTile.placedTantrixTile;
import static jorander.tantrixstpr.model.TantrixTile.BandColor.*;
import static jorander.tantrixstpr.model.TantrixTile.TileEdge.TOP_LEFT_EDGE;
import static jorander.tantrixstpr.model.TantrixTile.TileEdge.TOP_RIGHT_EDGE;
import static jorander.tantrixstpr.model.TantrixTile.tantrixTile;
import static jorander.tantrixstpr.model.TantrixTile.tile;
import static org.junit.Assert.*;

public class PlacedTantrixTileTest {

    @Test
    public void shouldBeconstructedWithATantrixTileAndANumberOfRotationStepsBetween0And5Iclusive() {
        assertNotNull(tile(1).badMap(o -> o.map(identity())).flatMap(tile -> placedTantrixTile(tile, 0)).get());
        assertTrue(placedTantrixTile(null, 0).isBad());
        assertTrue(tile(1).badMap(o -> o.map(identity())).flatMap(tile -> placedTantrixTile(tile, -1)).isBad());
        assertTrue(tile(1).badMap(o -> o.map(identity())).flatMap(tile -> placedTantrixTile(tile, 6)).isBad());
        assertEquals(2, placedTantrixTile(null, 6).getBad().size());
    }

    @Test
    public void shouldReturnTheBandColorOfTheTileEdgeAtASpecifiedPositionWithRespecToTheNumberOfRotationSteps() {
        TantrixTile tile = tantrixTile(99, BLUE, RED, YELLOW, YELLOW, BLUE, RED).get();
        assertEquals(BLUE, placedTantrixTile(tile, 0).get().bandColor(TOP_RIGHT_EDGE));
        assertEquals(RED, placedTantrixTile(tile, 0).get().bandColor(TOP_LEFT_EDGE));
        assertEquals(YELLOW, placedTantrixTile(tile, 2).get().bandColor(TOP_LEFT_EDGE));
        assertEquals(BLUE, placedTantrixTile(tile, 2).get().bandColor(TOP_RIGHT_EDGE));
    }
}
