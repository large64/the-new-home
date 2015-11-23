package engine.toolbox;

import engine.Map;

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
        return ((this.row) * Map.getRowNumber()) + this.column;
    }

    public boolean isBlocked(boolean isDestination) {
        return (
                this.row >= Map.getRowNumber()
                || this.row < 0
                || this.column >= Map.getRowNumber()
                || this.column < 0
                || !Map.isPositionFree(new Position(row, column), isDestination)
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
