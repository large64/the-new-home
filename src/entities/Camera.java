package entities;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector3f;
import renderEngine.DisplayManager;
import toolbox.Maths;
import toolbox.MousePicker;

/**
 * Created by large64 on 9/15/15.
 */
public class Camera {
    private static final float DEFAULT_PITCH = 36;
    private static final Vector3f DEFAULT_POSITION = new Vector3f(212.70139f, 29f, -275.4985f);
    private static final float CURSOR_MARGIN = 20;

    private Vector3f position = DEFAULT_POSITION;
    private float pitch = DEFAULT_PITCH;
    private float yaw = 0;
    private float roll = 0;
    private float zoom = 0;

    private float displayHeight = Display.getHeight();
    private float displayWidth = Display.getWidth();
    private float distanceFromGround = 29f;

    public Camera() {
    }

    public void move() {
        boolean move = false;

        if (Mouse.getX() >= displayWidth - CURSOR_MARGIN) {
                this.position.x += 1f;
        }
        else if (Mouse.getX() <= CURSOR_MARGIN) {
            this.position.x -= 1f;
        }
        else if (Mouse.getY() >= displayHeight - CURSOR_MARGIN) {
            this.position.z -= 1f;
        }
        else if (Mouse.getY() <= CURSOR_MARGIN) {
            this.position.z += 1f;
        }

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
            System.out.println("Camera position: [" + this.position.x + ", " + this.position.y + ", " + this.position.z + "]");
            System.out.println("Pitch: " + this.pitch + ", Yaw: " + this.yaw);
            System.out.println("MouseX: " + Mouse.getDX() + ", MouseY: " + Mouse.getDY());
        }

        calculateZoom();
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

    public void setZoom(float zoom) {
        this.zoom = zoom;
    }

    private void calculateZoom() {
        float zoomLevel = Mouse.getDWheel() * 0.008f;
        zoom -= zoomLevel;

        this.position.y -= zoomLevel;
        this.position.z -= zoomLevel;
    }
}
