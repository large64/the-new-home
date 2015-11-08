package engine.toolbox;

/**
 * Created by Dénes on 2015. 11. 07..
 */
public class Position {
    private int row;
    private int column;

    public Position() {
        this.row = 0;
        this.column = 0;
    }

    public Position(int row, int column) {
        this.row = row;
        this.column = column;
    }

    public void increase(int byRow, int byColumn) {
        this.row += byRow;
        this.column += byColumn;
    }

    // Use this function to make code more readable for humans
    public void decrease(int byRow, int byColumn) {
        this.row -= byRow;
        this.column -= byColumn;
    }

    public void set(int row, int column) {
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

    public int convertToMatrixPosition(int size) {
        return ((this.row) * size) + this.column;
    }
}
