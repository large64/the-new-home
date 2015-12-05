package game.logic.entities;

import engine.toolbox.Tile;
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
    private static boolean[][] places = new boolean[size][size];

    public RawMap(List<RawEntity> entities) {
        RawMap.entities = entities;
        RawMap.lookForChanges();
    }

    public static void lookForChanges() {
        if (!entities.isEmpty()) {
            for (int x = 0; x < RawMap.size; x++) {
                for (int y = 0; y < RawMap.size; y++) {
                    places[x][y] = true;
                    for (RawEntity entity : entities) {
                        if ((entity.getPosition().getRow() == x && entity.getPosition().getColumn() == y)
                                && entity.isAlive()) {
                            places[x][y] = false;
                        }
                    }
                }
            }
        }
    }

    public static boolean isTileFree(Tile tile, boolean isDestination) {
        return isDestination || RawMap.places[tile.getColumn()][tile.getRow()];
    }

    public static int getSize() {
        return RawMap.size;
    }

    public static int getNrOfTiles() {
        return NR_OF_TILES;
    }
}
