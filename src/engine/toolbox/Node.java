package engine.toolbox;

import java.util.ArrayList;

/**
 * Created by large64 on 19/11/15.
 */
public class Node {
    private static final int DISTANCE = 10;
    private static final int DIAGONAL_DISTANCE = 14;

    public int row;
    public int column;
    public int fCost;
    public int hCost;
    public int gCost;
    public Node parent = null;

    private ArrayList<Node> neighbors = new ArrayList<>();

    public Node(int row, int column) {
        this.column = column;
        this.row = row;
    }

    public static int gethCost(Position position1, Position position2) {
        int distance1 = Math.abs(position1.getRow() - position2.getRow());
        int distance2 = Math.abs(position1.getColumn() - position2.getColumn());

        return distance1 + distance2;
    }

    public ArrayList<Node> getNeighbors() {
        neighbors.clear();

        neighbors.add(new Node(
                row - 1,
                column
        ));

        neighbors.add(new Node(
                row - 1,
                column + 1
        ));

        neighbors.add(new Node(
                row,
                column + 1
        ));

        neighbors.add(new Node(
                row + 1,
                column + 1
        ));

        neighbors.add(new Node(
                row + 1,
                column
        ));

        neighbors.add(new Node(
                row + 1,
                column - 1
        ));

        neighbors.add(new Node(
                row,
                column - 1
        ));

        neighbors.add(new Node(
                row - 1,
                column - 1
        ));

        return neighbors;
    }

    public int getgCost(Node current) {
        int rowOffset = Math.abs(current.row - this.row);
        int columnOffset = Math.abs(current.column - this.column);

        if (rowOffset != 0 && columnOffset != 0) {
            return DIAGONAL_DISTANCE;
        }
        return DISTANCE;
    }

    @Override
    public boolean equals(Object object) {
        if (object instanceof Node) {
            Node node = (Node) object;
            if (node.column == this.column && node.row == this.row) {
                return true;
            }
        }
        return false;
    }

    @Override
    public String toString() {
        return "[" + row + ", " + column + "]";
    }
}