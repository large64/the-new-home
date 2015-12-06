package game.logic.entities;

import game.logic.toolbox.Side;
import game.logic.toolbox.map.Position;
import game.logic.toolbox.map.Tile;

/**
 * Created by Dénes on 2015. 11. 06..
 */
public abstract class RawEntity {
    private static final int DEFAULT_HEALTH = 100;

    protected static int counter = 0;
    protected Position position;
    protected Tile tilePosition;
    private Position defaultPosition;
    protected int health;
    private boolean isBeingAttacked = false;
    private boolean isBeingHealed = false;
    protected Side side;

    public RawEntity(Position position) {
        counter++;
        this.tilePosition = Tile.positionToTile(position);
        this.position = (this.tilePosition).toPosition();
        this.health = DEFAULT_HEALTH;
        this.side = Side.ENEMY;
        this.defaultPosition = position;
    }

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    public boolean isNextToAnEntity(RawEntity rawEntity) {
        int entityTileRow = rawEntity.getTilePosition().getRow();
        int entityTileColumn = rawEntity.getTilePosition().getColumn();

        // offsets of entities relative to each other
        int rowOffset = entityTileRow - this.tilePosition.getRow();
        int columnOffset = entityTileColumn - this.tilePosition.getColumn();

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
