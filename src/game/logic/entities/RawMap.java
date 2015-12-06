package game.logic.entities;

import com.sun.istack.internal.Nullable;
import game.logic.entities.units.RawSoldier;
import game.logic.toolbox.map.Tile;
import game.graphics.windowparts.Map;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Dénes on 2015. 12. 01..
 */
public class RawMap {
    private static final int TILE_SIZE = 5;
    private static final int NR_OF_TILES = (int) (Map.getSIZE() / TILE_SIZE);

    private static int size = (int) (Map.getSIZE() / TILE_SIZE);
    private static List<RawEntity> entities = new ArrayList<>();
    private static RawEntity[][] places = new RawEntity[size][size];

    public RawMap(List<RawEntity> entities) {
        RawMap.entities = entities;
        RawMap.lookForChanges();
    }

    public static void lookForChanges() {
        if (!entities.isEmpty()) {
            for (int x = 0; x < RawMap.size; x++) {
                for (int y = 0; y < RawMap.size; y++) {
                    places[x][y] = null;
                    for (RawEntity entity : entities) {
                        if ((Tile.positionToTile(entity.getPosition()).getColumn() == x
                                && Tile.positionToTile(entity.getPosition()).getRow() == y)
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
        return ((RawSoldier) RawMap.places[tile.getColumn()][tile.getRow()]);
    }
}
