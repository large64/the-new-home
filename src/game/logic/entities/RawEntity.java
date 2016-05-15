package game.logic.entities;

import game.graphics.entities.Type;
import game.logic.entities.buildings.RawBuilding;
import game.logic.entities.units.RawUnit;
import game.logic.toolbox.Side;
import game.logic.toolbox.map.Position;
import game.logic.toolbox.map.Tile;

import java.util.ArrayList;
import java.util.List;

public abstract class RawEntity {
    private static final int DEFAULT_HEALTH = 100;

    protected static int counter = 0;
    public boolean isMarkedForDeletion = false;
    protected Tile tilePosition;
    protected Position position;
    protected float rotation;
    protected float health;
    protected Side side;
    private boolean isBeingAttacked = false;
    private boolean isBeingHealed = false;
    private boolean isSelected = false;
    private String id;
    private RawEntity approachingEntity;

    public RawEntity() {
    }

    public RawEntity(Position position) {
        counter++;
        this.position = position;
        this.rotation = 0;
        this.tilePosition = Tile.positionToTile(position);
        this.health = DEFAULT_HEALTH;
        RawMap.lookForChanges();
        approachingEntity = null;
    }

    protected boolean isNextToAnEntity(RawEntity rawEntity) {
        int currentTileX = getTilePosition().getColumn();
        int currentTileY = getTilePosition().getRow();

        if (rawEntity instanceof RawBuilding) {
            List<Tile> tiles = ((RawBuilding) rawEntity).getExtentPositions();
            for (Tile tile : tiles) {
                int entityTileX = tile.getColumn();
                int entityTileY = tile.getRow();

                int xOffset = Math.abs(currentTileX - entityTileX);
                int yOffset = Math.abs(currentTileY - entityTileY);

                if (xOffset == 1 && yOffset == 1) {
                    return true;
                }
            }
        } else {
            Tile rawEntityTile = rawEntity.getTilePosition();

            int rowOffset = Math.abs(rawEntityTile.getRow() - this.tilePosition.getRow());
            int columnOffset = Math.abs(rawEntityTile.getColumn() - this.tilePosition.getColumn());

            List<Integer> validOffsets = new ArrayList<>();
            validOffsets.add(0);
            validOffsets.add(-1);
            validOffsets.add(1);

            return (validOffsets.contains(rowOffset) && validOffsets.contains(columnOffset));
        }

        return false;
    }

    public int getHealth() {
        return (int) health;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public void changeHealth(float by) {
        if (this.health <= 100) {
            this.health += by;
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

    public boolean isBeingHealed() {
        return isBeingHealed;
    }

    public void setBeingHealed(boolean beingHealed) {
        isBeingHealed = beingHealed;
    }

    public Side getSide() {
        return side;
    }

    public void setSide(Side side) {
        this.side = side;
    }

    public Tile getTilePosition() {
        return tilePosition;
    }

    public void setTilePosition(Tile tilePosition) {
        this.tilePosition = tilePosition;
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

    public void setApproachingEntity(RawEntity approachingEntity, Type approachingEntityType) {
        this.approachingEntity = approachingEntity;
        if (approachingEntityType != null && approachingEntity != null) {
            switch (approachingEntityType) {
                case SOLDIER:
                    this.isBeingAttacked = true;
                    this.isBeingHealed = false;
                    break;
                case HEALER:
                    this.isBeingHealed = true;
                    this.isBeingAttacked = false;
                    break;
                default:
                    this.isBeingHealed = false;
                    this.isBeingAttacked = false;
            }
        } else {
            this.isBeingHealed = false;
            this.isBeingAttacked = false;
        }
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

    public boolean isApproachingEntityAround() {
        try {
            return approachingEntity != null && ((RawUnit) approachingEntity).getDestinationTile().equals(this.tilePosition);
        } catch (NullPointerException ex) {
            return false;
        }
    }
}
