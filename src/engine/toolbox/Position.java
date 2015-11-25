package engine.toolbox;

import engine.RawMap;

/**
 * Created by Dénes on 2015. 11. 07..
 */
public class Position {
    private int row;
    private int column;

    public Position(int row, int column) {
        this.row = row;
        this.column = column;
    }

    @Override
    public String toString() {
        return "[" + this.row + "," + this.column + "]";
    }

    public int getRow() {
        return this.row;
    }

    public int getColumn() {
        return this.column;
    }

    public int convertToMatrixPosition() {
        return ((this.row) * RawMap.getRowNumber()) + this.column;
    }

    public boolean isBlocked(boolean isDestination) {
        return (
                this.row >= RawMap.getRowNumber()
                        || this.row < 0
                        || this.column >= RawMap.getRowNumber()
                        || this.column < 0
                        || !RawMap.isPositionFree(new Position(row, column), isDestination)
        );
    }

    @Override
    public boolean equals(Object object) {
        if (object instanceof Position) {
            Position position = (Position) object;
            if (position.row == this.row && position.column == this.column) {
                return true;
            }
        }
        return false;
    }
}
