package toolbox;

import javax.swing.*;
import java.awt.*;

/**
 * Created by large64 on 17/10/15.
 */
public class Screen extends JFrame {
    private Canvas canvas;

    public Screen(Canvas canvas) {
        this.canvas = canvas;
        GridBagLayout layout = new GridBagLayout();
        layout.minimumLayoutSize(this);
        this.setLayout(layout);
        this.setSize(canvas.getSize());
        this.add(canvas);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JButton button = new JButton("Test");
        JButton button1 = new JButton("Test2");

        JPanel menuPanel = new JPanel();
        menuPanel.setLayout(new GridLayout(2, 1));

        menuPanel.add(button);
        menuPanel.add(button1);
        this.add(menuPanel);
        this.pack();
        this.setVisible(true);
    }

    public Canvas getCanvas() {
        return canvas;
    }

    public void setCanvas(Canvas canvas) {
        this.canvas = canvas;
    }
}
