package engine.entities;

import engine.toolbox.Position;
import engine.toolbox.Tile;

/**
 * Created by Dénes on 2015. 11. 06..
 */
public abstract class RawEntity {
    private static final String ID = "entity";
    private static final int DEFAULT_HEALTH = 100;

    protected static int counter = 0;
    protected Position position;
    protected Tile tilePosition;
    private Position defaultPosition;
    private String id = ID + counter;
    private int health;
    private boolean isBeingAttacked = false;
    private boolean isBeingHealed = false;
    private Side side;

    public RawEntity(Position position) {
        counter++;
        this.tilePosition = Tile.positionToTile(position);
        this.position = (this.tilePosition).toPosition();
        this.health = DEFAULT_HEALTH;
        this.side = Side.ENEMY;
        this.defaultPosition = position;
    }

    @Override
    public String toString() {
        return (this.id + ": " + this.getPosition().toString() + " " + this.health + " " + side);
    }

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    public boolean isNextToAnEntity(RawEntity rawEntity) {
        int entityRow = rawEntity.getPosition().getRow();
        int entityColumn = rawEntity.getPosition().getColumn();

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
        else if (by > 0) {
            this.isBeingHealed = true;
        }
    }

    public boolean isAlive() {
        return (this.health > 0);
    }

    public boolean isBeingAttacked() {
        return isBeingAttacked;
    }

    public boolean isBeingHealed() {
        return isBeingHealed;
    }

    public void setBeingAttacked(boolean beingAttacked) {
        this.isBeingAttacked = beingAttacked;
    }

    public void setBeingHealed(boolean isBeingHealed) {
        this.isBeingHealed = isBeingHealed;
    }

    @Override
    public boolean equals(Object object) {
        if (object instanceof RawEntity) {
            RawEntity rawEntity = (RawEntity) object;

            if (rawEntity.getID().equals(this.getID())) {
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

    public void reset() {
        this.position = defaultPosition;
    }

    public Tile getTilePosition() {
        return tilePosition;
    }
}
