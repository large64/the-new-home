package engine.toolbox;

import engine.RawMap;
import org.lwjgl.util.vector.Vector3f;

/**
 * Created by Dénes on 2015. 11. 07..
 */
public class Position extends Vector3f {
    public Position(float x, float z) {
        this.x = (int) x;
        this.z = (int) z;
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

    public int convertToMatrixPosition() {
        return (int) (((this.x) * RawMap.getRowNumber()) + this.z);
    }

    public boolean isBlocked(boolean isDestination) {
        return (
                this.x >= RawMap.getRowNumber()
                        || this.x < 0
                        || this.z >= RawMap.getRowNumber()
                        || this.z < 0
                        || !RawMap.isPositionFree(new Position(x, z), isDestination)
        );
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
