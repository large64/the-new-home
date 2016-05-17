package game.graphics.windowparts;

import game.graphics.entities.Camera;
import game.graphics.entities.Entity;
import game.graphics.entities.Player;
import game.graphics.entities.buildings.Home;
import game.graphics.entities.units.Scientist;
import game.graphics.entities.units.Soldier;
import game.graphics.models.TexturedModel;
import game.graphics.toolbox.GameMode;
import game.graphics.windowparts.buildingpanel.BuildingPanel;
import game.graphics.windowparts.buildingpanel.BuildingPanelButton;
import game.graphics.windowparts.infopanels.ActionInfo;
import game.graphics.windowparts.infopanels.EntityInfo;
import game.graphics.windowparts.infopanels.PositionInfo;
import game.logic.entities.RawMap;
import game.logic.toolbox.*;
import org.lwjgl.util.vector.Vector3f;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Window {
    public static final int BOTTOM_COMPONENT_HEIGHT = 130;
    private static final Color MENU_BG_COLOR = new Color(2, 120, 0);
    private static final Color MENU_ITEM_BG_COLOR = new Color(205, 221, 237);
    private static JFrame mainFrame;
    private static JFrame menuFrame;
    private static JPanel menuPanel;
    private static JPanel entityInfoPanel;
    private static JCheckBox tilesCheckbox;
    private static boolean isTilesShown = false;
    private static JComboBox gameModeList;

    public Window(Canvas canvas) {

        EmptyBorder menuPanelBorder = new EmptyBorder(10, 20, 20, 20);
        menuPanel = new JPanel(new GridLayout(6, 1, 0, 5));

        loadDefaultMenu();

        tilesCheckbox = new JCheckBox("Show tiles");
        JPanel checkboxPanel = new JPanel();

        checkboxPanel.setBorder(BorderFactory.createLineBorder(new Color(122, 138, 153), 1, false));
        checkboxPanel.setBackground(MENU_ITEM_BG_COLOR);
        tilesCheckbox.setBackground(MENU_ITEM_BG_COLOR);
        tilesCheckbox.setBorder(new EmptyBorder(0, 10, 0, 10));
        tilesCheckbox.addActionListener(el -> {
            if (!isTilesShown) {
                Scene.getMainMap().setTilesShown(true);
                isTilesShown = true;
            } else {
                Scene.getMainMap().setTilesShown(false);
                isTilesShown = false;
            }
        });
        checkboxPanel.add(tilesCheckbox);

        JPanel devMenuPanel = new JPanel(new GridLayout(2, 1));

        mainFrame = new JFrame();
        mainFrame.setLayout(new BorderLayout());
        mainFrame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        mainFrame.setUndecorated(true);
        mainFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        initializeMenuFrame();
        new MiniMap();

        JLabel devMenuTitle = new JLabel("Developers' menu");
        devMenuTitle.setHorizontalAlignment(JLabel.CENTER);

        devMenuPanel.add(devMenuTitle);
        devMenuPanel.add(checkboxPanel);

        initializeBuildingPanel();

        menuFrame.add(menuPanel, BorderLayout.NORTH);
        menuFrame.add(devMenuPanel, BorderLayout.SOUTH);

        mainFrame.add(initializeBottomWrapper(), BorderLayout.SOUTH);
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

    static JFrame getMenuFrame() {
        return menuFrame;
    }

    static JComboBox getGameModeList() {
        return gameModeList;
    }

    private static void initializeMenuFrame() {
        menuFrame = new JFrame();
        menuFrame.setLayout(new BorderLayout());
        menuFrame.setBackground(MENU_BG_COLOR);
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
        } else {
            loadDefaultMenu();
        }
    }

    public static void loadDefaultMenu() {
        menuPanel.removeAll();

        JButton newGameButton = new JButton("New Game");
        newGameButton.addActionListener(e -> switchMenuFrameContent(Window.getNewGamePanel()));

        JButton quitButton = new JButton("Quit");
        quitButton.addActionListener(e1 -> mainFrame.dispatchEvent(new java.awt.event.WindowEvent(mainFrame, java.awt.event.WindowEvent.WINDOW_CLOSING)));

        JButton resumeButton = new JButton("Resume");
        resumeButton.addActionListener(el -> {
            menuFrame.setVisible(false);
            Player.setIsMouseGrabbed(true);
            Scene.setGameMode(GameMode.ONGOING);
        });

        JButton saveGameButton = new JButton("Save Game");
        saveGameButton.addActionListener(e -> GameSaver.save());

        JButton loadGameButton = new JButton("Load Game");
        loadGameButton.addActionListener(e -> switchMenuFrameContent(GameLoader.getLoaderPanel()));

        JLabel menuTitle = new JLabel("Main Menu");
        menuTitle.setHorizontalAlignment(JLabel.CENTER);

        menuPanel.add(menuTitle);
        menuPanel.add(resumeButton);
        menuPanel.add(newGameButton);
        menuPanel.add(saveGameButton);
        menuPanel.add(loadGameButton);
        menuPanel.add(quitButton);

        menuPanel.revalidate();
        menuPanel.repaint();
    }

    private static JPanel initializeBottomWrapper() {
        PositionInfo positionInfo = new PositionInfo();
        EntityInfo entityInfo = new EntityInfo();
        ActionInfo actionInfo = new ActionInfo();

        JPanel indicatorPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        indicatorPanel.setOpaque(false);
        indicatorPanel.setBorder(BorderFactory.createLineBorder(new Color(186, 186, 186), 1, true));
        indicatorPanel.add(positionInfo.getTextPane());
        indicatorPanel.setPreferredSize(new Dimension(120, BOTTOM_COMPONENT_HEIGHT));

        String[] gameModes = GameMode.getGameModes();
        gameModeList = new JComboBox<>(gameModes);
        JPanel gameModePanel = new JPanel(new FlowLayout());
        gameModeList.setEnabled(false);
        gameModeList.setPreferredSize(new Dimension(110, 20));
        gameModeList.addItemListener(e -> {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                Object item = e.getItem();
                GameObserver.lookForChanges();
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
        JLabel placeholderLabel = new JLabel("Menu");
        placeholderLabel.setForeground(Color.WHITE);
        gameModePanel.add(placeholderLabel);
        JButton menuButton = new JButton("Main menu");
        menuButton.addActionListener(e -> showMenu());
        gameModePanel.add(menuButton);

        JPanel miniMapPanel = new JPanel(new BorderLayout());
        JLabel title = new JLabel("Mini map");
        title.setHorizontalAlignment(JLabel.CENTER);
        title.setVerticalAlignment(JLabel.CENTER);
        miniMapPanel.add(title, BorderLayout.NORTH);
        miniMapPanel.add(MiniMap.getLabel(), BorderLayout.CENTER);

        JPanel bottomWrapperPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bottomWrapperPanel.setBackground(MENU_BG_COLOR);
        bottomWrapperPanel.setPreferredSize(new Dimension(mainFrame.getWidth(), 150));
        entityInfoPanel = entityInfo.getWrapperPanel();

        new UnitCreator();
        new InfoProvider();

        bottomWrapperPanel.add(InfoProvider.getWrapperPanel());
        bottomWrapperPanel.add(BuildingPanel.getPanel());
        bottomWrapperPanel.add(actionInfo.getWrapperPanel());
        bottomWrapperPanel.add(UnitCreator.getWrapperPanel());
        bottomWrapperPanel.add(entityInfoPanel);
        bottomWrapperPanel.add(indicatorPanel);
        bottomWrapperPanel.add(gameModePanel);
        bottomWrapperPanel.add(miniMapPanel);

        return bottomWrapperPanel;
    }

    private static void initializeBuildingPanel() {
        new BuildingPanel();
        BuildingPanel.addButton(new BuildingPanelButton("home", "Home"));
        BuildingPanel.addButton(new BuildingPanelButton("hospital", "Hospital"));
        BuildingPanel.addButton(new BuildingPanelButton("barrack", "Barrack"));
    }

    public static JPanel getEntityInfoPanel() {
        return entityInfoPanel;
    }

    public static JCheckBox getTilesCheckbox() {
        return tilesCheckbox;
    }

    public static void showMenu() {
        Scene.setGameMode(GameMode.PAUSED);
        menuFrame.setVisible(true);
        menuFrame.setAlwaysOnTop(true);
        menuFrame.toFront();
        menuFrame.setFocusable(true);
        menuFrame.requestFocus();
        gameModeList.setEnabled(false);
    }

    public static JPanel getNewGamePanel() {
        JPanel newGamePanel = new JPanel();
        GridLayout gridLayout = new GridLayout(5, 1, 0, 10);
        newGamePanel.setLayout(gridLayout);

        JLabel title = new JLabel("New Game");
        title.setHorizontalAlignment(JLabel.CENTER);
        newGamePanel.add(title);

        JLabel loadLabel = new JLabel("With how many enemies?");
        loadLabel.setHorizontalAlignment(JLabel.CENTER);
        newGamePanel.add(loadLabel);

        String[] options = new String[]{"10", "15", "20"};

        JComboBox optionsComboBox = new JComboBox<>(options);
        newGamePanel.add(optionsComboBox);

        JButton loadButton = new JButton("Start");
        loadButton.setHorizontalAlignment(JButton.CENTER);
        loadButton.addActionListener(e -> {
            int numberOfEnemies = Integer.valueOf((String) optionsComboBox.getSelectedItem());
            switchMenuFrameContent(null);
            Scene.startGame(numberOfEnemies);
        });
        newGamePanel.add(loadButton);

        JButton button = new JButton("Cancel");
        button.setHorizontalAlignment(JButton.CENTER);
        button.addActionListener(e -> Window.loadDefaultMenu());
        newGamePanel.add(button);

        return newGamePanel;
    }
}
