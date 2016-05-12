package game.logic.entities;

import game.graphics.entities.Entity;
import game.logic.entities.units.RawUnit;
import game.logic.toolbox.Side;
import game.logic.toolbox.map.Position;
import game.logic.toolbox.map.Tile;

/**
 * Created by Dénes on 2015. 11. 06..
 */
public abstract class RawEntity {
    private static final int DEFAULT_HEALTH = 100;

    protected static int counter = 0;
    public boolean isMarkedForDeletion = false;
    protected Tile tilePosition;
    protected Position position;
    protected float rotation;
    protected float health;
    protected Side side;
    private Position defaultPosition;
    private boolean isBeingAttacked = false;
    private boolean isBeingHealed = false;
    private Entity entity;
    private boolean isSelected = false;
    private String id;

    public RawEntity() {
    }

    public RawEntity(Position position) {
        counter++;
        this.position = position;
        this.rotation = 0;
        this.tilePosition = Tile.positionToTile(position);
        this.health = DEFAULT_HEALTH;
        this.defaultPosition = new Position(position.x, position.z);
        RawMap.lookForChanges();
        //Scene.addRawEntity(this);
    }

    protected boolean isNextToAnEntity(RawEntity rawEntity) {
        int entityTileRow = rawEntity.getTilePosition().getRow();
        int entityTileColumn = rawEntity.getTilePosition().getColumn();

        // offsets of entities relative to each other
        int rowOffset = entityTileRow - this.tilePosition.getRow();
        int columnOffset = entityTileColumn - this.tilePosition.getColumn();

        return (rowOffset >= -1 && rowOffset <= 1 && columnOffset >= -1 && columnOffset <= 1);
    }

    public int getHealth() {
        return (int) health;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public void changeHealth(float by) {
        this.health += by;

        if (by < 0) {
            this.isBeingAttacked = true;
        } else if (by > 0) {
            this.isBeingHealed = true;
        } else {
            this.isBeingAttacked = false;
            this.isBeingHealed = false;
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

    public void setSide(Side side) {
        this.side = side;
    }

    public void reset() {
        this.tilePosition = Tile.positionToTile(defaultPosition);
        this.setHealth(100);
    }

    public Tile getTilePosition() {
        return tilePosition;
    }

    public void setTilePosition(Tile tilePosition) {
        this.tilePosition = tilePosition;
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

    public void setPosition(Position position) {
        this.position = position;
    }

    public float getRotation() {
        return rotation;
    }

    public void setRotation(float rotation) {
        this.rotation = rotation;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public String getId() {
        if (this.id != null) {
            return this.id;
        }

        return "";
    }

    public String toJSON() {
        String JSON = "";
        JSON += "{";
        JSON += "\"id\":" + "\"" + getId() + "\",";
        if (this instanceof RawUnit) {
            RawUnit rawUnit = (RawUnit) this;
            JSON += "\"path\":" + rawUnit.getPath() + ",";
        }
        JSON += "\"tilePosition\":" + "\"" + getTilePosition() + "\",";
        JSON += "\"rotation\":" + "\"" + getRotation() + "\",";
        JSON += "\"health\":" + "\"" + getHealth() + "\",";
        JSON += "\"side\":" + "\"" + getSide() + "\",";
        JSON += "\"isSelected\":" + isSelected();
        JSON += "}";
        return JSON;
    }
}
