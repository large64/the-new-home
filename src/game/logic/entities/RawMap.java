package game.logic.entities;

import com.sun.istack.internal.Nullable;
import game.graphics.windowparts.Map;
import game.graphics.windowparts.Scene;
import game.logic.entities.buildings.RawBuilding;
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

    public RawMap() {}

    public static void lookForChanges() {
        places = new RawEntity[size][size];

        if (!rawEntities.isEmpty()) {
            for (RawEntity entity : rawEntities) {
                if (entity.isAlive()) {
                    int x = entity.getTilePosition().getColumn();
                    int y = entity.getTilePosition().getRow();

                    // @TODO: should not store the whole entity
                    places[x][y] = entity;

                    if (entity instanceof RawBuilding && ((RawBuilding) entity).hasExtent()) {
                        List<Tile> extentPositions = ((RawBuilding) entity).getExtentPositions();
                        for (Tile tile : extentPositions) {
                            int extentX = tile.getColumn();
                            int extentY = tile.getRow();

                            places[extentX][extentY] = entity;
                        }
                    }
                }
            }
        }
    }

    public static boolean isTileFree(Tile tile, boolean isDestination) {
        if (tile.getRow() <= size && tile.getRow() >= 0 && tile.getColumn() <= size && tile.getColumn() >= 0) {
            boolean isTrue = RawMap.places[tile.getColumn()][tile.getRow()] == null;
            return isDestination || isTrue;
        }
        return false;
    }

    public static boolean areTilesFree(Tile selectedTile, int[] extent) {
        List<Tile> extentPositions = RawBuilding.getExtentPositions(selectedTile, extent);

        for (Tile tile : extentPositions) {
            if (!RawMap.isTileFree(tile, false)) {
                return false;
            }
        }

        return true;
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

    public static void freeTiles(List<Tile> tiles) {
        for (Tile tile : tiles) {
            places[tile.getColumn()][tile.getRow()] = null;
        }
    }

    public static void setRawEntities() {
        RawMap.rawEntities = Scene.getRawEntities();
    }

    public static void addEntity(RawEntity rawEntity) {
        if (!rawEntities.contains(rawEntity)) {
            rawEntities.add(rawEntity);
        }
    }

    public static List<RawEntity> getRawEntities() {
        return rawEntities;
    }
}
