package engine.entities;

import engine.toolbox.Position;

/**
 * Created by Dénes on 2015. 11. 06..
 */
public class Entity {
    private static final String ID = "entity";
    private static int counter = 0;
    private String id = ID + counter;
    protected Position position;
    private int health = 100;

    public Entity() {
        counter++;
        this.position = new Position();
    }

    public Entity(Position position) {
        counter++;
        this.position = position;
    }

    public Position getPosition() {
        return position;
    }

    public String getID() {
        return id;
    }

    @Override
    public String toString() {
        return (this.id + ": " + this.getPosition().toString() + " " + this.health);
    }

    public void changeHealth(int by) {
        this.health += by;
    }
}
