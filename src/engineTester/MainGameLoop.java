// to run application from command line, build artifact "The New Home:jar", then cd into the root of the project and run
// java -Djava.library.path="/home/large64/IdeaProjects/The New Home/libs/lwjgl-2.9.3/native/linux/" -jar out/artifacts/The_New_Home_jar/The\ New\ Home.jar

package engineTester;

import entities.Camera;
import entities.Entity;
import entities.Light;
import entities.Player;
import guis.GuiRenderer;
import guis.GuiTexture;
import models.RawModel;
import models.TexturedModel;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;
import renderEngine.*;

import terrains.Terrain;
import textures.ModelTexture;
import textures.TerrainTexture;
import textures.TerrainTexturePack;
import toolbox.MousePicker;

import javax.xml.soap.Text;
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

        TerrainTexture backgroundTexture = new TerrainTexture(loader.loadTexture("grass"));
        TerrainTexture rTexture = new TerrainTexture(loader.loadTexture("dirt"));
        TerrainTexture gTexture = new TerrainTexture(loader.loadTexture("pinkflowers"));
        TerrainTexture bTexture = new TerrainTexture(loader.loadTexture("path"));

        TerrainTexturePack texturePack = new TerrainTexturePack(backgroundTexture, rTexture, gTexture, bTexture);
        TerrainTexture blendMap = new TerrainTexture(loader.loadTexture("blendmap"));

        RawModel model = OBJLoader.loadObjModel("tree", loader);

        TexturedModel staticModel = new TexturedModel(model, new ModelTexture(loader.loadTexture("grass")));
        TexturedModel grass = new TexturedModel(OBJLoader.loadObjModel("grassModel", loader),
                new ModelTexture(loader.loadTexture("grassTexture")));
        /*TexturedModel flower = new TexturedModel(OBJLoader.loadObjModel("flowerModel", loader),
                new ModelTexture(loader.loadTexture("flowerTexture")));*/

        ModelTexture fernModelTexture = new ModelTexture(loader.loadTexture("fern"));
        fernModelTexture.setNumberOfRows(2);
        TexturedModel fern = new TexturedModel(OBJLoader.loadObjModel("fern", loader),fernModelTexture);

        TexturedModel bobble = new TexturedModel(OBJLoader.loadObjModel("lowPolyTree", loader),
                new ModelTexture(loader.loadTexture("lowPolyTree")));

        TexturedModel lamp = new TexturedModel(OBJLoader.loadObjModel("lamp", loader),
                new ModelTexture(loader.loadTexture("lamp")));

        grass.getTexture().setHasTransparency(true);
        grass.getTexture().setUseFakeLighting(true);
        //flower.getTexture().setHasTransparency(true);
        //flower.getTexture().setUseFakeLighting(true);
        fern.getTexture().setHasTransparency(true);

        List<Entity> entities = new ArrayList<>();
        Random random = new Random(676452);
        Terrain terrain = new Terrain(0, -1, loader, texturePack, blendMap, "heightmap");

        for (int i = 0; i < 200; ++i) {
            /*if (i % 7 == 0) {
                float x = random.nextFloat() * 400;
                float z = random.nextFloat() * -600;
                float y = terrain.getHeightOfTerrain(x, z);
                entities.add(new Entity(grass, new Vector3f(x, y, z), 0, 0, 0, 1.8f));
            }*/
            if (i % 3 == 0) {
                float x = random.nextFloat() * 400.0f;
                float z = random.nextFloat() * -400.0f;
                float y = terrain.getHeightOfTerrain(x, z);
                entities.add(new Entity(fern, random.nextInt(4), new Vector3f(x, y, z), 0, random.nextFloat() * 360, 0, 0.9f));
                x = random.nextFloat() * 400f;
                z = random.nextFloat() * -400f;
                y = terrain.getHeightOfTerrain(x, z);
                entities.add(new Entity(bobble, new Vector3f(x, y, z), 0, random.nextFloat() * 360, 0,
                        random.nextFloat() * 0.1f + 0.6f));
                x = random.nextFloat() * 400f;
                z = random.nextFloat() * -400f;
                y = terrain.getHeightOfTerrain(x, z);
                entities.add(new Entity(staticModel, new Vector3f(x, y, z), 0, 0, 0, random.nextFloat() * 1 + 4));
            }
        }

        List<Light> lights = new ArrayList<>();
        lights.add(new Light(new Vector3f(-2000, 1000, -2000), new Vector3f(1f, 1f, 1f)));
        lights.add(new Light(new Vector3f(185, terrain.getHeightOfTerrain(185, -293) + 16, -293), new Vector3f(2, 0, 0), new Vector3f(1, 0.01f, 0.002f)));
        lights.add(new Light(new Vector3f(370, terrain.getHeightOfTerrain(370, -300) + 16, -300), new Vector3f(0, 2, 2), new Vector3f(1, 0.01f, 0.002f)));
        Light moveableLight = new Light(new Vector3f(293, terrain.getHeightOfTerrain(293, -305) + 16, -305), new Vector3f(2, 2, 0), new Vector3f(1, 0.01f, 0.002f));
        lights.add(moveableLight);

        entities.add(new Entity(lamp, new Vector3f(185, terrain.getHeightOfTerrain(185, -293), -293), 0, 0, 0, 1));
        entities.add(new Entity(lamp, new Vector3f(370, terrain.getHeightOfTerrain(370, -300), -300), 0, 0, 0, 1));
        Entity lampEntity = new Entity(lamp, new Vector3f(293, terrain.getHeightOfTerrain(293, -305), -305), 0, 0, 0, 1);
        entities.add(lampEntity);

        List<GuiTexture> guis = new ArrayList<>();
        GuiTexture gui = new GuiTexture(loader.loadTexture("julia_set"), new Vector2f(0.5f, 0.5f), new Vector2f(0.25f, 0.25f));
        guis.add(gui);
        GuiRenderer guiRenderer = new GuiRenderer(loader);

        // Player
        RawModel bunnyModel = OBJLoader.loadObjModel("stanfordBunny", loader);
        TexturedModel stanfordBunny = new TexturedModel(bunnyModel, new ModelTexture(loader.loadTexture("white")));
        Player player = new Player(stanfordBunny, new Vector3f(190, terrain.getHeightOfTerrain(190, -300), -300), 0, 0, 0, 1);

        Camera camera = new Camera();

        MasterRenderer renderer = new MasterRenderer(loader);

        MousePicker picker = new MousePicker(camera, renderer.getProjectionMatrix(), terrain);

        while(!Display.isCloseRequested()) {
            camera.move();
            player.move(terrain);
            picker.update();

            Vector3f terrainPoint = picker.getCurrentTerrainPoint();

            if (terrainPoint != null) {
                lampEntity.setPosition(terrainPoint);
                moveableLight.setPosition(new Vector3f(terrainPoint.x, terrainPoint.y + 15, terrainPoint.z));
            }

            renderer.processEntity(player);

            for (Entity entity : entities) {
                renderer.processEntity(entity);
            }
            renderer.render(lights, camera);
            renderer.processTerrain(terrain);
            //guiRenderer.render(guis);
            DisplayManager.updateDisplay();
        }

        //guiRenderer.cleanUp();
        renderer.cleanUp();
        loader.cleanUp();
        DisplayManager.closeDisplay();
    }
}
