package game.graphics.windowparts.infopanels;

import game.graphics.windowparts.Scene;
import game.logic.entities.RawEntity;
import game.logic.entities.units.RawUnit;
import game.logic.toolbox.Side;
import game.logic.toolbox.map.Tile;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class ActionInfo {
    private static final ArrayList<JButton> ACTION_BUTTONS = new ArrayList<>();
    private static JPanel wrapperPanel;

    public ActionInfo() {
        wrapperPanel = new JPanel(new GridLayout(1, 2));
        wrapperPanel.setBorder(BorderFactory.createLineBorder(new Color(0, 0, 0), 1));
        wrapperPanel.setOpaque(false);

        loadButtons();
        for (JButton button : ACTION_BUTTONS) {
            button.setBackground(Color.DARK_GRAY);
            button.setForeground(Color.WHITE);
            wrapperPanel.add(button);
        }

        wrapperPanel.setPreferredSize(new Dimension(180, game.graphics.windowparts.Window.BOTTOM_COMPONENT_HEIGHT));
        wrapperPanel.setVisible(false);
    }

    public static void lookForChanges() {
        if (!Scene.getSelectedEntities().isEmpty()) {
            wrapperPanel.setVisible(true);
        } else {
            wrapperPanel.setVisible(false);
        }
    }

    private void loadButtons() {
        JButton stopButton = new JButton("X");
        stopButton.addActionListener(el -> {
            if (!Scene.getSelectedEntities().isEmpty()) {
                Scene.getSelectedEntities().stream().filter(entity -> entity instanceof RawUnit).forEach(entity -> {
                    RawUnit rawUnit = (RawUnit) entity;
                    if (rawUnit.isMoving()) {
                        rawUnit.getPath().clear();
                    }
                });
            }
        });
        ACTION_BUTTONS.add(stopButton);

        JButton attackButton = new JButton("ATK");
        attackButton.addActionListener(el -> {
            if (!Scene.getSelectedEntities().isEmpty()) {
                Scene.getSelectedEntities().stream().filter(rawEntity -> rawEntity instanceof RawUnit).forEach(rawEntity -> {
                    RawUnit rawUnit = (RawUnit) rawEntity;

                    if (rawEntity.getSide().equals(Side.FRIEND)) {
                        RawEntity randomRawEntity = Scene.getRandomEntity(Side.ENEMY);

                        if (randomRawEntity != null) {
                            int row = randomRawEntity.getTilePosition().getRow();
                            int column = randomRawEntity.getTilePosition().getColumn();

                            Tile destinationTile = new Tile(row, column);

                            rawUnit.setDestinationTile(destinationTile);
                            rawUnit.calculatePath();
                        }
                    }
                });
            }
        });
        ACTION_BUTTONS.add(attackButton);
    }

    public JPanel getWrapperPanel() {
        return wrapperPanel;
    }
}
