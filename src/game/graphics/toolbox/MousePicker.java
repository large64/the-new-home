package game.graphics.toolbox;

import game.graphics.entities.Camera;
import game.graphics.windowparts.Map;
import game.logic.toolbox.Maths;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

public class MousePicker {

    private static final int RECURSION_COUNT = 30;
    private static final float RAY_RANGE = 600;
    private final Matrix4f projectionMatrix;
    private final Map map;
    private Vector3f currentRay = new Vector3f();
    private Matrix4f viewMatrix;
    private Camera camera;
    private Vector3f currentTerrainPoint;

    public MousePicker(Camera cam, Matrix4f projection, Map map) {
        camera = cam;
        projectionMatrix = projection;
        viewMatrix = Maths.createViewMatrix(camera);
        this.map = map;
    }

    public Vector3f getCurrentTerrainPoint() {
        return currentTerrainPoint;
    }

    public void update() {
        viewMatrix = Maths.createViewMatrix(camera);
        currentRay = calculateMouseRay();
        if (intersectionInRange(0, RAY_RANGE, currentRay, this.map)) {
            currentTerrainPoint = binarySearch(0, 0, RAY_RANGE, currentRay);
        } else {
            currentTerrainPoint = null;
        }
    }

    private Vector3f calculateMouseRay() {
        float mouseX = Mouse.getX();
        float mouseY = Mouse.getY();
        Vector2f normalizedCoordinates = getNormalisedDeviceCoordinates(mouseX, mouseY);
        Vector4f clipCoordinates = new Vector4f(normalizedCoordinates.x, normalizedCoordinates.y, -1.0f, 1.0f);
        Vector4f eyeCoordinates = toEyeCoordinates(clipCoordinates);
        return toWorldCoordinates(eyeCoordinates);
    }

    private Vector3f toWorldCoordinates(Vector4f eyeCoordinates) {
        Matrix4f invertedView = Matrix4f.invert(viewMatrix, null);
        Vector4f rayWorld = Matrix4f.transform(invertedView, eyeCoordinates, null);
        Vector3f mouseRay = new Vector3f(rayWorld.x, rayWorld.y, rayWorld.z);
        mouseRay.normalise();
        return mouseRay;
    }

    private Vector4f toEyeCoordinates(Vector4f clipCoordinates) {
        Matrix4f invertedProjection = Matrix4f.invert(projectionMatrix, null);
        Vector4f eyeCoordinates = Matrix4f.transform(invertedProjection, clipCoordinates, null);
        return new Vector4f(eyeCoordinates.x, eyeCoordinates.y, -1f, 0f);
    }

    private Vector2f getNormalisedDeviceCoordinates(float mouseX, float mouseY) {
        float x = (2.0f * mouseX) / Display.getWidth() - 1f;
        float y = (2.0f * mouseY) / Display.getHeight() - 1f;
        return new Vector2f(x, y);
    }

    private Vector3f getPointOnRay(Vector3f ray, float distance) {
        Vector3f camPos = camera.getPosition();
        Vector3f start = new Vector3f(camPos.x, camPos.y, camPos.z);
        Vector3f scaledRay = new Vector3f(ray.x * distance, ray.y * distance, ray.z * distance);
        return Vector3f.add(start, scaledRay, null);
    }

    private Vector3f binarySearch(int count, float start, float finish, Vector3f ray) {
        float half = start + ((finish - start) / 2f);
        if (count >= RECURSION_COUNT) {
            return getPointOnRay(ray, half);
        }
        if (intersectionInRange(start, half, ray, this.map)) {
            return binarySearch(count + 1, start, half, ray);
        } else {
            return binarySearch(count + 1, half, finish, ray);
        }
    }

    private boolean intersectionInRange(float start, float finish, Vector3f ray, Map map) {
        Vector3f startPoint = getPointOnRay(ray, start);
        Vector3f endPoint = getPointOnRay(ray, finish);
        return !isUnderGround(startPoint, map) && isUnderGround(endPoint, map);
    }

    private boolean isUnderGround(Vector3f testPoint, Map map) {
        float height = map.getHeightOfMap(testPoint.getX(), testPoint.getZ());
        return testPoint.y < height;
    }

    public void setCamera(Camera camera) {
        this.camera = camera;
    }
}