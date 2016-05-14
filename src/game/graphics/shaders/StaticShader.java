package game.graphics.shaders;

import game.graphics.entities.Camera;
import game.graphics.entities.Light;
import game.logic.toolbox.Maths;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import java.util.List;

/**
 * Created by large64 on 2015.09.08..
 */
public class StaticShader extends ShaderProgram {
    private static final int MAX_LIGHTS = 4;

    private static final String VERTEX_FILE = "src/game/graphics/shaders/vertexShader";
    private static final String FRAGMENT_FILE = "src/game/graphics/shaders/fragmentShader";

    private int location_transformationMatrix;
    private int location_projectionMatrix;
    private int location_viewMatrix;
    private int location_lightPosition[];
    private int location_lightColor[];
    private int location_shineDamper;
    private int location_reflectivity;
    private int location_useFakeLighting;
    private int location_numberOfRows;
    private int location_offset;
    private int location_attenuation[];
    private int location_isBeingAttacked;

    public StaticShader() {
        super(VERTEX_FILE, FRAGMENT_FILE);
    }

    @Override
    protected void getAllUniformLocations() {
        location_transformationMatrix = super.getUniformLocation("transformationMatrix");
        location_projectionMatrix = super.getUniformLocation("projectionMatrix");
        location_viewMatrix = super.getUniformLocation("viewMatrix");
        location_shineDamper = super.getUniformLocation("shineDamper");
        location_reflectivity = super.getUniformLocation("reflectivity");
        location_useFakeLighting = super.getUniformLocation("useFakeLighting");
        location_numberOfRows = super.getUniformLocation("numberOfRows");
        location_offset = super.getUniformLocation("offset");
        location_isBeingAttacked = super.getUniformLocation("isBeingAttacked");

        location_lightPosition = new int[MAX_LIGHTS];
        location_lightColor = new int[MAX_LIGHTS];
        location_attenuation = new int[MAX_LIGHTS];

        for (int i = 0; i < MAX_LIGHTS; i++) {
            location_lightPosition[i] = super.getUniformLocation("lightPosition[" + i + "]");
            location_lightColor[i] = super.getUniformLocation("lightColor[" + i + "]");
            location_attenuation[i] = super.getUniformLocation("attenuation[" + i + "]");
        }
    }

    public void loadNumberOfRows(int numberOfRows) {
        super.loadFloat(location_numberOfRows, numberOfRows);
    }

    public void loadOffset(float x, float y) {
        super.load2DVector(location_offset, new Vector2f(x, y));
    }

    @Override
    protected void bindAttributes() {
        super.bindAttribute(0, "position");
        super.bindAttribute(1, "textureCoords");
        super.bindAttribute(2, "normal");
    }

    public void loadFakeLightingVariable(boolean useFakeLighting) {
        super.loadBoolean(location_useFakeLighting, useFakeLighting);
    }

    public void loadIsBeingAttacked(boolean isBeingAttacked) {
        super.loadBoolean(location_isBeingAttacked, isBeingAttacked);
    }

    public void loadShineVariables(float damper, float reflectivity) {
        super.loadFloat(location_shineDamper, damper);
        super.loadFloat(location_reflectivity, reflectivity);
    }

    public void loadTransformationMatrix(Matrix4f matrix) {
        super.loadMatrix(location_transformationMatrix, matrix);
    }

    public void loadLights(List<Light> lights) {
        for (int i = 0; i < MAX_LIGHTS; i++) {
            if (i < lights.size()) {
                super.load3DVector(location_lightPosition[i], lights.get(i).getPosition());
                super.load3DVector(location_lightColor[i], lights.get(i).getColor());
                super.load3DVector(location_attenuation[i], lights.get(i).getAttenuation());
            } else {
                super.load3DVector(location_lightPosition[i], new Vector3f(0, 0, 0));
                super.load3DVector(location_lightColor[i], new Vector3f(0, 0, 0));
                super.load3DVector(location_attenuation[i], new Vector3f(1, 0, 0));
            }
        }
    }

    public void loadViewMatrix(Camera camera) {
        Matrix4f viewMatrix = Maths.createViewMatrix(camera);
        super.loadMatrix(location_viewMatrix, viewMatrix);
    }

    public void loadProjectionMatrix(Matrix4f matrix) {
        super.loadMatrix(location_projectionMatrix, matrix);
    }
}
