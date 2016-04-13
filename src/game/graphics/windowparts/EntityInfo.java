package game.graphics.windowparts;

import com.sun.istack.internal.Nullable;
import game.graphics.renderers.MasterRenderer;
import game.logic.entities.RawEntity;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

/**
 * Created by Dénes on 2015. 12. 07..
 */
public class EntityInfo {
    public static final int MULTI_SIZE = 8;

    private static JPanel wrapperPanel;
    private static JPanel singlePanel;
    private static JPanel multiPanel;
    private static List<RawEntity> entities;

    private static JLabel nameLabel;
    private static JLabel sideLabel;
    private static JLabel healthLabel;
    private static JLabel tileLabel;
    private static JLabel imageLabel;

    public EntityInfo() {
        wrapperPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        wrapperPanel.setOpaque(false);

        singlePanel = new JPanel(new BorderLayout(2, 2));
        multiPanel = new JPanel(new GridLayout(MULTI_SIZE / 4, MULTI_SIZE / 2, 2, 2));

        singlePanel.setPreferredSize(new Dimension(150, Window.BOTTOM_COMPONENT_HEIGHT));
        multiPanel.setPreferredSize(new Dimension(220, Window.BOTTOM_COMPONENT_HEIGHT));

        singlePanel.setVisible(false);
        multiPanel.setVisible(false);

        EmptyBorder border = new EmptyBorder(3, 3, 3, 3);
        singlePanel.setBorder(border);
        multiPanel.setBorder(border);

        nameLabel = new JLabel();
        sideLabel = new JLabel();
        healthLabel = new JLabel();
        tileLabel = new JLabel();
        imageLabel = new JLabel();
        nameLabel.setHorizontalAlignment(JLabel.CENTER);
        imageLabel.setVerticalAlignment(JLabel.CENTER);
        imageLabel.setHorizontalAlignment(JLabel.CENTER);

        singlePanel.add(nameLabel, BorderLayout.NORTH);
        JPanel bottomPanel = new JPanel(new GridLayout(1, 3, 5, 0));
        bottomPanel.add(healthLabel);
        bottomPanel.add(tileLabel);
        bottomPanel.add(sideLabel);
        singlePanel.add(bottomPanel, BorderLayout.SOUTH);
        singlePanel.add(imageLabel, BorderLayout.CENTER);

        for (int i = 0; i < MULTI_SIZE; i++) {
            multiPanel.add(new JLabel());
        }

        wrapperPanel.add(singlePanel);
        wrapperPanel.add(multiPanel);
    }

    public JPanel getWrapperPanel() {
        return wrapperPanel;
    }

    public static void refreshInfo() {
        if (entities.size() == 1) {
            multiPanel.setVisible(false);
            removeMultiPanelIcons();
            RawEntity entity = entities.get(0);
            String[] typeAndImage = determineTypeAndImage(entity);

            if (typeAndImage != null) {
                nameLabel.setText(typeAndImage[0]);
                imageLabel.setIcon(new ImageIcon(typeAndImage[1]));
            }
            sideLabel.setText(entity.getSide().toString());
            healthLabel.setText(String.valueOf(entity.getHealth()));
            tileLabel.setText(entity.getTilePosition().toString());

            singlePanel.setVisible(true);
        }
        else if (entities.size() > 1) {
            singlePanel.setVisible(false);
            for (int i = 0; i < entities.size(); i++) {
                RawEntity entity = entities.get(i);
                RawEntity selectableEntity = Scene.getSelectedEntities().get(i);
                String[] typeAndImage = determineTypeAndImage(entity);

                if (typeAndImage != null) {
                    JLabel label = ((JLabel) multiPanel.getComponent(i));
                    label.setIcon(new ImageIcon(typeAndImage[1]));
                    label.setText(String.valueOf(entity.getHealth()));
                    label.setHorizontalTextPosition(JLabel.CENTER);
                    label.setVerticalTextPosition(JLabel.BOTTOM);
                    label.setIconTextGap(-2);
                    label.setPreferredSize(new Dimension(50, 50));
                    label.addMouseListener(new MouseAdapter() {
                        @Override
                        public void mouseClicked(MouseEvent e) {
                            Scene.unSelectAllEntities();
                            Scene.getSelectedEntities().add(selectableEntity);
                            selectableEntity.getEntity().setSelected(true);
                        }
                    });
                }
            }
            multiPanel.setVisible(true);
        }
        else {
            singlePanel.setVisible(false);
            multiPanel.setVisible(false);
        }
    }

    @Nullable
    private static String[] determineTypeAndImage(RawEntity entity) {
        switch (entity.getClass().getSimpleName()) {
            case "RawSoldier":
                return new String[] {"Soldier", "res/soldier_profile.png"};
            case "RawHealer":
                return new String[] {"Healer", "res/healer_profile.png"};
            default:
                return null;
        }
    }

    public static void setEntities(List<RawEntity> entities) {
        EntityInfo.entities = entities;
    }

    private static void removeMultiPanelIcons() {
        for (int i = 0; i < MULTI_SIZE; i++) {
            JLabel label = ((JLabel) multiPanel.getComponent(i));
            label.setIcon(null);
            label.setText(null);
        }
    }
}
