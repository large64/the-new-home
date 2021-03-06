package game.graphics.entities;

import game.graphics.models.TexturedModel;
import game.graphics.windowparts.Scene;
import game.logic.entities.RawEntity;
import game.logic.entities.RawNeutral;
import game.logic.entities.buildings.RawBarrack;
import game.logic.entities.buildings.RawHome;
import game.logic.entities.buildings.RawHospital;
import game.logic.entities.units.RawHealer;
import game.logic.entities.units.RawScientist;
import game.logic.entities.units.RawSoldier;
import game.logic.toolbox.Side;
import game.logic.toolbox.map.Position;
import org.lwjgl.util.vector.Vector3f;

public class Entity {
    private TexturedModel model;
    private Vector3f position;
    private float scale;
    private RawEntity rawEntity;
    private float rotation;
    private float rotX, rotZ, rotY;

    protected Entity() {
    }

    protected Entity(TexturedModel model, Vector3f position, float rotX, float rotY, float rotZ, float scale, Type type, Side side) {
        this.model = model;
        this.rotX = rotX;
        this.rotY = rotY;
        this.rotZ = rotZ;
        this.scale = scale;

        RawEntity rawEntity = null;
        int x = (int) position.getX();
        int z = (int) position.getZ();

        switch (type) {
            case HEALER:
                rawEntity = new RawHealer(x, z, side);
                break;
            case SOLDIER:
                rawEntity = new RawSoldier(x, z, side);
                break;
            case SCIENTIST:
                rawEntity = new RawScientist(x, z, side);
                break;
            case NEUTRAL:
                rawEntity = new RawNeutral(new Position(x, z));
                break;
            case HOME:
                rawEntity = new RawHome(x, z, 100, side);
                break;
            case HOSPITAL:
                rawEntity = new RawHospital(x, z, 100, side);
                break;
            case BARRACK:
                rawEntity = new RawBarrack(x, z, 100, side);
                break;
        }

        this.rawEntity = rawEntity;
        Scene.addEntity(this);
        this.position = this.rawEntity.getTilePosition().toPosition();
        this.position.y = Scene.getMainMap().getHeightOfMap(position.getX(), position.getZ());
    }

    public Entity(TexturedModel model, Vector3f position, float rotX, float rotY, float rotZ, float scale) {
        this.model = model;
        this.rotX = rotX;
        this.rotY = rotY;
        this.rotZ = rotZ;
        this.scale = scale;

        int x = (int) position.getX();
        int z = (int) position.getZ();

        this.rawEntity = new RawNeutral(new Position(x, z));
        this.position = position;

        Scene.addEntity(this);
    }

    public void refreshPosition() {
        this.position = getRawEntity().getPosition();
        this.rotY = getRawEntity().getRotation();
        this.position.y = Scene.getMainMap().getHeightOfMap(this.position.x, this.position.z);
    }

    public RawEntity getRawEntity() {
        return rawEntity;
    }

    public void setRawEntity(RawEntity rawEntity) {
        this.rawEntity = rawEntity;
    }

    public TexturedModel getModel() {
        return model;
    }

    protected void setModel(TexturedModel model) {
        this.model = model;
    }

    public Vector3f getPosition() {
        return position;
    }

    public void setPosition(Vector3f position) {
        this.position = position;
    }

    public float getRotX() {
        return rotX;
    }

    public float getRotY() {
        return rotY;
    }

    public float getRotZ() {
        return rotZ;
    }

    public float getScale() {
        return scale;
    }

    protected void setScale(float scale) {
        this.scale = scale;
    }
}
