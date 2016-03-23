package game.logic.entities;

import game.graphics.entities.Entity;
import game.graphics.renderers.MasterRenderer;
import game.logic.toolbox.Side;
import game.logic.toolbox.map.Position;
import game.logic.toolbox.map.Tile;

/**
 * Created by Dénes on 2015. 11. 06..
 */
public abstract class RawEntity {
    private static final int DEFAULT_HEALTH = 100;

    protected static int counter = 0;
    protected Tile tilePosition;
    protected Position position;
    protected float rotation;
    private Position defaultPosition;
    protected int health;
    private boolean isBeingAttacked = false;
    private boolean isBeingHealed = false;
    protected Side side;
    private Entity entity;

    public RawEntity(Position position) {
        counter++;
        this.position = position;
        this.rotation = 0;
        this.tilePosition = Tile.positionToTile(position);
        this.health = DEFAULT_HEALTH;
        this.defaultPosition = new Position(position.x, position.z);

        MasterRenderer.addRawEntity(this);
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

    public Side getSide() {
        return side;
    }

    public void setSide(boolean side) {
        this.side = side ? Side.FRIEND : Side.ENEMY;
    }

    public void reset() {
        this.tilePosition = Tile.positionToTile(defaultPosition);
        this.setHealth(100);
    }

    public Tile getTilePosition() {
        return tilePosition;
    }

    public Entity getEntity() {
        return entity;
    }

    public void setEntity(Entity entity) {
        this.entity = entity;
    }

    public Position getPosition() {
        return position;
    }

    public float getRotation() {
        return rotation;
    }
}
