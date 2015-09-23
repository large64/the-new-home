package engineTester;

import entities.Camera;
import entities.Light;
import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector3f;
import renderEngine.*;

import terrains.Terrain;
import textures.ModelTexture;

/**
 * Created by large64 on 9/6/15.
 */
public class MainGameLoop {
    public static void main(String[] args) {
        DisplayManager.createDisplay();

        Loader loader = new Loader();

        Light light = new Light(new Vector3f(3000, 2000, 2000), new Vector3f(1, 1, 1));

        Terrain terrain = new Terrain(0, -1, loader, new ModelTexture(loader.loadTexture("grass")));

        Camera camera = new Camera();

        MasterRenderer renderer = new MasterRenderer();

        while(!Display.isCloseRequested()) {
            camera.move();

            renderer.processTerrain(terrain);
            renderer.render(light, camera);
            DisplayManager.updateDisplay();
        }

        renderer.cleanUp();
        loader.cleanUp();
        DisplayManager.closeDisplay();
    }
}
