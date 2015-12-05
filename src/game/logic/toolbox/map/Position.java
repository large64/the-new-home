package game.logic.toolbox.map;

import org.lwjgl.util.vector.Vector3f;

/**
 * Created by Dénes on 2015. 11. 07..
 */
public class Position extends Vector3f {
    public Position(float x, float z) {
        this.x = x;
        this.z = z;
        this.y = 0;
    }

    @Override
    public String toString() {
        return "[" + this.x + "," + this.z + "]";
    }

    public int getRow() {
        return (int) this.x;
    }

    public int getColumn() {
        return (int) this.z;
    }

    @Override
    public boolean equals(Object object) {
        if (object instanceof Position) {
            Position position = (Position) object;
            if (position.x == this.x && position.z == this.z) {
                return true;
            }
        }
        return false;
    }
}
