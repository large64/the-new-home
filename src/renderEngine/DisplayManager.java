package renderEngine;

import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.*;

/**
 * Created by large64 on 9/6/15.
 */
public class DisplayManager {
    private static final int WIDTH = 800;
    private static final int FPS_CAP = 720;
    private static final int HEIGHT = 600;

    public static void createDisplay() {
        ContextAttribs attribs = new ContextAttribs(3,2)
                .withForwardCompatible(true)
                .withProfileCore(true);

        try {
            Display.setDisplayMode(new DisplayMode(WIDTH, HEIGHT));
            Display.create(new PixelFormat(), attribs);
            Display.setTitle("The New Home");
        } catch (LWJGLException e) {
            e.printStackTrace();
        }

        GL11.glViewport(0, 0, WIDTH, HEIGHT);
    }

    public static void updateDisplay() {
        Display.sync(FPS_CAP);
        Display.update();
    }

    public static void closeDisplay() {
        Display.destroy();
    }
}
