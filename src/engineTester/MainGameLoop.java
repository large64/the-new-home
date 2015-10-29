// to run application from command line, build artifact "The New Home:jar", then cd into the root of the project and run
// java -Djava.library.path="/home/large64/IdeaProjects/The New Home/libs/lwjgl-2.9.3/native/linux/" -jar out/artifacts/The_New_Home_jar/The\ New\ Home.jar

package engineTester;


import org.lwjgl.opengl.Display;
import renderEngine.DisplayManager;
import renderEngine.MasterRenderer;
import toolbox.Indicator;
import toolbox.Screen;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.lang.management.ManagementFactory;

/**
 * Created by large64 on 9/6/15.
 */
public class MainGameLoop {
    public static JFrame mainFrame = new JFrame("The New Home");
    public static JPanel menuWrapperPanel = new JPanel(new BorderLayout());

    public static void main(String[] args) {
        mainFrame.setExtendedState(JFrame.MAXIMIZED_BOTH);

        Canvas canvas = new Canvas();
        Color menuBgColor = new Color(2, 120, 0);
        Indicator btnIndicator = new Indicator("text");

        EmptyBorder menuPanelBorder = new EmptyBorder(10, 10, 20, 10);
        BorderLayout layout = new BorderLayout();

        JPanel menuPanel = new JPanel(new GridLayout(2, 1));
        JPanel indicatorPanel = new JPanel(new GridLayout(2,1));

        JButton button = new JButton("Test test test");
        JButton button1 = new JButton("Test2");

        mainFrame.setLayout(layout);
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                MasterRenderer.requestExit(true);
                System.exit(0);
            }
        });

        menuPanel.setBorder(menuPanelBorder);
        indicatorPanel.setBorder(menuPanelBorder);
        menuWrapperPanel.setBackground(menuBgColor);
        menuPanel.setBackground(menuBgColor);
        indicatorPanel.setBackground(menuBgColor);

        menuPanel.add(button);
        menuPanel.add(button1);

        indicatorPanel.add(btnIndicator.getTextPane());
        indicatorPanel.add(new JButton("Test"));

        menuWrapperPanel.add(menuPanel, BorderLayout.NORTH);
        menuWrapperPanel.add(indicatorPanel, BorderLayout.AFTER_LAST_LINE);
        mainFrame.add(menuWrapperPanel, BorderLayout.WEST);
        mainFrame.pack();
        mainFrame.add(canvas, BorderLayout.CENTER);
        mainFrame.setVisible(true);
        canvas.setSize(mainFrame.getWidth(), mainFrame.getHeight());

        try {
            Display.setParent(canvas);
        } catch (Exception e) {
            System.err.println("Could not set canvas for Display.");
        }

        MasterRenderer.renderScene();
    }
}
