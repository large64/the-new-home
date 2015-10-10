package entities;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.util.vector.Vector3f;

/**
 * Created by large64 on 9/15/15.
 */
public class Camera {
    private static final Vector3f DEFAULT_POSITION = new Vector3f(212.70139f, 14.500017f, -275.4985f);
    private Vector3f position = DEFAULT_POSITION;
    private float pitch;
    private float yaw;
    private float roll;

    public Camera() {
    }

    public void move() {
        pitch += -Mouse.getDY() / 2f;
        yaw += Mouse.getDX() / 2f;
        boolean move = false;

        if (Keyboard.isKeyDown(Keyboard.KEY_W)) {
            this.position.z -= 0.1f;
            move = true;
        }

        if (Keyboard.isKeyDown(Keyboard.KEY_S)) {
            this.position.z += 0.1f;
            move = true;
        }

        if (Keyboard.isKeyDown(Keyboard.KEY_A)) {
            this.position.x -= 0.1f;
            move = true;
        }

        if (Keyboard.isKeyDown(Keyboard.KEY_D)) {
            this.position.x += 0.1f;
            move = true;
        }

        if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
            this.position.y -= 0.1f;
            move = true;
        }

        if (Keyboard.isKeyDown(Keyboard.KEY_SPACE)) {
            this.position.y += 0.1f;
            move = true;
        }

        if (move) {
            //System.out.println("Camera position: [" + this.position.x + ", " + this.position.y + ", " + this.position.z + "]");
        }
        move = false;
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
