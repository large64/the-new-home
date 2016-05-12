package game.graphics.windowparts.infopanels;

import com.sun.istack.internal.Nullable;
import game.logic.entities.RawEntity;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
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
        FlowLayout layout = new FlowLayout(FlowLayout.CENTER);
        layout.setVgap(0);
        wrapperPanel = new JPanel(layout);
        wrapperPanel.setOpaque(false);

        singlePanel = new JPanel(new BorderLayout());
        multiPanel = new JPanel(new GridLayout(MULTI_SIZE / 4, MULTI_SIZE / 2, 2, 2));

        singlePanel.setPreferredSize(new Dimension(150, game.graphics.windowparts.Window.BOTTOM_COMPONENT_HEIGHT));
        multiPanel.setPreferredSize(new Dimension(220, game.graphics.windowparts.Window.BOTTOM_COMPONENT_HEIGHT));

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
        wrapperPanel.setBorder(new LineBorder(Color.WHITE));
        wrapperPanel.setPreferredSize(new Dimension(150, game.graphics.windowparts.Window.BOTTOM_COMPONENT_HEIGHT));
        JLabel placeholderLabel = new JLabel("Select an entity");
        placeholderLabel.setForeground(Color.WHITE);
        placeholderLabel.setBorder(new EmptyBorder(50, 0, 0, 0));
        wrapperPanel.add(placeholderLabel);
    }

    public static void lookForChanges() {
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
            wrapperPanel.setPreferredSize(new Dimension(150, game.graphics.windowparts.Window.BOTTOM_COMPONENT_HEIGHT));
        } else if (entities.size() > 1) {
            singlePanel.setVisible(false);
            for (int i = 0; i < entities.size(); i++) {
                RawEntity entity = entities.get(i);
                String[] typeAndImage = determineTypeAndImage(entity);

                if (typeAndImage != null) {
                    JLabel label = ((JLabel) multiPanel.getComponent(i));
                    label.setIcon(new ImageIcon(typeAndImage[1]));
                    label.setText(String.valueOf(entity.getHealth()));
                    label.setHorizontalTextPosition(JLabel.CENTER);
                    label.setVerticalTextPosition(JLabel.BOTTOM);
                    label.setIconTextGap(-2);
                    label.setPreferredSize(new Dimension(50, 50));
                }
            }
            multiPanel.setVisible(true);
            wrapperPanel.setPreferredSize(new Dimension(220, game.graphics.windowparts.Window.BOTTOM_COMPONENT_HEIGHT));
        } else {
            singlePanel.setVisible(false);
            multiPanel.setVisible(false);
            wrapperPanel.setPreferredSize(new Dimension(150, game.graphics.windowparts.Window.BOTTOM_COMPONENT_HEIGHT));
        }
    }

    @Nullable
    private static String[] determineTypeAndImage(RawEntity entity) {
        switch (entity.getClass().getSimpleName()) {
            case "RawSoldier":
                return new String[]{"Soldier", "res/soldier_profile.png"};
            case "RawHealer":
                return new String[]{"Healer", "res/healer_profile.png"};
            case "RawScientist":
                return new String[]{"Scientist", "res/scientist_profile.png"};
            case "RawHome":
                return new String[]{"Home", "res/home_profile.png"};
            case "RawHospital":
                return new String[]{"Hospital", "res/hospital_profile.png"};
            case "RawBarrack":
                return new String[]{"Barrack", "res/barrack_profile.png"};
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

    public JPanel getWrapperPanel() {
        return wrapperPanel;
    }
}
