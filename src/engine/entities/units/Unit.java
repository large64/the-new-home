package engine.entities.units;

import engine.Map;
import engine.actions.ActionType;
import engine.entities.Entity;
import engine.toolbox.Node;
import engine.toolbox.Position;
import main.Game;

import java.util.*;

/**
 * Created by Dénes on 2015. 11. 06..
 */
public class Unit extends Entity {
    public Unit(int row, int column, int health) {
        super(new Position(row, column));
        this.setHealth(health);
    }

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

                if (current.equals(new Node(entity.getPosition().getRow(),
                        entity.getPosition().getColumn()))) {
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

    public void performAction(Entity entity, ActionType action) {
        while (!this.isNextToAnEntity(entity)) {
            this.goTo(entity);
        }
        switch (action) {
            case ATTACK:
                while (entity.getHealth() > 0) {
                    entity.changeHealth(-10);
                    Game.makeTimePass();
                }
                break;
            case HEAL:
                while (entity.getHealth() < 100) {
                    entity.changeHealth(10);
                    Game.makeTimePass();
                }
                break;
        }
    }

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

    @Override
    public String toString() {
        String toReturn = super.toString();
        return toReturn.replaceAll("entity", "unit");
    }
}
