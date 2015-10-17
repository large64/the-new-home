// to run application from command line, build artifact "The New Home:jar", then cd into the root of the project and run
// java -Djava.library.path="/home/large64/IdeaProjects/The New Home/libs/lwjgl-2.9.3/native/linux/" -jar out/artifacts/The_New_Home_jar/The\ New\ Home.jar

package engineTester;


import org.lwjgl.opengl.Display;
import renderEngine.MasterRenderer;
import toolbox.Screen;

import javax.swing.*;
import java.awt.*;

/**
 * Created by large64 on 9/6/15.
 */
public class MainGameLoop {
    public static void main(String[] args) {
        Canvas canvas = new Canvas();
        canvas.setSize(800, 600);
        Screen screen = new Screen(canvas);
        JPanel panel = new JPanel(new BorderLayout());
        panel.setSize(canvas.getSize());

        screen = new Screen(canvas);

        try {
            Display.setParent(canvas);
        } catch (Exception e) {
        }

        MasterRenderer.renderScene();
    }
}
