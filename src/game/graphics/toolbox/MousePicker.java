package game.graphics.toolbox;

import game.graphics.entities.Camera;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;
import game.graphics.windowparts.Map;
import game.logic.toolbox.Maths;

public class MousePicker {

    private static final int RECURSION_COUNT = 200;
    private static final float RAY_RANGE = 600;

    private Vector3f currentRay = new Vector3f();

    private Matrix4f projectionMatrix;
    private Matrix4f viewMatrix;
    private Camera camera;

    private Vector3f currentTerrainPoint;

    public MousePicker(Camera cam, Matrix4f projection) {
        camera = cam;
        projectionMatrix = projection;
        viewMatrix = Maths.createViewMatrix(camera);
    }

    public Vector3f getCurrentTerrainPoint() {
        return currentTerrainPoint;
    }

    public Vector3f getCurrentRay() {
        return currentRay;
    }

    public void update() {
        viewMatrix = Maths.createViewMatrix(camera);
        currentRay = calculateMouseRay();
        if (intersectionInRange(0, RAY_RANGE, currentRay)) {
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
            Vector3f endPoint = getPointOnRay(ray, half);
            return endPoint;
        }
        if (intersectionInRange(start, half, ray)) {
            return binarySearch(count + 1, start, half, ray);
        } else {
            return binarySearch(count + 1, half, finish, ray);
        }
    }

    private boolean intersectionInRange(float start, float finish, Vector3f ray) {
        Vector3f startPoint = getPointOnRay(ray, start);
        Vector3f endPoint = getPointOnRay(ray, finish);
        if (!isUnderGround(startPoint) && isUnderGround(endPoint)) {
            return true;
        } else {
            return false;
        }
    }

    private boolean isUnderGround(Vector3f testPoint) {
        float height = 0;
        height = Map.getHeightOfMap(testPoint.getX(), testPoint.getZ());
        if (testPoint.y < height) {
            return true;
        } else {
            return false;
        }
    }

}