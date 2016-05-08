package game.graphics.entities;

import game.graphics.windowparts.Scene;
import org.lwjgl.util.vector.Vector2f;

/**
 * Created by large64 on 2015.09.26..
 */
public class Player {
    private Camera camera;

    public Player() {
        this.camera = new Camera();
    }

    public void move(Vector2f firstMiddleClickPosition) {
        camera.move(firstMiddleClickPosition);
    }

    public void reset() {
        camera.reset();
    }

    public Camera getCamera() {
        return camera;
    }

    public void setCamera(Camera camera) {
        this.camera = camera;
    }
}
