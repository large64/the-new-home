package engine.toolbox;

/**
 * Created by Dénes on 2015. 11. 07..
 */
public class Position {
    // x points from left to right
    // y points from bottom to top
    // z points from camera into the distance
    // now we use a flat terrain to represent the position of an entity on the map, so y is always going to be 0
    private int x;
    private int y;
    private int z;

    public Position() {
        this.x = 0;
        this.y = 0;
        this.z = 0;
    }

    public Position(int x, int z) {
        this.x = x;
        this.z = z;
        this.y = 0;
    }

    public void increase(int byX, int byZ) {
        this.x += byX;
        this.z += byZ;
    }

    // Use this function to make code more readable for humans
    public void decrease(int byX, int byZ) {
        this.x -= byX;
        this.z -= byZ;
    }

    public void set(int x, int y, int z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    @Override
    public String toString() {
        return "[" + this.x + ", " + this.y + ", " + this.z + "]";
    }

    public int getX() {
        return this.x;
    }

    public int getZ() {
        return this.z;
    }

    public int convertToMatrixPosition(int size) {
        return ((this.x) * size) + this.z;
    }
}
