// to run application from command line, build artifact "The New Home:jar", then cd into the root of the project and run
// java -Djava.library.path="/home/large64/IdeaProjects/The New Home/libs/lwjgl-2.9.3/native/linux/" -jar out/artifacts/The_New_Home_jar/The\ New\ Home.jar

package game;


import game.graphics.renderers.MasterRenderer;
import game.graphics.windowparts.Window;
import org.lwjgl.opengl.Display;

import java.awt.*;

public class MainGameLoop {
    public static void main(String[] args) {
        Canvas canvas = new Canvas();
        new Window(canvas);
        canvas.setSize(Window.getWidth(), Window.getHeight());
        try {
            Display.setParent(canvas);
        } catch (Exception e) {
            System.err.println("Could not set canvas for Display.");
        }
        MasterRenderer.renderScene();
    }
}
