package game.logic.entities.units;

import game.graphics.toolbox.DisplayManager;
import game.graphics.windowparts.MiniMap;
import game.logic.entities.RawEntity;
import game.logic.entities.RawMap;
import game.logic.toolbox.Direction;
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
    private static final float TURN_SPEED = 100;
    private static final float DIAGONAL_ATTENUATION = 0.04f;

    private List<Node> path = new ArrayList<>();
    private Node currentNode;

    public RawUnit(int row, int column, boolean side) {
        super(new Position(row, column));
        this.position = this.getTilePosition().toPosition();
        this.setSide(side);
    }

    public void performAction(Tile tile) {
        // @TODO: make units avoid being on the same title when their destinations are approached by them
        if (!this.path.isEmpty()) {
            RawEntity entity = RawMap.whatIsOnTile(tile);

            if (entity != null && entity.getSide() != Side.FRIEND && entity.getSide() != Side.NEUTRAL) {
                if (this.isNextToAnEntity(entity)) {
                    entity.changeHealth(-10);
                }
                else {
                    this.step();
                }
            }
            else {
                this.step();
            }
        }
    }

    /**
     * Finds a way to tile (if there is). It is an implementation of A* algorithm.
     *
     * @param tile The tile on the raw map to be approached
     */
    public void calculatePath(Tile tile) {
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

            if (current.equals(new Node(tile.getRow(), tile.getColumn()))) {
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
        if (this.path != null && !currentNode.isProcessed) {
            Node toNode = path.get(0);

            Position currentPosition = this.position;
            float currentX = currentPosition.getX();
            float currentZ = currentPosition.getZ();

            Position toPosition = new Tile(toNode.row, toNode.column).toPosition();
            float toX = toPosition.getX();
            float toZ = toPosition.getZ();

            float distance = MOVEMENT_SPEED * DisplayManager.getFrameTimeSeconds();
            // @TODO: rotate before move to another tile

            // Move diagonal right down
            if (currentX < toX && currentZ < toZ &&
                    (distance(currentX, toX) > distance) && (distance(currentZ, toZ) > distance)) {
                this.position.x += distance - DIAGONAL_ATTENUATION;
                this.position.z += distance - DIAGONAL_ATTENUATION;
            }
            // Move diagonal right up
            else if (currentX < toX && currentZ > toZ &&
                    (distance(currentX, toX) > distance) && (distance(currentZ, toZ) > distance)) {
                this.position.x += distance - DIAGONAL_ATTENUATION;
                this.position.z -= distance - DIAGONAL_ATTENUATION;
            }
            // Move diagonal left down
            else if (currentX > toX && currentZ < toZ &&
                    (distance(currentX, toX) > distance) && (distance(currentZ, toZ) > distance)) {
                this.position.x -= distance - DIAGONAL_ATTENUATION;
                this.position.z += distance - DIAGONAL_ATTENUATION;
            }
            // Move diagonal left up
            else if (currentX > toX && currentZ > toZ &&
                    (distance(currentX, toX) > distance) && (distance(currentZ, toZ) > distance)) {
                this.position.x -= distance - DIAGONAL_ATTENUATION;
                this.position.z -= distance - DIAGONAL_ATTENUATION;
            }
            // Move right
            else if (currentX < toX && (distance(toX, currentX) > distance)) {
                this.position.x += distance;
            }
            // Move down
            else if (currentZ < toZ && (distance(toZ, currentZ) > distance)) {
                this.position.z += distance;
            }
            // Move left
            else if (currentX > toX && (distance(toX, currentX) > distance)) {
                this.position.x -= distance;
            }
            // Move up
            else if (currentZ > toZ && (distance(toZ, currentZ) > distance)) {
                this.position.z -= distance;
            }
            else {
                currentNode.isProcessed = true;
                path.remove(currentNode);
                if (!path.isEmpty()) {
                    currentNode = path.get(0);
                }
                else {
                    this.stopWalking();
                }
            }
            //MiniMap.mark(this.getTilePosition().toPosition());
        }
    }

    public boolean isMoving() {
        return !this.path.isEmpty();
    }

    public void stopWalking() {
        this.tilePosition = Tile.positionToTile(this.position);
        this.position = this.tilePosition.toPosition();
    }

    private float distance(float a, float b) {
        return Math.abs(a - b);
    }

    public List getPath() {
        return this.path;
    }
}
