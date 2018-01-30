package jorander.tantrixstpr.model;

import org.junit.Test;

import static jorander.tantrixstpr.model.TantrixTile.BandColor.*;
import static jorander.tantrixstpr.model.TantrixTile.TileEdge.BOTTOM_LEFT_EDGE;
import static jorander.tantrixstpr.model.TantrixTile.tantrixTile;
import static jorander.tantrixstpr.model.TantrixTile.tile;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class TantrixTileTest {

    @Test
    public void shouldBeConstructedWithTwoEdgesOfEachColor() {
        assertEquals(6, tantrixTile(99, RED, RED, BLUE, BLUE, GREEN, GREEN).get().getEdgeColors().size());
        assertTrue(tantrixTile(99, RED, RED, RED, RED, GREEN, GREEN).isBad());
        assertTrue(tantrixTile(99, RED, RED, BLUE, YELLOW, GREEN, GREEN).isBad());
    }

    @Test
    public void shouldPreDefineAll56Tiles() {
        assertEquals(tantrixTile(1, BLUE, RED, YELLOW, YELLOW, BLUE, RED), tile(1));
        assertTrue(tile(57).isBad());
    }

    @Test
    public void shouldReturnTheBandColorForASpecificTileEdge() {
        assertEquals(YELLOW, tile(1).get().edgeColor(BOTTOM_LEFT_EDGE));
    }
}
