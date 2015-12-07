package game.graphics.renderers;

import game.graphics.entities.units.Healer;
import game.graphics.windowparts.MiniMap;
import game.logic.entities.RawEntity;
import game.logic.entities.RawMap;
import game.logic.entities.units.RawHealer;
import game.logic.entities.units.RawSoldier;
import game.logic.entities.units.Unit;
import game.logic.toolbox.Side;
import game.logic.toolbox.map.Position;
import game.logic.toolbox.map.Tile;
import game.graphics.entities.Camera;
import game.graphics.entities.Entity;
import game.graphics.entities.Light;
import game.graphics.entities.Player;
import game.graphics.entities.units.Soldier;
import game.graphics.models.TexturedModel;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;
import game.graphics.shaders.StaticShader;
import game.graphics.shaders.TerrainShader;
import game.graphics.toolbox.DisplayManager;
import game.graphics.toolbox.Loader;
import game.graphics.toolbox.OBJLoader;
import game.graphics.windowparts.Map;
import game.graphics.textures.ModelTexture;
import game.graphics.textures.TerrainTexture;
import game.graphics.textures.TerrainTexturePack;
import game.graphics.windowparts.Indicator;
import game.graphics.toolbox.MousePicker;

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
        mapRenderer.render();
        terrainShader.stop();
        skyboxRenderer.render(camera);

        entities.clear();
    }

    public void prepare() {
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
        GL11.glClearColor(0, 0.2f, 0.3f, 1);
    }

    /**
     * Processes an entity
     * @param entity The entity that is to be processed
     */
    public void processEntity(Entity entity) {
        TexturedModel entityModel = entity.getModel();
        List<Entity> batch = (List<Entity>) entities.get(entityModel);

        if (entity instanceof game.graphics.entities.units.Unit) {
            ((game.graphics.entities.units.Unit) entity).refreshPosition();
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

        new Map(0, 1, loader, texturePack, blendMap, "heightmap");

        // Set features of entities
        List<Entity> entities = new ArrayList<>();
        TexturedModel soldierModel = new TexturedModel(OBJLoader.loadObjModel("soldier", loader),
                new ModelTexture(loader.loadTexture("soldier")));
        TexturedModel healerModel = new TexturedModel(OBJLoader.loadObjModel("healer", loader),
                new ModelTexture(loader.loadTexture("healer")));
        TexturedModel treeModel = new TexturedModel(OBJLoader.loadObjModel("tree", loader),
                new ModelTexture(loader.loadTexture("palm_tree")));

        // Generate random coordinates for entities
        Soldier soldier = new Soldier(soldierModel, new Vector3f(100, 0, 40), 0, 0, 0, 1);
        entities.add(soldier);

        Soldier soldier2 = new Soldier(soldierModel, new Vector3f(20, 0, 20), 0, 0, 0, 1);
        entities.add(soldier2);

        Healer healer = new Healer(healerModel, new Vector3f(100, 0, 100), 0, 0, 0, 1);
        entities.add(healer);

        Entity treeEntity = new Entity(treeModel, new Vector3f(100, 0, 100), 0, 0, 0, 1);
        entities.add(treeEntity);

        MiniMap.setEntities(rawEntities);
        MiniMap.lookForChanges();
        new RawMap(rawEntities);

        // Set features of lights
        List<Light> lights = new ArrayList<>();
        lights.add(new Light(new Vector3f(-2000, 2000, 2000), new Vector3f(1f, 1f, 1f)));

        // Set features of GUIs
        /*List<GuiTexture> guis = new ArrayList<>();
        GuiTexture gui = new GuiTexture(loader.loadTexture("julia_set"), new Vector2f(0.5f, 0.5f), new Vector2f(0.25f, 0.25f));
        guis.add(gui);
        GuiRenderer guiRenderer = new GuiRenderer(loader);*/

        // Set features of player
        Player player = new Player();

        // Set additional things like renderer, picker
        MasterRenderer renderer = new MasterRenderer(loader);
        MousePicker picker = new MousePicker(player.getCamera(), renderer.getProjectionMatrix());

        boolean rightClick = false;
        boolean leftClick = false;

        Tile selectedTile = null;
        List<RawEntity> selectedEntites = new ArrayList<>();

        // Start an infinite loop for rendering
        while(!Display.isCloseRequested()) {
            if (restart) {
                player.reset();
                for (RawEntity entity : rawEntities) {
                    entity.reset();
                }
                restart = false;
            }
            else {
                player.move();
            }

            if (Mouse.isButtonDown(1) && !rightClick && !selectedEntites.isEmpty()) {
                MiniMap.clearMarkers();
                for (RawEntity entity : selectedEntites) {
                    if (entity instanceof Unit) {
                        Unit unit = (Unit) entity;
                        selectedTile = Tile.positionToTile(new Position(picker.getCurrentTerrainPoint().x, picker.getCurrentTerrainPoint().z));
                        unit.calculatePath(selectedTile);
                    }
                }
            }

            if (Mouse.isButtonDown(0) && !leftClick) {
                Tile tile = Tile.positionToTile(new Position(picker.getCurrentTerrainPoint().x, picker.getCurrentTerrainPoint().z));
                boolean atLeastOne = false;
                for (Entity entity : entities) {
                    RawEntity rawEntity = entity.getRawEntity();

                    if (rawEntity.getTilePosition().equals(tile) && rawEntity.getSide().equals(Side.FRIEND)) {
                        selectedEntites.add(rawEntity);
                        entity.setSelected(true);
                        atLeastOne = true;
                    }
                }
                if (!atLeastOne) {
                    selectedEntites.clear();
                    for (Entity entity : entities) {
                        entity.setSelected(false);
                    }
                }
            }

            rightClick = Mouse.isButtonDown(1);
            leftClick = Mouse.isButtonDown(0);

            // Move the player per frame (and so the camera)
            picker.update();
            Indicator.lookForChanges(picker);

            for (Entity entity : entities) {
                RawEntity rawEntity = entity.getRawEntity();
                if (rawEntity.isAlive()) {
                    if (rawEntity instanceof Unit) {
                        Unit rawUnit = (Unit) rawEntity;
                        if ((rawUnit).isMoving() && selectedTile != null) {
                            rawUnit.performAction(selectedTile);
                        }
                    }

                    renderer.processEntity(entity);
                }
            }
            MiniMap.lookForChanges();
            RawMap.lookForChanges();
            renderer.render(lights, player.getCamera());
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

    public static void addRawEntity(RawEntity entity) {
        MasterRenderer.rawEntities.add(entity);
    }
}
