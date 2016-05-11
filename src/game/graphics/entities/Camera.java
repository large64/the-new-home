package game.graphics.entities;

import game.graphics.toolbox.GameMode;
import game.graphics.windowparts.Scene;
import game.graphics.windowparts.Window;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

/**
 * Created by large64 on 9/15/15.
 */
public class Camera {
    private static final int MAX_ZOOM = 10;
    private static final int MAX_BACK_ZOOM = 35;
    private static final float DEFAULT_PITCH = 36;
    // Create new final variable for default position to prevent it from changing in runtime
    private static final float DEFAULT_X = 103;
    private static final float DEFAULT_Z = 140;
    // Set default position (y) to be the maximum back zoom level
    private static final Vector3f DEFAULT_POSITION = new Vector3f(DEFAULT_X, (float) MAX_BACK_ZOOM, DEFAULT_Z);
    private static final float MIDDLE_CLICK_MARGIN = 30;
    private static boolean isMouseGrabbed = true;
    private Vector3f position;
    private float pitch = DEFAULT_PITCH;
    private float yaw = 0;
    private float newZoomDistance;

    public Camera() {
        this.position = new Vector3f(DEFAULT_X, (float) MAX_BACK_ZOOM, DEFAULT_Z);
    }

    public static boolean isMouseGrabbed() {
        return isMouseGrabbed;
    }

    public static void setIsMouseGrabbed(boolean isMouseGrabbed) {
        Camera.isMouseGrabbed = isMouseGrabbed;
    }

    void move(Vector2f firstMiddleClickPosition) {
        if (Keyboard.isKeyDown(Keyboard.KEY_ESCAPE)) {
            Window.showMenu();
        }

        GameMode gameMode = Scene.getGameMode();

        if (gameMode.equals(GameMode.BUILDING) || gameMode.equals(GameMode.ONGOING)) {
            float changePositionBy = (0.4f - calculateMoveDamping(newZoomDistance));

            if (Mouse.isButtonDown(2) && firstMiddleClickPosition != null) {
                if (Mouse.getX() + MIDDLE_CLICK_MARGIN > firstMiddleClickPosition.getX() && this.position.x < 186) {
                    this.position.x += changePositionBy;
                }

                if (Mouse.getX() - MIDDLE_CLICK_MARGIN < firstMiddleClickPosition.getX() && this.position.x > 14) {
                    this.position.x -= changePositionBy;
                }

                if (Mouse.getY() + MIDDLE_CLICK_MARGIN < firstMiddleClickPosition.getY() && this.position.z < 230) {
                    this.position.z += changePositionBy;
                }

                if (Mouse.getY() - MIDDLE_CLICK_MARGIN > firstMiddleClickPosition.getY() && this.position.z > 18) {
                    this.position.z -= changePositionBy;
                }
            }

            calculateZoom();
        }
    }

    public Vector3f getPosition() {
        return position;
    }

    public void setPosition(Vector3f position) {
        this.position = position;
    }

    void reset() {
        this.position = new Vector3f(DEFAULT_POSITION);
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
        return 0.2f + ((newZoomDistance - 5) * (0 - 0.2f) / (MAX_BACK_ZOOM - MAX_ZOOM));
    }

    public String toJSON() {
        String JSON = "";
        JSON += "{";
        JSON += "\"position\":{";
        JSON += "\"x\":\"" + position.getX() + "\",";
        JSON += "\"z\":\"" + position.getZ() + "\"";
        JSON += "},";
        JSON += "\"zoom\":\"" + position.getY() + "\"";
        JSON += "},";
        return JSON;
    }
}
