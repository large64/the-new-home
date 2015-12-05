// to run application from command line, build artifact "The New Home:jar", then cd into the root of the project and run
// java -Djava.library.path="/home/large64/IdeaProjects/The New Home/libs/lwjgl-2.9.3/native/linux/" -jar out/artifacts/The_New_Home_jar/The\ New\ Home.jar

package game;


import org.lwjgl.opengl.Display;
import renderEngine.MasterRenderer;
import toolbox.Window;

import java.awt.*;

/**
 * Created by large64 on 9/6/15.
 */
public class MainGameLoop {
    public static void main(String[] args) {
        Canvas canvas = new Canvas();
        Window window = new Window(canvas);
        canvas.setSize(window.getWidth(), window.getHeight());
        try {
            Display.setParent(canvas);
        } catch (Exception e) {
            System.err.println("Could not set canvas for Display.");
        }

        MasterRenderer.renderScene();
    }
}
