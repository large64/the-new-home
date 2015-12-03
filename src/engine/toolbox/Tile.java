package engine.toolbox;

import engine.entities.RawMap;
import terrains.Map;

/**
 * Created by large64 on 2015.12.01..
 */
public class Tile {
    private int column;
    private int row;

    public Tile(int row, int column) {
        this.column = column;
        this.row = row;
    }

    public static Tile positionToTile(Position position) {
        // using formula (a + ((X - Xmin) * (b - a)/(Xmax - Xmin)))
        int tileColumn = (0 + ((position.getColumn() - 0) * (RawMap.getNrOfTiles() - 0) / ((int) Map.getSIZE() - 0)));
        int tileRow = (0 + ((position.getRow() - 0) * (RawMap.getNrOfTiles() - 0) / ((int) Map.getSIZE() - 0)));

        return new Tile(tileRow, tileColumn);
    }

    public Position toPosition() {
        int column = (0 + ((this.getColumn() - 0) * ((int) Map.getSIZE() - 0) / (RawMap.getNrOfTiles() - 0)));
        int row = (0 + ((this.getRow() - 0) * ((int) Map.getSIZE() - 0) / (RawMap.getNrOfTiles() - 0)));

        return new Position(row + 2.5f, column + 3.25f);
    }

    public boolean isBlocked(boolean isDestination) {
        return (
                this.row >= RawMap.getSize()
                        || this.row < 0
                        || this.column >= RawMap.getSize()
                        || this.column < 0
                        || !RawMap.isTileFree(new Tile(this.row, this.column), isDestination)
        );
    }

    public int getColumn() {
        return column;
    }

    public int getRow() {
        return row;
    }
}
