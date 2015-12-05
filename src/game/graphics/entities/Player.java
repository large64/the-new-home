package game.graphics.entities;

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

    public void reset() {
        camera.reset();
    }

    public Camera getCamera() {
        return camera;
    }
}
