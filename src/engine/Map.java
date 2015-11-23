package engine;

import engine.entities.Entity;
import engine.toolbox.Position;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Dénes on 2015. 11. 06..
 */
public class Map {
    private static final Color BASE_TILE_COLOR = new Color(0, 0, 0);
    private static final Color BEING_ATTACKED_COLOR = new Color(255, 0, 0);
    private static final Color MARKED_COLOR = new Color(0, 0, 150);

    private static int size = 20;
    private static boolean[] places;
    private static List entities;
    private static JFrame frame;
    private static JPanel panel;
    private static List<Integer> markers = new ArrayList<>();

    public Map(List entities) {
        places = new boolean[size * size];
        frame = new JFrame("The New Home - engine tester");
        Map.entities = entities;
        panel = new JPanel();
        GridLayout gridLayout = new GridLayout(size, size);

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
        frame.pack();
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
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
            toAdd.setDisabledTextColor(new Color(255, 255, 255));
            toAdd.setText("");
            toAdd.setBackground(BASE_TILE_COLOR);

            for (Object object : entities) {
                Entity entity = (Entity) object;

                if (entity.isAlive() && entity.getPosition().convertToMatrixPosition() == i) {
                    String text = entity.toString();

                    if (!entity.isBeingAttacked()) {
                        entity.setBeingAttacked(false);
                    }
                    else {
                        toAdd.setBackground(BEING_ATTACKED_COLOR);
                    }

                    toAdd.setText(text);
                    places[i] = false;
                }
            }
            if (markers.contains(i)) {
                toAdd.setBackground(MARKED_COLOR);
            }
            if (first) {
                panel.add(toAdd);
            }
        }
    }

    public synchronized static void lookForChanges() {
        repaint(false);
        panel.revalidate();
    }

    public static int getSize() {
        return (size * size);
    }

    /**
     * Returns the number of rows of the map matrix that equals to the number of columns
     */
    public static int getRowNumber() {
        return size;
    }

    public static List getEntities() {
        return entities;
    }

    public static boolean isPositionFree(Position position) {
        return places[position.convertToMatrixPosition()];
    }

    public static boolean isPositionFree(Position position, boolean isDestination) {
        return isDestination || places[position.convertToMatrixPosition()];
    }

    public static void mark(int position) {
        markers.add(position);
    }

    public static void setSize(int size) {
        Map.size = size;
    }
}
