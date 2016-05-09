package game.graphics.windowparts;

import game.graphics.entities.Camera;
import game.graphics.toolbox.GameMode;
import game.graphics.windowparts.BuildingPanel.BuildingPanel;
import game.graphics.windowparts.BuildingPanel.BuildingPanelButton;
import game.logic.toolbox.GameLoader;
import game.logic.toolbox.GameSaver;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ItemEvent;

/**
 * Created by large64 on 31/10/15.
 */
public class Window {
    private static JFrame mainFrame;
    private static JFrame menuFrame;
    private static JPanel menuPanel;
    private static boolean isTilesShown = false;
    private static JComboBox gameModeList;

    static final int BOTTOM_COMPONENT_HEIGHT = 130;

    public Window(Canvas canvas) {
        Color menuBgColor = new Color(2, 120, 0);
        Color menuItemBg = new Color(205, 221, 237);

        EmptyBorder menuPanelBorder = new EmptyBorder(10, 20, 20, 20);
        menuPanel = new JPanel(new GridLayout(6, 1, 0, 5));

        loadDefaultMenu();

        String[] gameModes = GameMode.getGameModes();
        gameModeList = new JComboBox<>(gameModes);

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

        JPanel devMenuPanel = new JPanel(new GridLayout(2, 1));
        JPanel indicatorPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JPanel gameModePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JPanel miniMapPanel = new JPanel(new BorderLayout());

        mainFrame = new JFrame();
        mainFrame.setLayout(new BorderLayout());
        mainFrame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        mainFrame.setUndecorated(true);
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        initializeMenuFrame(menuBgColor);

        new MiniMap();

        indicatorPanel.setOpaque(false);
        indicatorPanel.setBorder(BorderFactory.createLineBorder(new Color(186, 186, 186), 1, true));

        JLabel devMenuTitle = new JLabel("Developers' menu");
        devMenuTitle.setHorizontalAlignment(JLabel.CENTER);

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

        new BuildingPanel();
        BuildingPanel.addButton(new BuildingPanelButton("home", "Home"));
        BuildingPanel.addButton(new BuildingPanelButton("hospital", "Hospital"));
        BuildingPanel.addButton(new BuildingPanelButton("barrack", "Barrack"));

        JPanel bottomWrapperPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bottomWrapperPanel.setBackground(menuBgColor);
        bottomWrapperPanel.setPreferredSize(new Dimension(mainFrame.getWidth(), 150));
        bottomWrapperPanel.add(BuildingPanel.getPanel());
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

    public static int getBottomComponentHeight() {
        return BOTTOM_COMPONENT_HEIGHT;
    }

    private static void initializeMenuFrame(Color menuBgColor) {
        menuFrame = new JFrame();
        menuFrame.setLayout(new BorderLayout());
        menuFrame.setBackground(menuBgColor);
        // Set menu panel size
        menuFrame.setPreferredSize(new Dimension(190, 400));
        menuFrame.setUndecorated(true);
        menuFrame.pack();
        menuFrame.setLocationRelativeTo(null);
        menuFrame.setAlwaysOnTop(true);
    }

    public static void switchMenuFrameContent(Component component) {
        if (component != null) {
            menuPanel.removeAll();
            menuPanel.add(component);
            menuPanel.revalidate();
            menuPanel.repaint();
        }
        else {
            loadDefaultMenu();
        }
    }

    public static void loadDefaultMenu() {
        menuPanel.removeAll();

        JButton button = new JButton("New Game");
        button.addActionListener(e -> {
            /*Scene.setEntities(GameLoader.load("save0"));
            Scene.setGameMode(GameMode.ONGOING);*/
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

        JButton button4 = new JButton("Save Game");
        button4.addActionListener(e -> {
            GameSaver.save();
        });

        JButton button5 = new JButton("Load Game");
        button5.addActionListener(e -> {
            switchMenuFrameContent(GameLoader.getLoaderPanel());
        });

        JLabel menuTitle = new JLabel("Main Menu");
        menuTitle.setHorizontalAlignment(JLabel.CENTER);

        menuPanel.add(menuTitle);
        menuPanel.add(button3);
        menuPanel.add(button);
        menuPanel.add(button4);
        menuPanel.add(button5);
        menuPanel.add(button2);

        menuPanel.revalidate();
        menuPanel.repaint();
    }
}
