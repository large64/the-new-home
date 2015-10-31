package entities;

import engineTester.MainGameLoop;
import org.lwjgl.input.*;
import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector3f;

/**
 * Created by large64 on 9/15/15.
 */
public class Camera {
    private static final int MAX_ZOOM = 5;
    private static final int MAX_BACK_ZOOM = 29;
    private static final float DEFAULT_PITCH = 36;
    // Set default position (y) to be the maximum back zoom level
    private static final Vector3f DEFAULT_POSITION = new Vector3f(212.70139f, (float) MAX_BACK_ZOOM, -275.4985f);
    private static final float CURSOR_MARGIN = 10;

    private Vector3f position = DEFAULT_POSITION;
    private float pitch = DEFAULT_PITCH;
    private float yaw = 0;
    private float roll = 0;

    private float newZoomDistance;

    private float displayHeight = Display.getHeight();
    private float displayWidth = Display.getWidth();

    public static boolean isMouseGrabbed = false;

    public Camera() {
    }

    public void move() {
        if (Mouse.isButtonDown(0)) {
            isMouseGrabbed = true;
            MainGameLoop.menuWrapperPanel.setVisible(false);
        }

        if (Keyboard.isKeyDown(Keyboard.KEY_ESCAPE)) {
            isMouseGrabbed = false;
            MainGameLoop.menuWrapperPanel.setVisible(true);
            // @TODO: focus on menuWrapperPanel after pressing Esc key

        }

        if (isMouseGrabbed) {
            boolean move = false;
            float changePositionBy = (0.6f - calculateMoveDamping(newZoomDistance));

            if (Mouse.getX() >= displayWidth - CURSOR_MARGIN) {
                this.position.x += changePositionBy;
                move = true;
            } else if (Mouse.getX() <= CURSOR_MARGIN) {
                this.position.x -= changePositionBy;
                move = true;
            } else if (Mouse.getY() >= displayHeight - CURSOR_MARGIN) {
                this.position.z -= changePositionBy;
                move = true;
            } else if (Mouse.getY() <= CURSOR_MARGIN) {
                this.position.z += changePositionBy;
                move = true;
            }

        /*if (move) {
            System.out.println("Camera position: [" + this.position.x + ", " + this.position.y + ", " + this.position.z + "]");
            System.out.println("Pitch: " + this.pitch + ", Yaw: " + this.yaw);
            System.out.println("MouseX: " + Mouse.getX() + ", MouseY: " + Mouse.getY());
        }*/

            calculateZoom();
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

    private void calculateZoom() {
        float zoomLevel = Mouse.getDWheel() * 0.008f;
        newZoomDistance = this.position.y - zoomLevel;

        if (newZoomDistance > MAX_ZOOM && newZoomDistance < MAX_BACK_ZOOM) {
            this.position.y -= zoomLevel;
            this.position.z -= zoomLevel;
        }
    }

    private float calculateMoveDamping(float newZoomDistance) {
        // newZoomDistance is between MAX_ZOOM and MAX_BACK_ZOOM
        // create number between 0 and 0.3 to subtract it from the default speed
        // using formula (a + ((X - Xmin) * (b - a)/(Xmax - Xmin)))
        return 0.3f + ((newZoomDistance - 5) * (0 - 0.3f) / (MAX_BACK_ZOOM - MAX_ZOOM));
    }
}
