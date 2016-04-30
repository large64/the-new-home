package game.logic.entities.buildings;

import game.graphics.windowparts.MiniMap;
import game.logic.entities.RawEntity;
import game.logic.toolbox.Side;
import game.logic.toolbox.map.Position;
import game.logic.toolbox.map.Tile;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Dénes on 2015. 11. 06..
 */
public class RawBuilding extends RawEntity {
    private int extent;

    public RawBuilding() {}

    public RawBuilding(int row, int column, int health, Side side) {
        super(new Position(row, column));
        this.setHealth(health);
        this.setSide(side);
    }

    @Override
    public String toString() {
        String toReturn = super.toString();
        return toReturn.replaceAll("entity", "building");
    }

    public int getExtent() {
        return extent;
    }

    void setExtent(int extent) {
        this.extent = extent;
    }

    public boolean hasExtent() {
        return extent > 1;
    }

    public List<Tile> getExtentPositions() {
        if (hasExtent()) {
            int currentX = tilePosition.getColumn();
            int currentY = tilePosition.getRow();

            ArrayList<Tile> tiles = new ArrayList<>();

            for (int i = (currentX - extent); i < (currentX + extent); i++) {
                // Shift column by one
                for (int j = (currentY - extent + 1); j < (currentY + extent + 1); j++) {
                    tiles.add(new Tile(j, i));
                }
            }

            return tiles;
        }
        return null;
    }
}
