package engine.entities;

import engine.toolbox.Position;
import terrains.Map;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Dénes on 2015. 12. 01..
 */
public class RawMap {
    private static int size = (int) Map.getSIZE();
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

    public static boolean isPositionFree(Position position, boolean isDestination) {
        return isDestination || RawMap.places[position.getRow()][position.getColumn()];
    }

    public static int getSize() {
        return RawMap.size;
    }
}
