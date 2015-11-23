package engine.entities;

import engine.toolbox.Position;

/**
 * Created by Dénes on 2015. 11. 06..
 */
public abstract class Entity {
    private static final String ID = "entity";
    private static final int DEFAULT_HEALTH = 100;

    private static int counter = 0;
    protected Position position;
    private String id = ID + counter;
    private int health;
    private boolean isBeingAttacked = false;
    private Side side;

    public Entity(Position position) {
        counter++;
        this.position = position;
        this.health = DEFAULT_HEALTH;
        this.side = Side.ENEMY;
    }

    @Override
    public String toString() {
        return (this.id + ": " + this.getPosition().toString() + " " + this.health);
    }

    public Position getPosition() {
        return position;
    }

    public boolean isNextToAnEntity(Entity entity) {
        int entityRow = entity.getPosition().getRow();
        int entityColumn = entity.getPosition().getColumn();

        // offsets of entities relative to each other
        int rowOffset = entityRow - this.position.getRow();
        int columnOffset = entityColumn - this.position.getColumn();

        return (rowOffset >= -1 && rowOffset <= 1 && columnOffset >= -1 && columnOffset <= 1);
    }

    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public void changeHealth(int by) {
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
        if (object instanceof Entity) {
            Entity entity = (Entity) object;

            if (entity.getID().equals(this.getID())) {
                return true;
            }
        }

        return false;
    }

    public String getID() {
        return id;
    }

    public Side getSide() {
        return side;
    }

    public void setSide(boolean side) {
        this.side = side ? Side.FRIEND : Side.ENEMY;
    }
}
