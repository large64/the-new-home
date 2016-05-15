package game.graphics.entities;

import org.lwjgl.util.vector.Vector2f;

public class Player {
    private Camera camera;

    public Player() {
        this.camera = new Camera();
    }

    public void move(Vector2f firstMiddleClickPosition) {
        camera.move(firstMiddleClickPosition);
    }

    public Camera getCamera() {
        return camera;
    }

    public void setCamera(Camera camera) {
        this.camera = camera;
    }
}
