package renderEngine;

import engine.MiniMap;
import engine.actions.Attack;
import engine.entities.RawEntity;
import engine.entities.units.RawSoldier;
import engine.entities.units.Unit;
import entities.Camera;
import entities.Entity;
import entities.Light;
import entities.Player;
import entities.units.Soldier;
import models.TexturedModel;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;
import shaders.StaticShader;
import shaders.TerrainShader;
import skybox.SkyboxRenderer;
import terrains.Map;
import textures.ModelTexture;
import textures.TerrainTexture;
import textures.TerrainTexturePack;
import toolbox.Indicator;
import toolbox.MousePicker;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

/**
 * Created by large64 on 2015.09.17..
 */
public class MasterRenderer {
    private static final float FOV = 70; // Field of view
    private static final float NEAR_PLANE = 0.1f;
    private static final float FAR_PLANE = 1000f;
    private static boolean restart = false;
    private StaticShader shader = new StaticShader();
    private EntityRenderer renderer;
    private Matrix4f projectionMatrix;
    private MapRenderer mapRenderer;
    private TerrainShader terrainShader = new TerrainShader();

    private java.util.Map entities = new HashMap<>();
    private static List<RawEntity> rawEntities = new ArrayList<>();
    private List<Map> maps = new ArrayList<>();

    private SkyboxRenderer skyboxRenderer;

    public MasterRenderer(Loader loader) {
        enableCulling();
        createProjectionMatrix();
        this.renderer = new EntityRenderer(shader, projectionMatrix);
        this.mapRenderer = new MapRenderer(terrainShader, projectionMatrix);
        skyboxRenderer = new SkyboxRenderer(loader, projectionMatrix);
    }

    public static void enableCulling() {
        GL11.glEnable(GL11.GL_CULL_FACE);
        GL11.glCullFace(GL11.GL_BACK);
    }

    private void createProjectionMatrix() {
        float aspectRatio = (float) Display.getWidth() / (float) Display.getHeight();
        float yScale = (float) (1f / Math.tan(Math.toRadians(FOV / 2f))) * aspectRatio;
        float xScale = yScale / aspectRatio;
        float frustumLength = FAR_PLANE - NEAR_PLANE;

        projectionMatrix = new Matrix4f();
        projectionMatrix.m00 = xScale;
        projectionMatrix.m11 = yScale;
        projectionMatrix.m22 = -((FAR_PLANE + NEAR_PLANE) / frustumLength);
        projectionMatrix.m23 = -1;
        projectionMatrix.m32 = -((2 * NEAR_PLANE * FAR_PLANE) / frustumLength);
        projectionMatrix.m33 = 0;
    }

    public static void disableCulling() {
        GL11.glDisable(GL11.GL_CULL_FACE);
    }

    public void render(List<Light> lights, Camera camera) {
        prepare();
        shader.start();
        shader.loadLights(lights);
        shader.loadViewMatrix(camera);
        renderer.render(entities);
        shader.stop();

        terrainShader.start();
        terrainShader.loadLights(lights);
        terrainShader.loadViewMatrix(camera);
        mapRenderer.render(maps);
        terrainShader.stop();
        skyboxRenderer.render(camera);

        maps.clear();
        entities.clear();
    }

    public void prepare() {
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
        GL11.glClearColor(0, 0.2f, 0.3f, 1);
    }

    public void processTerrain(Map map) {
        maps.add(map);
    }

    /**
     * Processes an entity
     * @param entity The entity that is to be processed
     */
    public void processEntity(Entity entity) {
        TexturedModel entityModel = entity.getModel();
        List<Entity> batch = (List<Entity>) entities.get(entityModel);

        if (entity instanceof entities.units.Unit) {
            ((entities.units.Unit) entity).refreshPosition();
        }

        if (batch != null) {
            batch.add(entity);
        } else {
            List<Entity> newBatch = new ArrayList<>();
            newBatch.add(entity);
            entities.put(entityModel, newBatch);
        }
    }

    public void cleanUp() {
        shader.cleanUp();
        terrainShader.cleanUp();
    }

