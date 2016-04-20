package game.graphics.windowparts;

import game.graphics.entities.Camera;
import game.graphics.toolbox.GameMode;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

/**
 * Created by large64 on 31/10/15.
 */
public class Window {
    private static JFrame mainFrame;
    private static JFrame menuFrame;
    private static boolean isTilesShown = false;
    private static JComboBox gameModeList;
    private static JPanel builderPanel;

    static final int BOTTOM_COMPONENT_HEIGHT = 130;

    public Window(Canvas canvas) {
        Color menuBgColor = new Color(2, 120, 0);
        Color menuItemBg = new Color(205, 221, 237);

        EmptyBorder menuPanelBorder = new EmptyBorder(10, 20, 20, 20);

        String[] gameModes = GameMode.getGameModes();
        gameModeList = new JComboBox<>(gameModes);

        JButton button = new JButton("New Game");
        button.addActionListener(e -> {
            Scene.restart();
            Scene.setGameMode(GameMode.ONGOING);
        });

        JButton button2 = new JButton("Quit");
        button2.addActionListener(e1 -> {
            mainFrame.dispatchEvent(new java.awt.event.WindowEvent(mainFrame, java.awt.event.WindowEvent.WINDOW_CLOSING));
        });

        JButton button3 = new JButton("Resume");
        button3.addActionListener(el ->{
            menuFrame.setVisible(false);
            Camera.setIsMouseGrabbed(true);
            Scene.setGameMode(GameMode.ONGOING);
        });

        JCheckBox tilesCheckbox = new JCheckBox("Show tiles");
        JPanel checkboxPanel = new JPanel();

        checkboxPanel.setBorder(BorderFactory.createLineBorder(new Color(122, 138, 153), 1, false));
        checkboxPanel.setBackground(menuItemBg);
        tilesCheckbox.setBackground(menuItemBg);
        tilesCheckbox.setBorder(new EmptyBorder(0, 10, 0, 10));
        tilesCheckbox.addActionListener(el ->{
            if (!isTilesShown) {
                Scene.getMainMap().setTilesShown(true);
                isTilesShown = true;
            }
            else {
                Scene.getMainMap().setTilesShown(false);
                isTilesShown = false;
            }
        });
        checkboxPanel.add(tilesCheckbox);

        PositionInfo positionInfo = new PositionInfo();
        EntityInfo entityInfo = new EntityInfo();
        ActionInfo actionInfo = new ActionInfo();

        JPanel menuPanel = new JPanel(new GridLayout(5, 1));
        JPanel devMenuPanel = new JPanel(new GridLayout(2, 1));
        JPanel indicatorPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JPanel gameModePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
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
        menuFrame.setAlwaysOnTop(true);

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
        menuPanel.add(button2);

        devMenuPanel.add(devMenuTitle);
        devMenuPanel.add(checkboxPanel);

        indicatorPanel.add(positionInfo.getTextPane());
        indicatorPanel.setPreferredSize(new Dimension(120, BOTTOM_COMPONENT_HEIGHT));


        gameModeList.setEnabled(false);
        gameModeList.setPreferredSize(new Dimension(110, 20));
        gameModeList.addItemListener(e -> {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                Object item = e.getItem();
                switch (item.toString()) {
                    case "Play":
                        Scene.setGameMode(GameMode.ONGOING);
                        break;
                    case "Build":
                        Scene.setGameMode(GameMode.BUILDING);
                        break;
                }
            }
        });
        gameModePanel.setPreferredSize(new Dimension(120, BOTTOM_COMPONENT_HEIGHT));
        gameModePanel.setOpaque(false);
        JLabel gameModeTitleLabel = new JLabel("Switch game mode:");
        gameModeTitleLabel.setForeground(Color.WHITE);
        gameModePanel.add(gameModeTitleLabel);
        gameModePanel.add(gameModeList);

        miniMapPanel.add(new JLabel("Mini map"), BorderLayout.NORTH);
        miniMapPanel.add(MiniMap.getLabel(), BorderLayout.CENTER);

        builderPanel = new JPanel(new GridLayout(2, 4));
        builderPanel.setPreferredSize(new Dimension(400, BOTTOM_COMPONENT_HEIGHT));
        builderPanel.setOpaque(false);
        builderPanel.setVisible(false);
        builderPanel.setBorder(new EmptyBorder(0, 0, 0, 10));

        for (int i = 0; i < 8; i++) {
            builderPanel.add(new JButton(Integer.toString(i)));
        }

        JPanel bottomWrapperPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bottomWrapperPanel.setBackground(menuBgColor);
        bottomWrapperPanel.setPreferredSize(new Dimension(mainFrame.getWidth(), 150));
        bottomWrapperPanel.add(builderPanel);
        bottomWrapperPanel.add(actionInfo.getWrapperPanel());
        bottomWrapperPanel.add(entityInfo.getWrapperPanel());
        bottomWrapperPanel.add(indicatorPanel);
        bottomWrapperPanel.add(gameModePanel);
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

    static JComboBox getGameModeList() {
        return gameModeList;
    }

    public static void setBuilderPanelVisible() {
        if (!builderPanel.isVisible()) {
            builderPanel.setVisible(true);
        }
    }

    public static void setBuilderPanelInvisible() {
        if (builderPanel.isVisible()) {
            builderPanel.setVisible(false);
        }
    }
}
