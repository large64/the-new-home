package engine.entities.units;

import engine.Map;
import engine.actions.ActionType;
import engine.entities.Entity;
import engine.exceptions.ImproperActionException;
import engine.toolbox.Node;
import engine.toolbox.Position;
import main.Game;

import java.util.*;

/**
 * Created by Dénes on 2015. 11. 06..
 */
public class Unit extends Entity {
    public Unit(int row, int column, int health, boolean side) {
        super(new Position(row, column));

        this.setHealth(health);
        this.setSide(side);
    }

    /**
     * Helps this to perform an ActionType on entity.
     *
     * @param entity The entity on which the action will be performed.
     * @param action The action to be performed on entity.
     * @throws ImproperActionException
     */
    public void performAction(Entity entity, ActionType action) throws ImproperActionException {
        switch (action) {
            case ATTACK:
                if (this.getSide() != entity.getSide()) {
                    while (!this.isNextToAnEntity(entity)) {
                        this.goTo(entity);
                    }

                    while (entity.getHealth() > 0) {
                        entity.changeHealth(-10);
                        Game.makeTimePass();
                    }
                } else {
                    throw new ImproperActionException("Will not attack friendly entity.");
                }
                break;

            case HEAL:
                if (this.getSide() == entity.getSide()) {
                    while (!this.isNextToAnEntity(entity)) {
                        this.goTo(entity);
                    }

                    while (entity.getHealth() < 100) {
                        entity.changeHealth(10);
                        Game.makeTimePass();
                    }
                } else {
                    throw new ImproperActionException("Will not heal hostile entity.");
                }
                break;
        }
    }

    /**
     * Finds a way to entity (if there is) and approaches it. It is an implementation of A* algorithm.
     *
     * @param entity The entity to be approached.
     */
    public void goTo(Entity entity) {
        List<Node> path = new ArrayList<>();
        if (path.isEmpty()) {
            List<Node> open = new ArrayList<>();
            Queue<Node> closed = new LinkedList<>();

            Node startPosition = new Node(this.getPosition().getRow(), this.getPosition().getColumn());
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

                if (current.equals(new Node(entity.getPosition().getRow(), entity.getPosition().getColumn()))) {
                    while (current != null) {
                        path.add(current);
                        current = current.parent;
                    }
                    Collections.reverse(path);
                    path.remove(path.size() - 1);
                    System.out.println(path);

                    walk(path);
                    return;
                }

                open.remove(current);
                closed.add(current);

                ArrayList<Node> neighbors = current.getNeighbors();

                processNeighbors(neighbors, entity, open, closed, current);
            }
        }
    }

    /**
     * Makes this walk a given path step by step.
     * @param path The path to be taken.
     */
    private void walk(List path) {
        ListIterator iterator = path.listIterator();

        while (!path.isEmpty()) {
            Node node = (Node) iterator.next();
            this.position = new Position(node.row, node.column);

            Map.mark(position.convertToMatrixPosition());
            iterator.remove();
            Game.makeTimePass();
        }
    }

    /**
     * Processes neighbors of a selected node in a graph. This is a part of A* algorithm.
     * @param neighbors The neighbors of the selected node to be processed.
     * @param entity The destination entity, which we are heading to.
     * @param open The list of currently open (not yet processed) nodes.
     * @param closed The list of already processed nodes.
     * @param current The current node which is being processed. Taken from the open list, and put into closed when
     *                processed.
     */
    private void processNeighbors(List<Node> neighbors, Entity entity, List open, Queue<Node> closed, Node current) {
        for (Node neighbor : neighbors) {
            Position neighborPosition = new Position(neighbor.row, neighbor.column);
            boolean isDestination = false;

            if (neighborPosition.equals(entity.getPosition())) {
                isDestination = true;
            }

            if (closed.contains(neighbor) || neighborPosition.isBlocked(isDestination)) {
                continue;
            }

            int gCost = current.gCost + neighbor.getgCost(current);
            boolean isgCostBest = false;

            if (!open.contains(neighbor)) {
                isgCostBest = true;
                neighbor.hCost = Node.gethCost(neighborPosition, entity.getPosition());
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

    /**
     * Provides a string representation of this.
     * @return String representation of this.
     */
    @Override
    public String toString() {
        String toReturn = super.toString();
        return toReturn.replaceAll("entity", "unit");
    }
}
