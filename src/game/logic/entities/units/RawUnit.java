package game.logic.entities.units;

import game.graphics.toolbox.DisplayManager;
import game.graphics.windowparts.InfoProvider;
import game.graphics.windowparts.Scene;
import game.logic.entities.RawEntity;
import game.logic.entities.RawMap;
import game.logic.toolbox.Side;
import game.logic.toolbox.map.Node;
import game.logic.toolbox.map.Position;
import game.logic.toolbox.map.Tile;

import java.util.*;

/**
 * Created by Dénes on 2015. 11. 06..
 */
public class RawUnit extends RawEntity {
    private static final float MOVEMENT_SPEED = 7;
    private static final float DIAGONAL_ATTENUATION = 0.04f;

    private List<Node> path = new ArrayList<>();
    private Node currentNode;
    private Tile destinationTile;

    public RawUnit() {
    }

    public RawUnit(int row, int column, Side side) {
        super(new Position(row, column));
        this.position = this.getTilePosition().toPosition();
        this.setSide(side);
    }

    public void performAction(Tile tile) {
        // @TODO: make units avoid being on the same title when their destinations are approached by them
        if (!this.path.isEmpty() && destinationTile != null) {
            try {
                RawEntity enemy = RawMap.whatIsOnTile(tile);

                if (enemy != null && !enemy.getSide().equals(Side.FRIEND) && this.isNextToAnEntity(enemy)) {
                    if (enemy.isAlive()) {
                        enemy.changeHealth(-0.5f);
                    }
                    else {
                        enemy.isMarkedForDeletion = true;
                        this.step();
                    }
                } else {
                    this.step();
                }
            } catch (IndexOutOfBoundsException ex) {
                this.getPath().clear();
            }
        }
    }

    /**
     * Finds a way to tile (if there is). It is an implementation of A* algorithm.
     */
    public void calculatePath() {
        if (destinationTile != null) {
            this.path.clear();
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

                if (current.equals(new Node(destinationTile.getRow(), destinationTile.getColumn()))) {
                    while (current != null) {
                        this.path.add(current);
                        current = current.parent;
                    }
                    Collections.reverse(this.path);
                    currentNode = path.get(0);
                    path.remove(0);
                    return;
                }

                open.remove(current);
                closed.add(current);

                ArrayList<Node> neighbors = current.getNeighbors();

                processNeighbors(neighbors, destinationTile, open, closed, current);
            }
        }
    }

    /**
     * Processes neighbors of a selected node in a graph. This is a part of A* algorithm.
     *
     * @param neighbors The neighbors of the selected node to be processed
     * @param tile      The destination tile, which we are heading to
     * @param open      The list of currently open (not yet processed) nodes
     * @param closed    The list of already processed nodes
     * @param current   The current node which is being processed. Taken from the open list, and put into closed when
     *                  processed.
     */
    private void processNeighbors(List<Node> neighbors, Tile tile, List<Node> open, Queue<Node> closed, Node current) {
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
        if (this.path != null && !currentNode.isProcessed) {
            Node toNode = path.get(0);

            Position currentPosition = this.position;
            float currentX = currentPosition.getX();
            float currentZ = currentPosition.getZ();

            Position toPosition = new Tile(toNode.row, toNode.column).toPosition();
            float toX = toPosition.getX();
            float toZ = toPosition.getZ();

            float distance = MOVEMENT_SPEED * DisplayManager.getFrameTimeSeconds();

            // Move diagonal right down
            if (currentX < toX && currentZ < toZ &&
                    (distance(currentX, toX) > distance) && (distance(currentZ, toZ) > distance)) {
                this.turnToDirection(45);
                this.position.x += distance - DIAGONAL_ATTENUATION;
                this.position.z += distance - DIAGONAL_ATTENUATION;
            }
            // Move diagonal right up
            else if (currentX < toX && currentZ > toZ &&
                    (distance(currentX, toX) > distance) && (distance(currentZ, toZ) > distance)) {
                this.turnToDirection(145);
                this.position.x += distance - DIAGONAL_ATTENUATION;
                this.position.z -= distance - DIAGONAL_ATTENUATION;
            }
            // Move diagonal left down
            else if (currentX > toX && currentZ < toZ &&
                    (distance(currentX, toX) > distance) && (distance(currentZ, toZ) > distance)) {
                this.turnToDirection(315);
                this.position.x -= distance - DIAGONAL_ATTENUATION;
                this.position.z += distance - DIAGONAL_ATTENUATION;
            }
            // Move diagonal left up
            else if (currentX > toX && currentZ > toZ &&
                    (distance(currentX, toX) > distance) && (distance(currentZ, toZ) > distance)) {
                this.turnToDirection(225);
                this.position.x -= distance - DIAGONAL_ATTENUATION;
                this.position.z -= distance - DIAGONAL_ATTENUATION;
            }
            // Move right
            else if (currentX < toX && (distance(toX, currentX) > distance)) {
                this.turnToDirection(90);
                this.position.x += distance;
            }
            // Move down
            else if (currentZ < toZ && (distance(toZ, currentZ) > distance)) {
                this.turnToDirection(0);
                this.position.z += distance;
            }
            // Move left
            else if (currentX > toX && (distance(toX, currentX) > distance)) {
                this.turnToDirection(270);
                this.position.x -= distance;
            }
            // Move up
            else if (currentZ > toZ && (distance(toZ, currentZ) > distance)) {
                this.turnToDirection(180);
                this.position.z -= distance;
            } else {
                this.tilePosition = new Tile(currentNode.row, currentNode.column);
                currentNode.isProcessed = true;
                path.remove(currentNode);
                RawMap.lookForChanges();
                if (!path.isEmpty()) {
                    currentNode = path.get(0);
                } else {
                    this.stopWalking();
                }
            }
            //MiniMap.mark(this.getTilePosition().toPosition());
        }
    }

    public boolean isMoving() {
        return !this.path.isEmpty();
    }

    private void stopWalking() {
        this.tilePosition = Tile.positionToTile(this.position);
        this.position = this.tilePosition.toPosition();
    }

    private float distance(float a, float b) {
        return Math.abs(a - b);
    }

    private void turnToDirection(float to) {
        if (this.rotation != to) {
            this.rotation = to;
        }
    }

    public List getPath() {
        return this.path;
    }

    public void setPath(List<Node> path) {
        this.path = path;
    }

    public void setCurrentNode(Node currentNode) {
        this.currentNode = currentNode;
    }

    public Tile getDestinationTile() {
        return destinationTile;
    }

    public void setDestinationTile(Tile destinationTile) {
        this.destinationTile = destinationTile;
    }
}
