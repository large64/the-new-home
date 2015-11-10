package engine;

import engine.entities.Entity;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;

/**
 * Created by Dénes on 2015. 11. 06..
 */
public class Map {
    private static final int SIZE = 20;
    private static int[][] places;
    private static List entities;
    private static JFrame frame;
    private static JPanel panel;
    private static final Color DEFAULT_COLOR = new Color(209, 209, 209);

    public Map(List entities) {
        places = new int[SIZE][SIZE];
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
        frame.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                System.out.println(e.getClickCount());
            }
        });
        frame.pack();
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    private static void repaint(boolean first) {
        for (int j = 0; j < getSize(); j++) {
            JTextPane toAdd;
            if (first) {
                toAdd = new JTextPane();
            }
            else {
                toAdd = (JTextPane) panel.getComponent(j);
            }

            toAdd.setEnabled(false);
            toAdd.setDisabledTextColor(new Color(0, 0, 0));
            toAdd.setText("");

            for (int i = 0; i < entities.size(); ++i) {
                Entity entity = (Entity) entities.get(i);
                if (entity.isAlive() && entity.getPosition().convertToMatrixPosition(SIZE) == j) {
                    String text = entity.getID();
                    if (entity.isBeingAttacked()) {
                        text += " " + entity.getHealth();
                    }
                    else {
                        entity.setBeingAttacked(false);
                    }
                    toAdd.setText(text);
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
}
