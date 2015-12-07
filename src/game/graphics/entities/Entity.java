package game.graphics.entities;

import game.logic.entities.Neutral;
import game.logic.entities.RawEntity;
import game.graphics.models.TexturedModel;
import game.logic.entities.units.RawHealer;
import game.logic.toolbox.map.Position;
import org.lwjgl.util.vector.Vector3f;

/**
 * Created by large64 on 10/10/15.
 */
public class Entity {
    protected TexturedModel model;
    protected Vector3f position;
    protected float rotX, rotY, rotZ;
    protected float scale;
    protected int textureIndex = 0;
    protected float health = 100;
    private RawEntity rawEntity;
    private boolean isSelected = false;

    public Entity() {}

    public Entity(TexturedModel model, Vector3f position, float rotX, float rotY, float rotZ, float scale) {
        this.model = model;
        this.rawEntity = new Neutral(new Position(position.x, position.z));
        this.position = this.rawEntity.getTilePosition().toPosition();
        this.rotX = rotX;
        this.rotY = rotY;
        this.rotZ = rotZ;
        this.scale = scale;
    }

    public float getTextureXOffset() {
        int column = textureIndex % model.getTexture().getNumberOfRows();
        return (float) column / (float) model.getTexture().getNumberOfRows();
    }

    public float getTextureYOffset() {
        int row = textureIndex / model.getTexture().getNumberOfRows();
        return (float) row / (float) model.getTexture().getNumberOfRows();
    }

    public void setRawEntity(RawEntity rawEntity) {
        this.rawEntity = rawEntity;
    }

    public RawEntity getRawEntity() {
        return rawEntity;
    }

    public TexturedModel getModel() {
        return model;
    }

    public void setModel(TexturedModel model) {
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

    public void setRotX(float rotX) {
        this.rotX = rotX;
    }

    public float getRotY() {
        return rotY;
    }

    public void setRotY(float rotY) {
        this.rotY = rotY;
    }

    public float getRotZ() {
        return rotZ;
    }

    public void setRotZ(float rotZ) {
        this.rotZ = rotZ;
    }

    public float getScale() {
        return scale;
    }

    public void setScale(float scale) {
        this.scale = scale;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
}
