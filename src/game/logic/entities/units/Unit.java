package game.logic.entities.units;

import game.graphics.windowparts.MiniMap;
import game.logic.entities.RawEntity;
import game.logic.entities.RawMap;
import game.logic.toolbox.map.Node;
import game.logic.toolbox.map.Position;
import game.logic.toolbox.map.Tile;

import java.util.*;

/**
 * Created by Dénes on 2015. 11. 06..
 */
public class Unit extends RawEntity {
    private List<Node> path = new ArrayList<>();
    private boolean isMoving = false;
    private Node currentNode;

    public Unit(int row, int column, int health, boolean side) {
        super(new Position(row, column));

        this.setHealth(health);
        this.setSide(side);
    }

    public void performAction(Tile tile) {
        if (!this.path.isEmpty()) {
            RawEntity entity = RawMap.whatIsOnTile(tile);
            String type = "";
            String id;

            if (entity != null) {
                id = ((RawSoldier) (entity)).getId();
                type = id.substring(0, id.length() - 1);
            }

            switch (type) {
                case "soldier":
                    if (this.isNextToAnEntity(entity) && entity != null) {
                        entity.changeHealth(-10);
                        System.out.println(entity.getHealth());
                    }
                    else {
                        this.step();
                    }
                    break;
                default:
                    this.step();
                    break;
            }
        }
    }

    /**
     * Finds a way to tile (if there is). It is an implementation of A* algorithm.
     *
     * @param tile The tile on the raw map to be approached
     */
    public void calculatePath(Tile tile) {
        clearPath();
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
                    this.path.add(current);
                    current = current.parent;
                }
                Collections.reverse(this.path);
                currentNode = path.get(0);
                path.remove(0);
                this.isMoving = true;
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

    public void step() {
        if (this.path != null) {
            if (!currentNode.isProcessed) {
                Tile tilePosition = new Tile(currentNode.row, currentNode.column);
                this.position = tilePosition.toPosition();
                this.tilePosition = tilePosition;
                this.position.y = game.graphics.windowparts.Map.getHeightOfMap(this.position.x, this.position.z);
                MiniMap.mark(this.position);
                currentNode.isProcessed = true;
                if (this.path.size() > 0) {
                    this.path.remove(currentNode);
                }
            }
            else {
                currentNode = path.get(0);
            }
        }
    }

    public boolean isMoving() {
        return !this.path.isEmpty();
    }

    public void clearPath() {
        this.path.clear();
    }

    public List<Node> getPath() {
        return path;
    }
}
