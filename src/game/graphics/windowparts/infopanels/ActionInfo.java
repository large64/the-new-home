package game.graphics.windowparts.infopanels;

import game.graphics.windowparts.Scene;
import game.logic.entities.RawEntity;
import game.logic.entities.units.RawUnit;
import game.logic.toolbox.Side;
import game.logic.toolbox.map.Tile;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

/**
 * Created by Dénes on 2016. 02. 20..
 */
public class ActionInfo {
    private static ArrayList<JButton> actionButtons = new ArrayList<>();
    private static JPanel wrapperPanel;

    public ActionInfo() {
        wrapperPanel = new JPanel(new GridLayout(1, 2));
        wrapperPanel.setBorder(BorderFactory.createLineBorder(new Color(0, 0, 0), 1));
        wrapperPanel.setOpaque(false);

        loadButtons();
        for (JButton button : actionButtons) {
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
        actionButtons.add(stopButton);

        JButton attackButton = new JButton("ATK");
        attackButton.addActionListener(el -> {
            if (!Scene.getSelectedEntities().isEmpty()) {
                for (RawEntity rawEntity : Scene.getSelectedEntities()) {
                    if (rawEntity instanceof RawUnit) {
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
                    }
                }
            }
        });
        actionButtons.add(attackButton);
    }

    public JPanel getWrapperPanel() {
        return wrapperPanel;
    }
}
