package engine.entities.units;

import engine.Map;
import engine.entities.Entity;
import engine.toolbox.Position;

import java.util.List;

/**
 * Created by Dénes on 2015. 11. 06..
 */
public class Unit extends Entity {
    private enum Step {
        UP, DOWN, LEFT, RIGHT, UP_LEFT, UP_RIGHT, DOWN_LEFT, DOWN_RIGHT
    }

    private Step nextStep;

    public Unit(int row, int column) {
        super(new Position(row, column));
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
        int entityRow = entity.getPosition().getRow();
        int entityColumn = entity.getPosition().getColumn();

        // x offset of entities relative to each other
        int rowOffset = entityRow - this.position.getRow();
        int columnOffset = entityColumn - this.position.getColumn();

        //System.out.println(this.isNextToAnEntity(entity));

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
        }
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
            switch (this.nextStep) {
                case UP:
                    if (Map.isPositionFree(
                            new Position(this.getPosition().getRow() - 1, this.getPosition().getColumn())
                    )) {
                        this.stepUp();
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
                    if (Map.isPositionFree(
                            new Position(this.getPosition().getRow() + 1, this.getPosition().getColumn() + 1)
                    )) {
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
                    break;
                case UP_LEFT:
                    if (Map.isPositionFree(
                            new Position(this.getPosition().getRow() - 1, this.getPosition().getColumn() - 1)
                    )) {
                        this.stepUp();
                        this.stepLeft();
                    }
                    break;
            }
        }
    }
}
