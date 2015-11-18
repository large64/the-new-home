package engine.entities.units;

import engine.Map;
import engine.entities.Entity;
import engine.toolbox.Position;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by Dénes on 2015. 11. 06..
 */
public class Unit extends Entity {
    private enum Step {
        UP, DOWN, LEFT, RIGHT, UP_LEFT, UP_RIGHT, DOWN_LEFT, DOWN_RIGHT
    }

    private enum HorizontalBypassStrategy {
        LEFT, RIGHT
    }

    private enum VerticalBypassStrategy {
        UP, DOWN
    }

    private class PathPosition {
        public int row;
        public int column;
        public int counter;

        public PathPosition(int row, int column, int counter) {
            this.column = column;
            this.row = row;
            this.counter = counter;
        }

        @Override
        public boolean equals(Object object) {
            if (object != null) {
                PathPosition position = (PathPosition) object;
                if (position.column == this.column && position.row == this.row) {
                    return true;
                }
            }
            return false;
        }

        @Override
        public String toString() {
            return "[" + row + ", " + column + "] " + counter;
        }
    }
    private Step nextStep;
    private HorizontalBypassStrategy horizontalBypassStrategy;
    public static LinkedList<PathPosition> path = new LinkedList();
    int counter = 0;

    public Unit(int row, int column) {
        super(new Position(row, column));
        this.horizontalBypassStrategy = HorizontalBypassStrategy.LEFT;
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
        if (path.isEmpty()) {
            path.add(new PathPosition(
                    entity.getPosition().getRow(),
                    entity.getPosition().getColumn(),
                    0
            ));
        }

        for (int i = 0; i < path.size(); ++i) {
            PathPosition pathElement = path.get(i);
            LinkedList<PathPosition> neighbors = new LinkedList<>();

            Position temporaryPosition = new Position(pathElement.row, pathElement.column);
            if (temporaryPosition.isBlocked()) {
                path.remove(i);
            }
            else if (path.contains(pathElement)) {
                for (PathPosition aPath : path) {
                    if (aPath.equals(pathElement) && aPath.counter >= pathElement.counter) {
                        path.remove(i);
                    }
                }
            }
            else {
                path.add(pathElement);
            }

            neighbors.add(new PathPosition(
                    pathElement.row,
                    pathElement.column - 1,
                    i + 1
            ));

            neighbors.add(new PathPosition(
                    pathElement.row + 1,
                    pathElement.column,
                    i + 1
            ));

            neighbors.add(new PathPosition(
                    pathElement.row,
                    pathElement.column + 1,
                    i + 1
            ));

            neighbors.add(new PathPosition(
                    pathElement.row - 1,
                    pathElement.column,
                    i + 1
            ));

            for (int j = 0; j < neighbors.size(); j++) {
                PathPosition neighborElement = neighbors.get(j);
                Position neighborPosition = new Position(neighborElement.row, neighborElement.column);
                if (neighborPosition.isBlocked()) {
                    path.remove(neighborElement);
                }
                else if (path.contains(neighborElement)) {
                    for (PathPosition aPath : path) {
                        if (aPath.equals(neighborElement) && aPath.counter >= neighborElement.counter) {
                            path.remove(i);
                        }
                    }
                }
                else {
                    path.add(neighborElement);
                }
            }
            //Map.lookForChanges();
        }

        /*int entityRow = entity.getPosition().getRow();
        int entityColumn = entity.getPosition().getColumn();

        // x offset of entities relative to each other
        int rowOffset = entityRow - this.position.getRow();
        int columnOffset = entityColumn - this.position.getColumn();

        if (!this.isNextToAnEntity(entity)) {
            if (rowOffset == -1 && columnOffset == -1) {
                this.nextStep = Step.UP;
            } else if (rowOffset == 1 && columnOffset == 1) {
                this.nextStep = Step.DOWN;
            } else if (rowOffset == 1 && columnOffset == -1) {
                this.nextStep = Step.LEFT;
            } else if (rowOffset == -1 && columnOffset == 1) {
                this.nextStep = Step.RIGHT;
            } else if (rowOffset >= 1 && columnOffset <= -1) {
                this.nextStep = Step.DOWN_LEFT;
            } else if (rowOffset >= 1 && columnOffset >= 1) {
                this.nextStep = Step.DOWN_RIGHT;
            } else if (rowOffset <= -1 && columnOffset >= 1) {
                this.nextStep = Step.UP_RIGHT;
            } else if (rowOffset <= -1 && columnOffset <= -1) {
                this.nextStep = Step.UP_LEFT;
            } else if (rowOffset >= 1 && columnOffset == 0) {
                this.nextStep = Step.DOWN;
            } else if (rowOffset == 0 && columnOffset >= 1) {
                this.nextStep = Step.RIGHT;
            } else if (rowOffset <= -1 && columnOffset == 0) {
                this.nextStep = Step.UP;
            } else if (rowOffset == 0 && columnOffset <= -1) {
                this.nextStep = Step.LEFT;
            }

            step();
        }*/
    }

