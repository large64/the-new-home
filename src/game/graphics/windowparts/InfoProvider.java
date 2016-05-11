package game.graphics.windowparts;

import javax.swing.*;
import javax.swing.text.DefaultCaret;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Dénes on 2016. 05. 11..
 */
public class InfoProvider {
    private static List<String> messages;
    private static JTextArea textArea;
    private static JPanel wrapperPanel;

    public InfoProvider() {
        messages = new ArrayList<>();
        FlowLayout layout = new FlowLayout(FlowLayout.LEFT, 0, 0);
        wrapperPanel = new JPanel(layout);

        wrapperPanel.setPreferredSize(new Dimension(200, Window.BOTTOM_COMPONENT_HEIGHT));

        textArea = new JTextArea();
        textArea.setBackground(Color.BLACK);
        textArea.setForeground(Color.WHITE);
        textArea.setWrapStyleWord(true);
        textArea.setLineWrap(true);
        textArea.setEditable(false);

        // Always scroll to bottom
        DefaultCaret caret = (DefaultCaret)textArea.getCaret();
        caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);

        textArea.setText("Welcome to The New Home. Tips and messages will be shown here." + '\n');

        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.getViewport().setOpaque(false);
        scrollPane.setPreferredSize(new Dimension(200, Window.BOTTOM_COMPONENT_HEIGHT));
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        wrapperPanel.add(scrollPane);
    }

    public static JPanel getWrapperPanel() {
        return wrapperPanel;
    }

    public static void writeMessage(String message) {
        String toAppend = "--------------------------------------------" + '\n';
        messages.add(message);
        textArea.append(toAppend + message + '\n');
    }
}
