package game.graphics.windowparts;

import game.graphics.entities.Camera;
import game.graphics.renderers.MapRenderer;
import game.graphics.renderers.MasterRenderer;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

/**
 * Created by large64 on 31/10/15.
 */
public class Window {
    private static JFrame mainFrame;
    private static JFrame menuFrame;
    private static JPanel bottomWrapperPanel;
    private static boolean areTilesShown = false;

    public static final int BOTTOM_COMPONENT_HEIGHT = 130;

    public Window(Canvas canvas) {
        Color menuBgColor = new Color(2, 120, 0);
        Color menuItemBg = new Color(205, 221, 237);

        EmptyBorder menuPanelBorder = new EmptyBorder(10, 20, 20, 20);

        JButton button = new JButton("New Game");
        button.addActionListener(e -> {
            MasterRenderer.restart();
        });

        JButton button1 = new JButton("Quit");
        button1.addActionListener(e1 -> {
            mainFrame.dispatchEvent(new java.awt.event.WindowEvent(mainFrame, java.awt.event.WindowEvent.WINDOW_CLOSING));
        });

        JCheckBox tilesCheckbox = new JCheckBox("Show tiles");
        JPanel checkboxPanel = new JPanel();

        checkboxPanel.setBorder(BorderFactory.createLineBorder(new Color(122, 138, 153), 1, false));
        checkboxPanel.setBackground(menuItemBg);
        tilesCheckbox.setBackground(menuItemBg);
        tilesCheckbox.setBorder(new EmptyBorder(0, 10, 0, 10));
        tilesCheckbox.addActionListener(el ->{
            if (!areTilesShown) {
                MapRenderer.setShowTiles(true);
                areTilesShown = true;
            }
            else {
                MapRenderer.setShowTiles(false);
                areTilesShown = false;
            }
        });
        checkboxPanel.add(tilesCheckbox);

        JButton button3 = new JButton("Resume");
        button3.addActionListener(el ->{
            menuFrame.setVisible(false);
            Camera.setIsMouseGrabbed(true);
        });

        PositionInfo positionInfo = new PositionInfo();
        EntityInfo entityInfo = new EntityInfo();
        ActionInfo actionInfo = new ActionInfo();

        JPanel menuPanel = new JPanel(new GridLayout(4, 1));
        JPanel devMenuPanel = new JPanel(new GridLayout(2, 1));
        JPanel indicatorPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JPanel miniMapPanel = new JPanel(new BorderLayout());

        mainFrame = new JFrame();
        mainFrame.setLayout(new BorderLayout());
        mainFrame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        mainFrame.setUndecorated(true);
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        menuFrame = new JFrame();
        menuFrame.setLayout(new BorderLayout());
        menuFrame.setBackground(menuBgColor);
        // Set menu panel size
        menuFrame.setPreferredSize(new Dimension(190, 400));
        menuFrame.setUndecorated(true);
        menuFrame.pack();
        menuFrame.setLocationRelativeTo(null);

        new MiniMap();

        indicatorPanel.setOpaque(false);
        indicatorPanel.setBorder(BorderFactory.createLineBorder(new Color(186, 186, 186), 1, true));

        JLabel menuTitle = new JLabel("Menu");
        menuTitle.setHorizontalAlignment(JLabel.CENTER);

        JLabel devMenuTitle = new JLabel("Developers' menu");
        devMenuTitle.setHorizontalAlignment(JLabel.CENTER);

        menuPanel.add(menuTitle);
        menuPanel.add(button3);
        menuPanel.add(button);
        menuPanel.add(button1);

        devMenuPanel.add(devMenuTitle);
        devMenuPanel.add(checkboxPanel);

        indicatorPanel.add(positionInfo.getTextPane());
        indicatorPanel.setPreferredSize(new Dimension(120, BOTTOM_COMPONENT_HEIGHT));

        miniMapPanel.add(new JLabel("Mini map"), BorderLayout.NORTH);
        miniMapPanel.add(MiniMap.getLabel(), BorderLayout.CENTER);
        bottomWrapperPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bottomWrapperPanel.setBackground(menuBgColor);
        bottomWrapperPanel.setPreferredSize(new Dimension(mainFrame.getWidth(), 150));
        bottomWrapperPanel.add(actionInfo.getWrapperPanel());
        bottomWrapperPanel.add(entityInfo.getWrapperPanel());
        bottomWrapperPanel.add(indicatorPanel);
        bottomWrapperPanel.add(miniMapPanel);

        menuFrame.add(menuPanel, BorderLayout.NORTH);
        menuFrame.add(devMenuPanel, BorderLayout.SOUTH);

        mainFrame.add(bottomWrapperPanel, BorderLayout.SOUTH);
        mainFrame.add(canvas, BorderLayout.CENTER);
        mainFrame.pack();

        mainFrame.setVisible(true);

        menuPanel.setBorder(menuPanelBorder);
        devMenuPanel.setBorder(menuPanelBorder);
    }

    public static int getWidth() {
        return mainFrame.getWidth();
    }

    public static int getHeight() {
        return mainFrame.getHeight();
    }

    public static JFrame getMenuFrame() {
        return menuFrame;
    }

    public static JFrame getMainFrame() {
        return mainFrame;
    }
}
