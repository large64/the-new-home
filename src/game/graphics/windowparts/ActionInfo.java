package game.graphics.windowparts;

import game.graphics.renderers.MasterRenderer;
import game.logic.entities.RawEntity;
import game.logic.entities.units.RawUnit;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

/**
 * Created by Dénes on 2016. 02. 20..
 */
public class ActionInfo {
    private JPanel wrapperPanel;
    private static ArrayList<JButton> actionButtons = new ArrayList<>();

    public ActionInfo() {
        wrapperPanel = new JPanel(new GridLayout(3, 3));
        wrapperPanel.setBorder(BorderFactory.createLineBorder(new Color(0, 0, 0), 1));
        wrapperPanel.setOpaque(false);

        loadButtons();
        for (JButton button : actionButtons) {
            wrapperPanel.add(button);
        }

        wrapperPanel.setPreferredSize(new Dimension(150, Window.BOTTOM_COMPONENT_HEIGHT));
        wrapperPanel.setMaximumSize(new Dimension(150, Window.BOTTOM_COMPONENT_HEIGHT));
    }

    private void loadButtons() {
        JButton button0 = new JButton("X");
        button0.addActionListener(el ->{
            if (!MasterRenderer.getSelectedEntities().isEmpty()) {
                for (RawEntity entity : MasterRenderer.getSelectedEntities()) {
                    if (entity instanceof RawUnit) {
                        RawUnit rawUnit = (RawUnit) entity;
                        if (rawUnit.isMoving()) {
                            rawUnit.getPath().clear();
                        }
                    }
                }
            }
        });
        actionButtons.add(button0);

        JButton button1 = new JButton("->");
        // @TODO: create actionlistener (move)
        actionButtons.add(button1);

        JButton button2 = new JButton("<->");
        // @TODO: create actionlistener (patrol)
        actionButtons.add(button2);

        JButton button3 = new JButton("ATK");
        // @TODO: create actionlistener (attack)
        actionButtons.add(button3);

        JButton button4 = new JButton();
        button4.setContentAreaFilled(false);
        button4.setBorder(BorderFactory.createLineBorder(new Color(195, 195, 195), 1));
        actionButtons.add(button4);

        JButton button5 = new JButton();
        button5.setContentAreaFilled(false);
        button5.setBorder(BorderFactory.createLineBorder(new Color(195, 195, 195), 1));
        actionButtons.add(button5);

        JButton button6 = new JButton();
        button6.setContentAreaFilled(false);
        button6.setBorder(BorderFactory.createLineBorder(new Color(195, 195, 195), 1));
        actionButtons.add(button6);

        JButton button7 = new JButton();
        button7.setContentAreaFilled(false);
        button7.setBorder(BorderFactory.createLineBorder(new Color(195, 195, 195), 1));
        actionButtons.add(button7);

        JButton button8 = new JButton();
        button8.setContentAreaFilled(false);
        button8.setBorder(BorderFactory.createLineBorder(new Color(195, 195, 195), 1));
        actionButtons.add(button8);
    }

    public JPanel getWrapperPanel() {
        return wrapperPanel;
    }
}
