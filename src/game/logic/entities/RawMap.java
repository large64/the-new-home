package game.logic.entities;

import com.sun.istack.internal.Nullable;
import game.graphics.windowparts.Map;
import game.graphics.windowparts.Scene;
import game.logic.entities.buildings.RawBuilding;
import game.logic.toolbox.map.Tile;

import java.util.ArrayList;
import java.util.List;

public class RawMap {
    private static final int TILE_SIZE = 5;
    private static final int NR_OF_TILES = (int) (Map.getSIZE() / TILE_SIZE);

    private static final int SIZE = (int) (Map.getSIZE() / TILE_SIZE);
    private static List<RawEntity> rawEntities = new ArrayList<>();
    private static RawEntity[][] places = new RawEntity[SIZE][SIZE];

    public RawMap() {
    }

    public static void lookForChanges() {
        places = new RawEntity[SIZE][SIZE];

        if (!rawEntities.isEmpty()) {
            rawEntities.stream().filter(RawEntity::isAlive).forEach(entity -> {
                int x = entity.getTilePosition().getColumn();
                int y = entity.getTilePosition().getRow();

                places[x][y] = entity;

                if (entity instanceof RawBuilding && ((RawBuilding) entity).hasExtent()) {
                    List<Tile> extentPositions = ((RawBuilding) entity).getExtentPositions();
                    for (Tile tile : extentPositions) {
                        int extentX = tile.getColumn();
                        int extentY = tile.getRow();

                        places[extentX][extentY] = entity;
                    }
                }
            });
        }
    }

    public static boolean isTileFree(Tile tile, boolean isDestination) {
        if (tile.getRow() <= SIZE && tile.getRow() >= 0 && tile.getColumn() <= SIZE && tile.getColumn() >= 0) {
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
        return RawMap.SIZE;
    }

    public static int getNrOfTiles() {
        return NR_OF_TILES;
    }

    @Nullable
    public static RawEntity whatIsOnTile(Tile tile) throws ArrayIndexOutOfBoundsException {
        return RawMap.places[tile.getColumn()][tile.getRow()];
    }

    public static void freeTiles(List<Tile> tiles) {
        for (Tile tile : tiles) {
            places[tile.getColumn()][tile.getRow()] = null;
        }
    }

    public static void setRawEntities() {
        RawMap.rawEntities = Scene.getRawEntities();
    }

    public static List<RawEntity> getRawEntities() {
        return rawEntities;
    }
}
