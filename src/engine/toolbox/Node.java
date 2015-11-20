package engine.toolbox;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by large64 on 19/11/15.
 */
public class Node {
    public int row;
    public int column;
    public int fCost;
    public int hCost;
    public int gCost;

    private static final int DISTANCE = 10;
    private static final int DIAGONAL_DISTANCE = 14;

    private List<Node> neighbors = new ArrayList<>();

    public Node parent = null;

    public Node(int row, int column) {
        this.column = column;
        this.row = row;
    }

    public void updateCosts(Position start, Position destination) {
    }

    public List getNeighbors() {
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

    public int getDistanceToThisNode() {
        int distance = 0;
        Node current = this;
        while (parent != null) {
            distance += current.gCost;
            current = current.parent;
        }
        return distance;
    }

    public int getgCost() {
        if (row != 0 && column != 0) {
            this.gCost = 14;
            return 14;
        }
        else {
            this.gCost = 10;
            return 10;
        }
    }

    public int gethCost(Position position1, Position position2) {
        int distance1 = Math.abs(position1.getRow() - position2.getRow());
        int distance2 = Math.abs(position1.getColumn() - position2.getColumn());
        return distance1 + distance2;
    }

    @Override
    public boolean equals(Object object) {
        if (object != null) {
            Node node = (Node) object;
            if (node.column == this.column && node.row == this.row) {
                return true;
            }
        }
        return false;
    }

    @Override
    public String toString() {
        return "[" + row + ", " + column + "] ";
    }
}
