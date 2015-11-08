package engine;

import engine.entities.Entity;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Dénes on 2015. 11. 06..
 */
public class Map {
    private static final int SIZE = 40;
    private static int[][] places;
    private static List entities;
    private static JFrame frame;
    private static JPanel panel;

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
        init();
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
        frame.pack();
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    private static void init() {
        for (int j = 0; j < getSize(); j++) {
            JTextPane toAdd = new JTextPane();
            toAdd.setEnabled(false);
            toAdd.setDisabledTextColor(new Color(0, 0, 0));
            toAdd.setText("");

            for (int i = 0; i < entities.size(); ++i) {
                Entity entity = (Entity) entities.get(i);
                if (entity.getPosition().convertToMatrixPosition(SIZE) == j) {
                    toAdd.setText(entity.getID());
                }
            }
            panel.add(toAdd);
        }
    }

    public static void lookForChanges() {
        panel.removeAll();
        init();
        //panel.repaint();
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
