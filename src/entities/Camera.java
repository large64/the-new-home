package entities;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.util.vector.Vector3f;

/**
 * Created by large64 on 9/15/15.
 */
public class Camera {
    private static final Vector3f DEFAULT_POSITION = new Vector3f(0, 1, 0);
    private Vector3f position = DEFAULT_POSITION;
    private float pitch;
    private float yaw;
    private float roll;

    public Camera() {}

    public void move() {
        if (Keyboard.isKeyDown(Keyboard.KEY_W)) {
            position.z -= 0.02f;
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_D)) {
            position.x += 0.02f;
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_A)) {
            position.x -= 0.02f;
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_S)) {
            position.z += 0.02f;
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_SPACE)) {
            position.y += 0.02f;
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
            position.y -= 0.02f;
        }

        yaw += Mouse.getDX() / 2f;
        pitch += -Mouse.getDY() / 2f;

        if (Keyboard.isKeyDown(Keyboard.KEY_R)) {
            position.set(DEFAULT_POSITION);
            yaw = 0;
            pitch = 0;
        }
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
