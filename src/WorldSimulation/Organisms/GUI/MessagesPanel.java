package WorldSimulation.Organisms.GUI;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;

public class MessagesPanel extends JPanel {
    private static int CELL_PX = 25;
    private static int MESSAGES_PX = 200;
    public MessagesPanel(int rows) {
        setPreferredSize(new Dimension(MESSAGES_PX, rows * CELL_PX));
        setBackground(Color.BLACK);
    }
    public void displayMessages(String message) {
        JTextArea textArea = new JTextArea(message);
        textArea.setEditable(false);
        textArea.setForeground(Color.WHITE);
        textArea.setBackground(Color.BLACK);
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        textArea.setSize(getSize());
        Border marginBorder = BorderFactory.createEmptyBorder(5, 5, 5, 5);
        textArea.setBorder(marginBorder);
        removeAll();
        add(textArea);
        revalidate();
        repaint();
    }
    public void dispose() {
        removeAll();
        setPreferredSize(new Dimension(0, 0));
        setBackground(null);
    }
}
