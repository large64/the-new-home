package engine.entities.units;

import engine.Map;
import engine.entities.Entity;
import engine.toolbox.Node;
import engine.toolbox.Position;

import java.util.*;

/**
 * Created by Dénes on 2015. 11. 06..
 */
public class Unit extends Entity {
    private enum Step {
        UP, DOWN, LEFT, RIGHT, UP_LEFT, UP_RIGHT, DOWN_LEFT, DOWN_RIGHT
    }

    public Unit(int row, int column) {
        super(new Position(row, column));
    }

    public Unit(int row, int column, int health) {
        super(new Position(row, column));
        this.setHealth(health);
    }

    public void stepUp() {
        this.position.increase(-1, 0);
    }

    public void stepDown() {
        this.position.increase(1, 0);
    }

    public void stepLeft() {
        this.position.increase(0, -1);
    }

    public void stepRight() {
        this.position.increase(0, 1);
    }

    public void stepTowards(Entity entity) {
        List<Node> open = new ArrayList<>();
        Queue<Node> closed = new LinkedList<>();

        Node startPosition = new Node(this.getPosition().getRow(), this.getPosition().getColumn());
        open.add(startPosition);

        while (!open.isEmpty()) {
            Node current = open.get(0);
            int min = current.fCost;


            for (int i = 0; i < open.size(); i++) {
                Node innerCurrent = open.get(i);
                if (innerCurrent.fCost < min) {
                    min = innerCurrent.fCost;
                    current = innerCurrent;
                }
            }

            if (current.equals(new Node(entity.getPosition().getRow(),
                    entity.getPosition().getColumn()))) {
                List<Node> result = new ArrayList<>();
                while (current != null) {
                    result.add(current);
                    current = current.parent;
                }
                Collections.reverse(result);
                System.out.println(result);
                return;
            }

            open.remove(current);
            closed.add(current);

            List<Node> neighbors = current.getNeighbors();

            // We got the neighbors
            for (Node neighbor : neighbors) {
                Position neighborPosition = new Position(neighbor.row, neighbor.column);
                boolean isDestination = false;

                if(neighborPosition.equals(entity.getPosition())) {
                    isDestination = true;
                }

                if (closed.contains(neighbor) || neighborPosition.isBlocked(isDestination)) {
                    continue;
                }

                int gCost = current.gCost + neighbor.getgCost(current);
                boolean isgCostBest = false;

                if (!open.contains(neighbor)) {
                    isgCostBest = true;
                    neighbor.hCost = neighbor.gethCost(neighborPosition, entity.getPosition());
                    open.add(neighbor);
                }
                else if (gCost < neighbor.gCost) {
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

    public void attack(Entity entity) {
        if (this.isNextToAnEntity(entity)) {
            if (entity.getHealth() > 0) {
                entity.changeHealth(-10);
            }
        }
        else {
            this.stepTowards(entity);
        }
    }

    /**
     * Checks if the next step is available to make.
     * @param position
     *  the position that "this" is going towards
     * @return
     *  whether there is any other objects on the tile we are moving to
     */
    public boolean isDestinationFree(Position position) {
        List<Entity> entities = Map.getEntities();

        for (int i = 0; i < entities.size(); i++) {
            Entity entity = entities.get(i);
            if (this.isNextToAnEntity(entity) && Map.isPositionFree(position)) {
                return false;
            }
        }

        return true;
    }
}
