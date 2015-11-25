package entities.units;

import engine.entities.units.RawSoldier;
import engine.toolbox.Position;
import models.TexturedModel;
import org.lwjgl.util.vector.Vector3f;
import terrains.Map;

/**
 * Created by large64 on 10/10/15.
 */
public class Soldier extends Unit {
    public Soldier(TexturedModel model, Vector3f position, float rotX, float rotY, float rotZ, float scale) {
        super(model, position, rotX, rotY, rotZ, scale);
    }

    public Soldier(TexturedModel model, RawSoldier rawSoldier, Map map, float rotX, float rotY, float rotZ, float scale) {
        super(model, rawSoldier, rotX, rotY, rotZ, scale);
        refreshPosition(getRawEntity().getPosition(), map);
    }

    private void refreshPosition(Position position, Map map) {
        // @TODO: important! Make these functions available for all units!
        float x = position.getRow();
        float z = position.getColumn();
        this.position = new Vector3f(x, map.getHeightOfMap(x, z), z);
    }
}
