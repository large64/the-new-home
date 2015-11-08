package engine.entities.units;

import engine.entities.Entity;

/**
 * Created by Dénes on 2015. 11. 06..
 */
public class Unit extends Entity {
    public void attack(Unit unit) {
        int entityX = unit.getPosition().getX();
        int entityZ = unit.getPosition().getZ();

        // x offset of entities relative to each other
        int xOffset = Math.abs(entityX - this.position.getX());
        int zOffset = Math.abs(entityZ - this.position.getZ());

        if (xOffset == 1 || zOffset == 1) {
            unit.changeHealth(-10);
        }
    }
}
