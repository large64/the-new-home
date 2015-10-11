package entities;

import models.TexturedModel;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.util.vector.Vector3f;
import renderEngine.DisplayManager;

/**
 * Created by large64 on 2015.09.26..
 */
public class Player {
    private Camera camera;

    public Player() {
        this.camera = new Camera();
    }

    public void move() {
        camera.move();
    }

    public Camera getCamera() {
        return camera;
    }
}
