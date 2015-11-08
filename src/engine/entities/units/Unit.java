package engine.entities.units;

import engine.entities.Entity;
import engine.toolbox.Position;

/**
 * Created by Dénes on 2015. 11. 06..
 */
public class Unit extends Entity {
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

    public void changeHealth(int by) {
        super.changeHealth(by);
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
                this.stepUp();
            }
            else if (rowOffset == 1 && columnOffset == 1) {
                this.stepDown();
            }
            else if (rowOffset == 1 && columnOffset == -1) {
                this.stepLeft();
            }
            else if (rowOffset == -1 && columnOffset == 1) {
                this.stepRight();
            }
            else if (rowOffset >= 1 && columnOffset <= -1) {
                this.stepDown();
                this.stepLeft();
            }
            else if (rowOffset >= 1 && columnOffset >= 1) {
                this.stepDown();
                this.stepRight();
            }
            else if (rowOffset <= -1 && columnOffset >= 1) {
                this.stepUp();
                this.stepRight();
            }
            else if (rowOffset <= -1 && columnOffset <= -1) {
                this.stepUp();
                this.stepLeft();
            }
            else if (rowOffset >= 1 && columnOffset == 0) {
                this.stepDown();
            }
            else if (rowOffset == 0 && columnOffset >= 1) {
                this.stepRight();
            }
            else if (rowOffset <= -1 && columnOffset == 0) {
                this.stepUp();
            }
            else if (rowOffset == 0 && columnOffset <= -1) {
                this.stepLeft();
            }
        }
    }

    public void attack(Entity entity) {
        if (this.isNextToAnEntity(entity)) {
            entity.changeHealth(10);
        }
        else {
            stepTowards(entity);
        }
    }
}