    public void attack(Entity entity) {
        if (this.isNextToAnEntity(entity)) {
            if (entity.getHealth() > 0) {
                entity.setHealth(-10);
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

    public void step() {
        if (this.nextStep != null) {
            int currentPositionRow = this.getPosition().getRow();
            int currentPositionColumn = this.getPosition().getColumn();
            Position nextPosition;

            switch (this.nextStep) {
                case UP:
                    if (Map.isPositionFree(
                            new Position(currentPositionRow - 1, currentPositionColumn)
                    )) {
                        this.stepUp();
                    }
                    else {
                        lookAround(currentPositionRow, currentPositionColumn);
                    }
                    break;
                case DOWN:
                    if (Map.isPositionFree(
                            new Position(this.getPosition().getRow() + 1, this.getPosition().getColumn())
                    )) {
                        this.stepDown();
                    }
                    break;
                case LEFT:
                    if (Map.isPositionFree(
                            new Position(this.getPosition().getRow(), this.getPosition().getColumn() - 1)
                    )) {
                        this.stepLeft();
                    }
                    break;
                case RIGHT:
                    if (Map.isPositionFree(
                            new Position(this.getPosition().getRow(), this.getPosition().getColumn() + 1)
                    )) {
                        this.stepRight();
                    }
                    break;
                case DOWN_LEFT:
                    if (Map.isPositionFree(
                            new Position(this.getPosition().getRow() + 1, this.getPosition().getColumn() - 1)
                    )) {
                        this.stepDown();
                        this.stepLeft();
                    }
                    break;
                case DOWN_RIGHT:
                    nextPosition = new Position(currentPositionRow + 1, currentPositionColumn + 1);
                    if (Map.isPositionFree(nextPosition)) {
                        this.stepDown();
                        this.stepRight();
                    }
                    else {
                        this.stepRight();
                    }
                    break;
                case UP_RIGHT:
                    if (Map.isPositionFree(
                            new Position(this.getPosition().getRow() - 1, this.getPosition().getColumn() + 1)
                    )) {
                        this.stepUp();
                        this.stepRight();
                    }
                    else {
                        this.stepUp();
                    }
                    break;
                case UP_LEFT:
                    if (Map.isPositionFree(
                            new Position(currentPositionRow - 1, currentPositionColumn - 1)
                    )) {
                        this.stepUp();
                        this.stepLeft();
                    }
                    break;
            }
        }
    }

    private void switchStrategy() {
        if (this.horizontalBypassStrategy == HorizontalBypassStrategy.LEFT){
            if (this.isOnTheLeftEdge()) {
                this.horizontalBypassStrategy = HorizontalBypassStrategy.RIGHT;
            }
            else {
                stepLeft();
            }
        }
        else {
            if (this.isOnTheRightEdge()) {
                this.horizontalBypassStrategy = HorizontalBypassStrategy.LEFT;
            }
            else {
                stepRight();
            }
        }
    }
    private void lookAround(int positionRow, int positionColumn) {
        switch (this.nextStep) {
            case UP:
                if (Map.isPositionFree(new Position(positionRow - 1, positionColumn -1))) {
                    this.stepUp();
                    this.stepLeft();
                }
                else if (Map.isPositionFree(new Position(positionRow - 1, positionColumn + 1))) {
                    this.stepUp();
                    this.stepRight();
                }
                else {
                    switchStrategy();
                }
                break;
            case UP_LEFT:
                if (Map.isPositionFree(new Position(positionRow, positionColumn - 1))) {
                    this.stepLeft();
                }
                else if (Map.isPositionFree(new Position(positionRow - 1, positionColumn))) {
                    this.stepUp();
                }
                else {
                    switchStrategy();
                }
                break;
        }
    }
}
