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

    public void attack(Unit unit) {

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
        System.out.println(entity.isOnTheEdge());

        // x offset of entities relative to each other
        int rowOffset = entityRow - this.position.getRow();
        int columnOffset = entityColumn - this.position.getColumn();

        if (!(Math.abs(rowOffset) == 1 || Math.abs(columnOffset) == 1)) {
            if (rowOffset > 1) {
                this.stepDown();
            }
            if (rowOffset < -1) {
                this.stepUp();
            }
            if (columnOffset > 1) {
                this.stepLeft();
            }
            if (columnOffset < -1) {
                this.stepRight();
            }
        }
    }
}
