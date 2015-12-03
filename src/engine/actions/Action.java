package engine.actions;

import engine.entities.RawEntity;
import engine.entities.units.Unit;
import engine.toolbox.Tile;

/**
 * Created by large64 on 2015.11.22..
 */
public abstract class Action implements Runnable {
    Unit unit;
    RawEntity rawEntity;
    Tile tile;

    public Action(Unit unit, RawEntity rawEntity) {
        this.unit = unit;
        this.rawEntity = rawEntity;
    }

    public Action(Unit unit, Tile tile) {
        this.unit = unit;
        this.tile = tile;
    }
}
