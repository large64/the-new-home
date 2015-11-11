package engine.entities;

import engine.Map;
import engine.toolbox.Position;

/**
 * Created by Dénes on 2015. 11. 06..
 */
public class Entity {
    private static final String ID = "entity";
    private static int counter = 0;
    private String id = ID + counter;
    protected Position position;
    private static final int DEFAULT_HEALTH = 100;
    private int health;
    private boolean isBeingAttacked = false;

    public Entity() {
        counter++;
        this.position = new Position();
        this.health = DEFAULT_HEALTH;
    }

    public Entity(Position position) {
        counter++;
        this.position = position;
        this.health = DEFAULT_HEALTH;
    }

    public Position getPosition() {
        return position;
    }

    public String getID() {
        return id;
    }

    @Override
    public String toString() {
        return (this.id + ": " + this.getPosition().toString() + " " + this.health);
    }

    public boolean isOnTheEdge() {
        return this.position.getRow() == 0 || this.position.getColumn() == 0
                || this.position.getRow() == (Map.getRowNumber() - 1)
                || this.position.getColumn() == (Map.getRowNumber() - 1);
    }

    public boolean isNextToAnEntity(Entity entity) {
        int entityRow = entity.getPosition().getRow();
        int entityColumn = entity.getPosition().getColumn();

        // offsets of entities relative to each other
        int rowOffset = entityRow - this.position.getRow();
        int columnOffset = entityColumn - this.position.getColumn();

        return (((rowOffset == 1) && (columnOffset == 0)) ||
                ((columnOffset == 1) && (rowOffset == 0)) ||
                ((rowOffset == -1) && (columnOffset == 0)) ||
                ((columnOffset == -1) && (rowOffset == 0)));
    }

    public int getHealth() {
        return health;
    }

    public void setHealth(int by) {
        this.health += by;
        if (by < 0) {
            this.isBeingAttacked = true;
        }
    }

    public boolean isAlive() {
        return (this.health > 0);
    }

    public boolean isBeingAttacked() {
        return isBeingAttacked;
    }

    public void setBeingAttacked(boolean beingAttacked) {
        isBeingAttacked = beingAttacked;
    }

    @Override
    public boolean equals(Object object) {
        Entity entity = (Entity) object;
        if (entity.getID().equals(this.getID())) {
            return true;
        }

        return false;
    }
}
