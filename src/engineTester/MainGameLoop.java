package engineTester;

import entities.Camera;
import entities.Entity;
import entities.Light;
import models.TexturedModel;
import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector3f;
import renderEngine.*;
import models.RawModel;
import shaders.StaticShader;
import textures.ModelTexture;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by large64 on 9/6/15.
 */
public class MainGameLoop {
    public static void main(String[] args) {
        DisplayManager.createDisplay();

        Loader loader = new Loader();

        RawModel model = OBJLoader.loadObjModel("cube", loader);
        TexturedModel cubeModel = new TexturedModel(model,
                new ModelTexture(loader.loadTexture("julia_set")));

        Light light = new Light(new Vector3f(3000, 2000, 3000), new Vector3f(1, 1, 1));

        Camera camera = new Camera();
        Random random = new Random();
        List<Entity> allCubes = new ArrayList<>();

        for (int i = 0; i < 200; i++) {
            float x = random.nextFloat() * 100 - 50;
            float y = random.nextFloat() * 100 - 50;
            float z = random.nextFloat() * -300;

            allCubes.add(new Entity(cubeModel, new Vector3f(x, y, z), random.nextFloat() * 180f,
                    random.nextFloat() * 180f, 0f, 1f));
        }

        MasterRenderer renderer = new MasterRenderer();

        while(!Display.isCloseRequested()) {
            // increaseRotation was here in the video
            camera.move();

            for (Entity cube : allCubes) {
                renderer.processEntity(cube);
            }
            renderer.render(light, camera);
            DisplayManager.updateDisplay();
        }

        renderer.cleanUp();
        loader.cleanUp();
        DisplayManager.closeDisplay();
    }
}
