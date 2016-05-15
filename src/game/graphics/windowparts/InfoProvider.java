package game.graphics.windowparts;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.text.DefaultCaret;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class InfoProvider {
    private static List<String> messages;
    private static JTextArea textArea;
    private static JPanel wrapperPanel;

    InfoProvider() {
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
        textArea.setBorder(new EmptyBorder(5, 5, 5, 5));

        // Always scroll to bottom
        DefaultCaret caret = (DefaultCaret) textArea.getCaret();
        caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);

        textArea.setText("Welcome to The New Home! Tips and messages will be shown here." + '\n');

        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.getViewport().setOpaque(false);
        scrollPane.setPreferredSize(new Dimension(200, Window.BOTTOM_COMPONENT_HEIGHT));
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        wrapperPanel.add(scrollPane);
    }

    static JPanel getWrapperPanel() {
        return wrapperPanel;
    }

    public static void writeMessage(String message) {
        String previousMessage = "";

        if (!messages.isEmpty()) {
            previousMessage = messages.get(messages.size() - 1);
        }

        // Prevent duplicates
        if (!previousMessage.equals(message)) {
            String timeStamp = new SimpleDateFormat("HH:mm:ss").format(new Date());

            String toAppend = "-------------------------------------------" + '\n';
            messages.add(message);
            textArea.append(toAppend + timeStamp + ": " + message + '\n');
        }
    }

    public static void clear() {
        messages.clear();
        textArea.setText("Welcome to The New Home! Tips and messages will be shown here." + '\n');
    }
}
