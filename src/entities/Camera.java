package entities;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.util.vector.Vector3f;

/**
 * Created by large64 on 9/15/15.
 */
public class Camera {
    private static final Vector3f DEFAULT_POSITION = new Vector3f(50, 20, -50);
    private Vector3f position = DEFAULT_POSITION;
    private float pitch;
    private float yaw;
    private float roll;

    public Camera() {}

    public void move() {
        pitch += -Mouse.getDY() / 2f;
        yaw += Mouse.getDX() / 2f;
    }

    public Vector3f getPosition() {
        return position;
    }

    public float getPitch() {
        return pitch;
    }

    public float getYaw() {
        return yaw;
    }

    public float getRoll() {
        return roll;
    }
}
