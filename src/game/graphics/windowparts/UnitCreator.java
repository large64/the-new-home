package game.graphics.windowparts;

import game.graphics.entities.units.Healer;
import game.graphics.entities.units.Soldier;
import game.graphics.models.TexturedModel;
import game.graphics.toolbox.GameMode;
import game.logic.entities.RawEntity;
import game.logic.entities.buildings.RawBarrack;
import game.logic.entities.buildings.RawHospital;
import game.logic.exceptions.EnemyNearbyException;
import game.logic.exceptions.NotEnoughHousesException;
import game.logic.toolbox.GameObserver;
import game.logic.toolbox.Side;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Arrays;

class UnitCreator {
    private final static JLabel bottomLabel = new JLabel();
    private static JPanel wrapperPanel;
    private static JLabel hospitalLabel;
    private static JLabel barrackLabel;
    private static MouseListener createSoldierListener;
    private static MouseListener createHealerListener;
    private static boolean isEntityAdded = false;

    public UnitCreator() {
        wrapperPanel = new JPanel(new BorderLayout());

        JLabel titleLabel = new JLabel("Create entity:");
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);

        wrapperPanel.add(titleLabel, BorderLayout.NORTH);

        JPanel innerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));

        hospitalLabel = new JLabel(new ImageIcon("res/healer_profile.png"));
        hospitalLabel.setVisible(false);

        barrackLabel = new JLabel(new ImageIcon("res/soldier_profile.png"));
        barrackLabel.setVisible(false);

        innerPanel.add(hospitalLabel);
        innerPanel.add(barrackLabel);
        bottomLabel.setHorizontalAlignment(SwingConstants.CENTER);
        wrapperPanel.add(bottomLabel, BorderLayout.SOUTH);

        wrapperPanel.add(innerPanel, BorderLayout.CENTER);
        wrapperPanel.setPreferredSize(new Dimension(150, Window.BOTTOM_COMPONENT_HEIGHT));
        wrapperPanel.setBorder(new EmptyBorder(3, 3, 3, 3));
        wrapperPanel.setVisible(false);

        createHealerListener = new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                super.mouseReleased(e);
                if (!isEntityAdded) {
                    if (GameObserver.getNumberOfEnemyEntities() < 1) {
                        TexturedModel healerModel = Scene.getModelsMap().get("healerUnit");
                        Healer healer = new Healer(healerModel, 1, Side.FRIEND);
                        Scene.setLevitatingEntity(healer);
                        isEntityAdded = true;
                    } else {
                        InfoProvider.writeMessage("Cannot create unit now, you're under attack!");
                    }
                }
            }
        };

        createSoldierListener = new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                super.mouseReleased(e);
                if (!isEntityAdded) {
                    if (GameObserver.getNumberOfEnemyEntities() > 0) {
                        try {
                            throw new EnemyNearbyException();
                        } catch (EnemyNearbyException e1) {
                            InfoProvider.writeMessage("Cannot create unit now, enemies nearby!");
                        }
                    } else if ((GameObserver.getNumberOfHomes() * 5) - GameObserver.getNumberOfSoldiers() < 1) {
                        try {
                            throw new NotEnoughHousesException();
                        } catch (NotEnoughHousesException e1) {
                            InfoProvider.writeMessage("You need to build one more house to create a soldier.");
                        }
                    } else {
                        TexturedModel soldierModel = Scene.getModelsMap().get("soldierUnit");
                        Soldier soldier = new Soldier(soldierModel, 1, Side.FRIEND);
                        Scene.setLevitatingEntity(soldier);
                        isEntityAdded = true;
                    }
                }
            }
        };

        barrackLabel.addMouseListener(createSoldierListener);
        hospitalLabel.addMouseListener(createHealerListener);
    }

    static JPanel getWrapperPanel() {
        return wrapperPanel;
    }

    public static void lookForChanges() {
        java.util.List<RawEntity> selectedEntities = Scene.getSelectedEntities();

        if ((Scene.getGameMode().equals(GameMode.ONGOING) || !selectedEntities.isEmpty()) && selectedEntities.size() == 1) {
            isEntityAdded = false;
            RawEntity selectedEntity = selectedEntities.get(0);
            if (selectedEntity instanceof RawHospital) {
                hospitalLabel.setVisible(true);
                bottomLabel.setText("Healer");
                barrackLabel.setVisible(false);
                wrapperPanel.setVisible(true);
            } else if (selectedEntity instanceof RawBarrack) {
                hospitalLabel.setVisible(false);
                bottomLabel.setText("Soldier");
                barrackLabel.setVisible(true);
                wrapperPanel.setVisible(true);
            } else {
                wrapperPanel.setVisible(false);
                bottomLabel.setText(null);
            }
        } else {
            wrapperPanel.setVisible(false);
            bottomLabel.setText(null);
        }
    }
}
