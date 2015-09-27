// to run application from command line, build artifact "The New Home:jar", then cd into the root of the project and run
// java -Djava.library.path="/home/large64/IdeaProjects/The New Home/libs/lwjgl-2.9.3/native/linux/" -jar out/artifacts/The_New_Home_jar/The\ New\ Home.jar

package engineTester;

import entities.Camera;
import entities.Entity;
import entities.Light;
import entities.Player;
import models.RawModel;
import models.TexturedModel;
import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector3f;
import renderEngine.*;

import terrains.Terrain;
import textures.ModelTexture;
import textures.TerrainTexture;
import textures.TerrainTexturePack;

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

        grass.getTexture().setHasTransparency(true);
        grass.getTexture().setUseFakeLighting(true);
        //flower.getTexture().setHasTransparency(true);
        //flower.getTexture().setUseFakeLighting(true);
        fern.getTexture().setHasTransparency(true);

        List<Entity> entities = new ArrayList<>();
        Random random = new Random(676452);
        Terrain terrain = new Terrain(0, -1, loader, texturePack, blendMap, "heightmap");

        for (int i = 0; i < 200; ++i) {
            if (i % 7 == 0) {
                float x = random.nextFloat() * 400;
                float z = random.nextFloat() * -600;
                float y = terrain.getHeightOfTerrain(x, z);
                entities.add(new Entity(grass, new Vector3f(x, y, z), 0, 0, 0, 1.8f));
                /*entities.add(new Entity(flower, new Vector3f(random.nextFloat() * 400 - 200, 0,
                        random.nextFloat() * -400), 0, 0, 0, 2.3f));*/
            }
            if (i % 3 == 0) {
                float x = random.nextFloat() * 400;
                float z = random.nextFloat() * -600;
                float y = terrain.getHeightOfTerrain(x, z);
                entities.add(new Entity(fern, random.nextInt(4), new Vector3f(x, y, z), 0, random.nextFloat() * 360, 0, 0.9f));
                x = random.nextFloat() * 400;
                z = random.nextFloat() * -600;
                y = terrain.getHeightOfTerrain(x, z);
                entities.add(new Entity(bobble, new Vector3f(x, y, z), 0, random.nextFloat() * 360, 0,
                        random.nextFloat() * 0.1f + 0.6f));
                x = random.nextFloat() * 400;
                z = random.nextFloat() * -600;
                y = terrain.getHeightOfTerrain(x, z);
                entities.add(new Entity(staticModel, new Vector3f(x, y, z), 0, 0, 0, random.nextFloat() * 1 + 4));
            }
        }

        Light light = new Light(new Vector3f(20000, 40000, 20000), new Vector3f(1, 1, 1));

        // Player
        RawModel bunnyModel = OBJLoader.loadObjModel("stanfordBunny", loader);
        TexturedModel stanfordBunny = new TexturedModel(bunnyModel, new ModelTexture(loader.loadTexture("white")));
        Player player = new Player(stanfordBunny, new Vector3f(100, 0, -50), 0, 0, 0, 1);

        Camera camera = new Camera();

        MasterRenderer renderer = new MasterRenderer();

        while(!Display.isCloseRequested()) {
            camera.move();
            player.move(terrain);
            renderer.processEntity(player);

            for (Entity entity : entities) {
                renderer.processEntity(entity);
            }
            renderer.render(light, camera);
            renderer.processTerrain(terrain);
            DisplayManager.updateDisplay();
        }

        renderer.cleanUp();
        loader.cleanUp();
        DisplayManager.closeDisplay();
    }
}
