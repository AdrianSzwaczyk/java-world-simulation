package WorldSimulation.Organisms.GUI;

import WorldSimulation.Tools.ButtonXY;
import WorldSimulation.World;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class BoardPanel extends JPanel {
    private GameFrame parentFrame;
    private World world;
    private static int CELL_PX = 25;
    private static int MESSAGES_PX = 200;
    private JButton[][] board;
    public BoardPanel(World world, GameFrame gameFrame) {
        board = new JButton[world.getRows()][world.getCols()];
        parentFrame = gameFrame;
        this.world = world;
        setLayout(new GridLayout(world.getRows(), world.getCols()));
        createBoard();
        setBounds(0,0,world.getCols() * CELL_PX, world.getRows() * CELL_PX);
        repaint();
    }
    private void createBoard() {
        for (int y = 0; y < world.getRows(); y++) {
            for (int x = 0; x < world.getCols(); x++) {
                ButtonXY boardCell;
                if (world.getOrganism(y, x) != null) {
                    boardCell = new ButtonXY(y, x, world.getOrganism(y, x).getImage().getName());
                    boardCell.setBackground(world.getOrganism(y, x).getImage().getColor());
                }
                else {
                    boardCell = new ButtonXY(y, x, "");
                    boardCell.setBackground(Color.BLACK);
                }
                boardCell.setMargin(new Insets(5, 0, 0, 0));
                boardCell.setFocusable(false);
                boardCell.setBorderPainted(false);
                boardCell.setFont(new Font("Segoe UI Emoji", Font.PLAIN, CELL_PX / 2));
                boardCell.setBorder(null);
                boardCell.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        if (boardCell.getText() == "") {
                            chooseOrganism(boardCell.gY(), boardCell.gX());
                            updateBoard();
                            parentFrame.displayMessages(world.throwMessages());
                        }
                    }
                });
                add(boardCell);
                board[y][x] = boardCell;
            }
        }
    }
    private void chooseOrganism(int y, int x) {
        String[] options = { "Antelope", "Fox", "Sheep", "Turtle", "Wolf", "Dandelion", "Grass", "Guarana", "Nightshade", "SosnowskysHogweed" };
        JComboBox<String> comboBox = new JComboBox<>(options);
        int result = JOptionPane.showOptionDialog(parentFrame, comboBox, "Choose an option", JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null, null, null);
        if (result == JOptionPane.OK_OPTION) {
            String selectedOption = (String) comboBox.getSelectedItem();
            world.spawnEntity(selectedOption, y, x);
            updateBoard();
            parentFrame.displayMessages(world.throwMessages());
        }
    }
    public void updateBoard() {
        for (int y = 0; y < world.getRows(); y++) {
            for (int x = 0; x < world.getCols(); x++) {
                if (world.getOrganism(y, x) != null) {
                    getBoardField(y, x).setText(world.getOrganism(y, x).getImage().getName());
                    getBoardField(y, x).setBackground(world.getOrganism(y, x).getImage().getColor());
                } else {
                    getBoardField(y, x).setText("");
                    getBoardField(y, x).setBackground(Color.BLACK);
                    getBoardField(y, x).setBorder(null);
                }
                getBoardField(y, x).setFont(new Font("Segoe UI Emoji", Font.PLAIN, CELL_PX / 2));
            }
        }
        repaint();
    }
    public JButton getBoardField(int y, int x) {
        return board[y][x];
    }
    public void dispose() {
        for (int y = 0; y < board.length; y++) {
            for (int x = 0; x < board[y].length; x++) {
                JButton button = board[y][x];
                for (ActionListener listener : button.getActionListeners()) {
                    button.removeActionListener(listener);
                }
            }
        }
        removeAll();
        world = null;
        parentFrame = null;
        board = null;
    }
}
