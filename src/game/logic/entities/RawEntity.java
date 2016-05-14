package game.logic.entities;

import game.logic.entities.buildings.RawBuilding;
import game.logic.entities.units.RawUnit;
import game.logic.toolbox.Side;
import game.logic.toolbox.map.Position;
import game.logic.toolbox.map.Tile;

import java.util.ArrayList;
import java.util.List;

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
    private boolean isSelected = false;
    private String id;
    private RawEntity attacker;

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
        attacker = null;
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
        this.health += by;
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

    public RawEntity getAttacker() {
        return attacker;
    }

    public void setAttacker(RawEntity attacker) {
        this.attacker = attacker;
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

    public boolean isAttackerAround() {
        try {
            return attacker != null && ((RawUnit) attacker).getDestinationTile().equals(this.tilePosition);
        } catch (NullPointerException ex) {
            return false;
        }
    }
}
