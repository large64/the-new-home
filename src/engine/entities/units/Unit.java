package engine.entities.units;

import engine.entities.RawEntity;
import engine.toolbox.Node;
import engine.toolbox.Position;
import engine.toolbox.Tile;

import java.util.*;

/**
 * Created by Dénes on 2015. 11. 06..
 */
public class Unit extends RawEntity {
    private List<Node> path = new ArrayList<>();
    private boolean isMoving = false;

    public Unit(int row, int column, int health, boolean side) {
        super(new Position(row, column));

        this.setHealth(health);
        this.setSide(side);
    }

    /**
     * Finds a way to rawEntity (if there is) and approaches it. It is an implementation of A* algorithm.
     *
     * @param tile The tile on the raw map to be approached
     */
    public void goTo(Tile tile) {
        path.clear();
        List<Node> open = new ArrayList<>();
        Queue<Node> closed = new LinkedList<>();

        Node startPosition = new Node(this.getTilePosition().getRow(), this.getTilePosition().getColumn());
        open.add(startPosition);

        while (!open.isEmpty()) {
            Node current = open.get(0);
            int min = current.fCost;

            for (Node innerCurrent : open) {
                if (innerCurrent.fCost < min) {
                    min = innerCurrent.fCost;
                    current = innerCurrent;
                }
            }

            if (current.equals(new Node(tile.getRow(), tile.getColumn()))) {
                while (current != null) {
                    path.add(current);
                    current = current.parent;
                }
                Collections.reverse(path);
                System.out.println(path);
                return;
            }

            open.remove(current);
            closed.add(current);

            ArrayList<Node> neighbors = current.getNeighbors();

            processNeighbors(neighbors, tile, open, closed, current);
        }
    }

    /**
     * Processes neighbors of a selected node in a graph. This is a part of A* algorithm.
     *
     * @param neighbors The neighbors of the selected node to be processed
     * @param tile The destination tile, which we are heading to
     * @param open The list of currently open (not yet processed) nodes
     * @param closed The list of already processed nodes
     * @param current The current node which is being processed. Taken from the open list, and put into closed when
     *                processed.
     */
    private void processNeighbors(List<Node> neighbors, Tile tile, List open, Queue<Node> closed, Node current) {
        for (Node neighbor : neighbors) {
            Tile neighborPosition = new Tile(neighbor.row, neighbor.column);
            boolean isDestination = false;

            if (neighborPosition.equals(tile)) {
                isDestination = true;
            }

            if (closed.contains(neighbor) || neighborPosition.isBlocked(isDestination)) {
                continue;
            }

            int gCost = current.gCost + neighbor.getgCost(current);
            boolean isgCostBest = false;

            if (!open.contains(neighbor)) {
                isgCostBest = true;
                neighbor.hCost = Node.gethCost(neighborPosition, tile);
                open.add(neighbor);
            } else if (gCost < neighbor.gCost) {
                isgCostBest = true;
            }

            if (isgCostBest) {
                neighbor.parent = current;
                neighbor.gCost = gCost;
                neighbor.fCost = neighbor.gCost + neighbor.hCost;
            }
        }
    }
}
