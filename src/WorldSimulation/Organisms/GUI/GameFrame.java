package WorldSimulation.Organisms.GUI;

import WorldSimulation.World;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;

public class GameFrame extends JFrame implements KeyListener {
    private static int CELL_PX = 50;
    private static int MESSAGES_PX = 200;
    private int rows;
    private int cols;
    private BoardPanel boardPanel;
    private MessagesPanel messagesPanel;
    private World world;
    private int exit = 0;
    public GameFrame() {
        int width = readIntInput("Enter width (minimum 6, maximum 80):", 6, 80);
        int height = readIntInput("Enter height (minimum 6, maximum 50):", 6, 50);
        infoMessage();
        this.world = new World(height, width);
        this.rows = world.getRows();
        this.cols = world.getCols();
        boardPanel = new BoardPanel(world, this);
        messagesPanel = new MessagesPanel(rows);
        createFrame();
        displayMessages(world.throwMessages());
        requestFocusInWindow();
    }
    public void resetGUI() {
        removeKeyListener(this);
        dispose();
        rows = world.getRows();
        cols = world.getCols();
        boardPanel.dispose();
        messagesPanel.dispose();
        exit = 0;
        boardPanel = new BoardPanel(world, this);
        messagesPanel = new MessagesPanel(rows);
        createFrame();
        updateBoard();
        displayMessages(world.throwMessages());
        revalidate();
        repaint();
        setVisible(true);
        requestFocusInWindow();
    }
    private void createFrame() {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int screenWidth = screenSize.width;
        int screenHeight = screenSize.height;
        setSize(Math.min(cols * CELL_PX + MESSAGES_PX, screenWidth - 50), Math.min(rows * CELL_PX, screenHeight - 50));
        setLayout(new BorderLayout());
        add(messagesPanel, BorderLayout.EAST);
        add(boardPanel);
        setDefaultCloseOperation((JFrame.EXIT_ON_CLOSE));
        setTitle("World Simulation - Adrian Szwaczyk");
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                float fontSize = getHeight() / 80.0f;
                for (int y = 0; y < rows; y++) {
                    for (int x = 0; x < cols; x++) {
                        boardPanel.getBoardField(y, x).setFont(boardPanel.getBoardField(y, x).getFont().deriveFont(fontSize));
                    }
                }
            }
        });
        addKeyListener(this);
        setLocationRelativeTo(null);
        setVisible(true);
    }
    private void newGame() {
        dispose();
        int width = readIntInput("Enter width (minimum 6, maximum 80):", 6, 80);
        int height = readIntInput("Enter height (minimum 6, maximum 50):", 6, 50);
        infoMessage();
        world = new World(height, width);
        resetGUI();
    }
    @Override
    public void keyPressed(KeyEvent e) {
        int keyCode = e.getKeyCode();
        if (world.getDirection() != keyCode) {
            if (keyCode == KeyEvent.VK_UP || keyCode == KeyEvent.VK_DOWN || keyCode == KeyEvent.VK_LEFT || keyCode == KeyEvent.VK_RIGHT || (keyCode == KeyEvent.VK_R && world.ultable())) {
                world.setDirection(keyCode);
                exit = world.turn();
                updateBoard();
                displayMessages(world.throwMessages());
                if (exit == 1) {
                    int choice = JOptionPane.showConfirmDialog(null, "Do you want to start a new game?", "New Game Confirmation", JOptionPane.YES_NO_OPTION);
                    if (choice == JOptionPane.YES_OPTION) {
                        newGame();
                    } else {
                        System.exit(0);
                    }
                }
            } else if (keyCode == KeyEvent.VK_S) {
                String fileName = JOptionPane.showInputDialog(null, "Enter file name:");
                if (fileName != null) {
                    try {
                        world.saveDataToFile(fileName);
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                }
            } else if (keyCode == KeyEvent.VK_O) {
                String fileName = JOptionPane.showInputDialog(null, "Enter file name:");
                if (fileName != null) {
                    File file = new File("saves/" + fileName + ".txt");
                    if (file.exists()) {
                        world = new World("saves/" + fileName + ".txt");
                        resetGUI();
                    } else {
                        JOptionPane.showMessageDialog(null, "File does not exist: " + fileName);
                    }
                }
            }
            else if (keyCode == KeyEvent.VK_N) {
                newGame();
            } else if (keyCode == KeyEvent.VK_ESCAPE) {
                System.exit(0);
            }
        }
    }
    public void displayMessages(String s) {
        messagesPanel.displayMessages(s);
    }
    public void updateBoard() {
        boardPanel.updateBoard();
    }
    @Override
    public void keyReleased(KeyEvent e) {}

    @Override
    public void keyTyped(KeyEvent e) {}
    private static int readIntInput(String message, int minValue, int maxValue) {
        while (true) {
            try {
                String input = JOptionPane.showInputDialog(null, message);
                if (input != null) {
                    int value = Integer.parseInt(input);
                    if (value >= minValue && value <= maxValue) {
                        return value;
                    } else {
                        JOptionPane.showMessageDialog(null, "Input must be between " + minValue + " and " + maxValue + ". Please enter a valid integer.");
                    }
                } else {
                    System.exit(0);
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "Invalid input. Please enter a valid integer.");
            }
        }
    }
    @Override
    public void dispose() {
        getContentPane().removeAll();
        removeKeyListener(this);
        super.dispose();
    }
    private static void infoMessage() {
        JOptionPane.showMessageDialog(null, "Arrows - moving\nLMB click - adding an organism\nR - ult\nN - new game\nS - save to file\nO - open file\nEscape - exit", "Info", JOptionPane.INFORMATION_MESSAGE);
    }
}
