package engine.entities.units;

import engine.MiniMap;
import engine.actions.ActionType;
import engine.entities.RawEntity;
import engine.entities.RawMap;
import engine.exceptions.ImproperActionException;
import engine.toolbox.Node;
import engine.toolbox.Position;
import engine.toolbox.Tile;
import main.Game;
import terrains.*;

import java.util.*;

/**
 * Created by Dénes on 2015. 11. 06..
 */
public class Unit extends RawEntity {
    public Unit(int row, int column, int health, boolean side) {
        super(new Position(row, column));

        this.setHealth(health);
        this.setSide(side);
    }

    /**
     * Helps this to perform an ActionType on rawEntity
     *
     * @param rawEntity The rawEntity on which the action will be performed
     * @param action The action to be performed on rawEntity
     * @throws ImproperActionException
     */
    public void performAction(RawEntity rawEntity, ActionType action) throws ImproperActionException {
        switch (action) {
            case ATTACK:
                if (this instanceof RawSoldier && this.getSide() != rawEntity.getSide()) {
                    while (!this.isNextToAnEntity(rawEntity)) {
                        this.goTo(rawEntity.getTilePosition());
                    }

                    while (rawEntity.getHealth() > 0) {
                        rawEntity.changeHealth(-10);
                        MiniMap.lookForChanges();
                        RawMap.lookForChanges();
                    }
                    rawEntity.setBeingAttacked(false);
                } else {
                    throw new ImproperActionException();
                }
                break;

            case HEAL:
                if (this instanceof RawHealer && this.getSide() == rawEntity.getSide()) {
                    while (!this.isNextToAnEntity(rawEntity)) {
                        this.goTo(rawEntity.getTilePosition());
                    }

                    while (rawEntity.getHealth() < 100) {
                        rawEntity.changeHealth(10);
                        MiniMap.lookForChanges();
                        RawMap.lookForChanges();
                    }
                    rawEntity.setBeingHealed(false);
                } else {
                    throw new ImproperActionException();
                }
                break;
            case WALK:
                while (!this.isNextToAnEntity(rawEntity)) {
                    this.goTo(rawEntity.getTilePosition());
                }
                break;
        }
        MiniMap.lookForChanges();
        RawMap.lookForChanges();
    }

    /**
     * Finds a way to rawEntity (if there is) and approaches it. It is an implementation of A* algorithm.
     *
     * @param tile The tile on the raw map to be approached
     */
    public void goTo(Tile tile) {
        List<Node> path = new ArrayList<>();

        if (path.isEmpty()) {
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
                    walk(path);
                    return;
                }

                open.remove(current);
                closed.add(current);

                ArrayList<Node> neighbors = current.getNeighbors();

                processNeighbors(neighbors, tile, open, closed, current);
            }
        }
    }

    /**
     * Makes this walk a given path step by step
     *
     * @param path The path to be taken
     */
    public void walk(List path) {
        ListIterator iterator = path.listIterator();

        while (!path.isEmpty()) {
            Node node = (Node) iterator.next();
            Tile tile = new Tile(node.row, node.column);
            Position position = tile.toPosition();
            this.position = position;
            this.position.y = terrains.Map.getHeightOfMap(this.position.x, this.position.z);
            this.tilePosition = Tile.positionToTile(this.position);
            // @TODO: implement smooth walking

            MiniMap.mark(this.position);
            MiniMap.lookForChanges();
            RawMap.lookForChanges();
            iterator.remove();
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