    public Matrix4f getProjectionMatrix() {
        return projectionMatrix;
    }

    public static void renderScene() {
        // @TODO: make z position of entity positive everywhere (maybe by changing the map position)
        // @TODO: build minimap into map
        // @TODO: synchronize positions of raw and real objects
        // @TODO: indicator blinks under Ubuntu
        // @TODO: make game loader work for map, too
        DisplayManager.createDisplay();
        Loader loader = new Loader();

        // Set map features
        TerrainTexture backgroundTexture = new TerrainTexture(loader.loadTexture("grass"));
        TerrainTexture rTexture = new TerrainTexture(loader.loadTexture("dirt"));
        TerrainTexture gTexture = new TerrainTexture(loader.loadTexture("pinkflowers"));
        TerrainTexture bTexture = new TerrainTexture(loader.loadTexture("path"));

        TerrainTexturePack texturePack = new TerrainTexturePack(backgroundTexture, rTexture, gTexture, bTexture);
        TerrainTexture blendMap = new TerrainTexture(loader.loadTexture("blendmap"));

        Random random = new Random(676452);
        Map map = new Map(0, -1, loader, texturePack, blendMap, "heightmap");

        // Set features of entities
        List<RawEntity> rawEntities = new ArrayList<>();
        List<Entity> entities = new ArrayList<>();
        TexturedModel soldierModel = new TexturedModel(OBJLoader.loadObjModel("soldier", loader),
                new ModelTexture(loader.loadTexture("soldier")));

        // Generate random coordinates for entities
        RawSoldier rawSoldier = new RawSoldier(5, 5, 100, true);
        Soldier soldier = new Soldier(soldierModel, rawSoldier, map, 0, 0, 0, 1);
        entities.add(soldier);
        rawEntities.add(rawSoldier);


        RawSoldier rawSoldier2 = new RawSoldier(20, 20, 100, false);
        Soldier soldier2 = new Soldier(soldierModel, rawSoldier2, map, 0, 0, 0, 1);
        entities.add(soldier2);
        rawEntities.add(rawSoldier2);

        MiniMap.setEntities(rawEntities);
        MiniMap.refresh();

        // Set features of lights
        List<Light> lights = new ArrayList<>();
        lights.add(new Light(new Vector3f(-2000, 2000, -2000), new Vector3f(1f, 1f, 1f)));

        // Set features of GUIs
        /*List<GuiTexture> guis = new ArrayList<>();
        GuiTexture gui = new GuiTexture(loader.loadTexture("julia_set"), new Vector2f(0.5f, 0.5f), new Vector2f(0.25f, 0.25f));
        guis.add(gui);
        GuiRenderer guiRenderer = new GuiRenderer(loader);*/

        // Set features of player
        Player player = new Player();

        // Set additional things like renderer, picker
        MasterRenderer renderer = new MasterRenderer(loader);
        MousePicker picker = new MousePicker(player.getCamera(), renderer.getProjectionMatrix(), map);

        // Start an infinite loop for rendering
        while(!Display.isCloseRequested()) {
            if (restart) {
                //player.reset();
                for (Entity entity : entities) {
                    entity.reset();
                }

                for (int i = 0; i < entities.size(); i++) {
                    if (i == 0) {
                        Attack attackRunnable = new Attack((Unit) (entities.get(i).getRawEntity()), entities.get(1).getRawEntity());
                        Thread thread = new Thread(attackRunnable);
                        thread.start();
                    }
                }
                restart = false;
            }
            else {
                player.move();
            }
            // Move the player per frame (and so the camera)
            picker.update();
            Indicator.lookForChanges(picker);

            for (Entity entity : entities) {
                renderer.processEntity(entity);
            }
            renderer.render(lights, player.getCamera());
            renderer.processTerrain(map);
            //guiRenderer.render(guis);
            DisplayManager.updateDisplay();
        }

        //guiRenderer.cleanUp();
        renderer.cleanUp();
        loader.cleanUp();
        DisplayManager.closeDisplay();
    }

    public static void restart() {
        restart = true;
    }

    public static List<RawEntity> getRawEntities() {
        return rawEntities;
    }
}
