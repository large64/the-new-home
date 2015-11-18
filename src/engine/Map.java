package engine;

import engine.entities.Entity;
import engine.toolbox.Position;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;

/**
 * Created by Dénes on 2015. 11. 06..
 */
public class Map {
    private static final int SIZE = 5;
    private static boolean[] places;
    private static List entities;
    private static JFrame frame;
    private static JPanel panel;

    public Map(List entities) {
        places = new boolean[SIZE * SIZE];
        frame = new JFrame("The New Home - engine tester");
        Map.entities = entities;


        panel = new JPanel();
        GridLayout gridLayout = new GridLayout(SIZE, SIZE);
        gridLayout.setHgap(2);
        gridLayout.setVgap(2);
        panel.setLayout(gridLayout);
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        //frame.setUndecorated(true);
        repaint(true);
        frame.add(panel);
        frame.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                super.keyPressed(e);
                if (e.getKeyCode() == 27) {
                    frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
                }
                //System.out.println(e.getKeyCode());
            }
        });
        /*frame.pack();
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);*/
    }

    private static void repaint(boolean first) {
        for (int i = 0; i < getSize(); i++) {
            JTextPane toAdd;
            places[i] = true;
            if (first) {
                toAdd = new JTextPane();
            }
            else {
                toAdd = (JTextPane) panel.getComponent(i);
            }

            toAdd.setEnabled(false);
            toAdd.setDisabledTextColor(new Color(0, 0, 0));
            toAdd.setText("");

            for (int j = 0; j < entities.size(); ++j) {
                Entity entity = (Entity) entities.get(j);
                if (entity.isAlive() && entity.getPosition().convertToMatrixPosition() == i) {
                    String text = entity.getID();
                    if (entity.isBeingAttacked()) {
                        text += " " + entity.getHealth();
                    }
                    else {
                        entity.setBeingAttacked(false);
                    }
                    toAdd.setText(text);
                    places[i] = false;
                }
            }
            if (first) {
                panel.add(toAdd);
            }
        }
    }

    public static void lookForChanges() {
        //panel.removeAll();
        repaint(false);
        panel.revalidate();
    }

    public static int getSize() {
        return (SIZE * SIZE);
    }

    /**
     * Returns the number of rows of the map matrix that equals to the number of columns
     */
    public static int getRowNumber() {
        return SIZE;
    }

    public static List getEntities() {
        return entities;
    }

    public static boolean isPositionFree(Position position) {
        return places[position.convertToMatrixPosition()];
    }
}
