package game.logic.entities.buildings;

import game.logic.entities.RawEntity;
import game.logic.toolbox.Side;
import game.logic.toolbox.map.Position;
import game.logic.toolbox.map.Tile;

import java.util.ArrayList;
import java.util.List;

public class RawBuilding extends RawEntity {
    private int[] extent = new int[2];

    public RawBuilding() {
    }

    public RawBuilding(int row, int column, int health, Side side) {
        super(new Position(row, column));
        this.setHealth(health);
        this.setSide(side);
    }

    public static List<Tile> getExtentPositions(Tile fromTile, int[] extent) {
        int currentX = fromTile.getColumn();
        int currentY = fromTile.getRow();

        ArrayList<Tile> tiles = new ArrayList<>();
        int extentX = extent[0];
        int extentY = extent[1];

        for (int i = (currentX - extentX); i < (currentX + extentX); i++) {
            // Shift column by one
            for (int j = (currentY - extentY + 1); j < (currentY + extentY); j++) {
                tiles.add(new Tile(j, i));
            }
        }

        return tiles;
    }

    @Override
    public String toString() {
        String toReturn = super.toString();
        return toReturn.replaceAll("entity", "building");
    }

    public int[] getExtent() {
        return extent;
    }

    void setExtent(int[] extent) {
        this.extent = extent;
    }

    public int getExtentX() {
        return extent[0];
    }

    public int getExtentY() {
        return extent[1];
    }

    public boolean hasExtent() {
        return getExtentX() > 1 || getExtentY() > 1;
    }

    public List<Tile> getExtentPositions() {
        if (hasExtent()) {
            int currentX = tilePosition.getColumn();
            int currentY = tilePosition.getRow();

            ArrayList<Tile> tiles = new ArrayList<>();
            int extentX = getExtentX();
            int extentY = getExtentY();

            for (int i = (currentX - extentX); i < (currentX + extentX); i++) {
                // Shift column by one
                for (int j = (currentY - extentY + 1); j < (currentY + extentY); j++) {
                    tiles.add(new Tile(j, i));
                }
            }

            return tiles;
        }
        return null;
    }
}
