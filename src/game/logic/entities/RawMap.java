package game.logic.entities;

import com.sun.istack.internal.Nullable;
import game.graphics.windowparts.Map;
import game.logic.toolbox.map.Tile;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Dénes on 2015. 12. 01..
 */
public class RawMap {
    private static final int TILE_SIZE = 5;
    private static final int NR_OF_TILES = (int) (Map.getSIZE() / TILE_SIZE);

    private static int size = (int) (Map.getSIZE() / TILE_SIZE);
    private static List<RawEntity> rawEntities = new ArrayList<>();
    private static RawEntity[][] places = new RawEntity[size][size];

    public RawMap(List<RawEntity> rawEntities) {
        RawMap.rawEntities = rawEntities;
        RawMap.lookForChanges();
    }

    public static void lookForChanges() {
        if (!rawEntities.isEmpty()) {
            for (int x = 0; x < RawMap.size; x++) {
                for (int y = 0; y < RawMap.size; y++) {
                    places[x][y] = null;
                    for (RawEntity entity : rawEntities) {
                        if (entity.getTilePosition().getColumn() == x
                                && entity.getTilePosition().getRow() == y
                                && entity.isAlive()) {
                            places[x][y] = entity;
                        }
                    }
                }
            }
        }
    }

    public static boolean isTileFree(Tile tile, boolean isDestination) {
        boolean isTrue = RawMap.places[tile.getColumn()][tile.getRow()] == null;
        return isDestination || isTrue;
    }

    public static int getSize() {
        return RawMap.size;
    }

    public static int getNrOfTiles() {
        return NR_OF_TILES;
    }

    @Nullable
    public static RawEntity whatIsOnTile(Tile tile) {
        return (RawMap.places[tile.getColumn()][tile.getRow()]);
    }
}
