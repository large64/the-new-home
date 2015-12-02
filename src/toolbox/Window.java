package toolbox;

import com.sun.glass.events.WindowEvent;
import engine.MiniMap;
import org.lwjgl.opengl.Display;
import renderEngine.MapRenderer;
import renderEngine.MasterRenderer;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.text.html.ObjectView;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowListener;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by large64 on 31/10/15.
 */
public class Window {
    private static JFrame mainFrame;
    private static JPanel menuWrapperPanel;
    private static JPanel bottomWrapperPanel;
    private static boolean isTileShown = false;

    public Window(Canvas canvas) {
        Color menuBgColor = new Color(2, 120, 0);

        EmptyBorder menuPanelBorder = new EmptyBorder(10, 10, 20, 10);

        JButton button = new JButton("New Game");
        button.addActionListener(e -> {
            MasterRenderer.restart();
        });

        JButton button1 = new JButton("Quit");
        button1.addActionListener(e1 -> {
            mainFrame.dispatchEvent(new java.awt.event.WindowEvent(mainFrame, java.awt.event.WindowEvent.WINDOW_CLOSING));
        });

        JButton button2 = new JButton("Toggle tiles");
        button2.addActionListener(el ->{
            if (!isTileShown) {
                MapRenderer.setShowTiles(true);
                isTileShown = true;
            }
            else {
                MapRenderer.setShowTiles(false);
                isTileShown = false;
            }
        });

        Indicator indicator = new Indicator("text");

        JPanel menuPanel = new JPanel(new GridLayout(3, 1));
        JPanel indicatorPanel = new JPanel(new GridLayout(1,1));
        JPanel bottomPanel = new JPanel(new BorderLayout());

        mainFrame = new JFrame();
        mainFrame.setLayout(new BorderLayout());
        mainFrame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        mainFrame.setUndecorated(true);
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        menuWrapperPanel = new JPanel();
        menuWrapperPanel.setLayout(new BorderLayout());
        menuWrapperPanel.setBackground(menuBgColor);
        // Set menu panel size
        menuWrapperPanel.setPreferredSize(new Dimension(150, mainFrame.getHeight()));
        menuPanel.setOpaque(false);

        new MiniMap();

        bottomPanel.add(new JLabel("Mini map"), BorderLayout.NORTH);
        bottomPanel.add(MiniMap.getLabel(), BorderLayout.CENTER);
        bottomWrapperPanel = new JPanel();
        bottomWrapperPanel.setLayout(new BorderLayout());
        bottomWrapperPanel.setBackground(menuBgColor);
        bottomWrapperPanel.setPreferredSize(new Dimension(mainFrame.getWidth(), 150));
        bottomWrapperPanel.add(bottomPanel, BorderLayout.EAST);

        indicatorPanel.setOpaque(false);
        indicatorPanel.setBorder(BorderFactory.createLineBorder(new Color(186, 186, 186), 1, true));

        menuPanel.add(button);
        menuPanel.add(button1);
        menuPanel.add(button2);

        indicatorPanel.add(indicator.getTextPane());

        menuWrapperPanel.add(menuPanel, BorderLayout.NORTH);
        menuWrapperPanel.add(indicatorPanel, BorderLayout.SOUTH);

        mainFrame.add(menuWrapperPanel, BorderLayout.WEST);
        mainFrame.add(bottomWrapperPanel, BorderLayout.SOUTH);
        mainFrame.add(canvas, BorderLayout.CENTER);
        mainFrame.pack();
        mainFrame.setVisible(true);

        menuPanel.setBorder(menuPanelBorder);
    }

    public static int getWidth() {
        return mainFrame.getWidth();
    }

    public static int getHeight() {
        return mainFrame.getHeight();
    }

    public static JPanel getMenuWrapperPanel() {
        return menuWrapperPanel;
    }
}
