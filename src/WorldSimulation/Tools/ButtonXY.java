package WorldSimulation.Tools;

import javax.swing.*;

public class ButtonXY extends JButton {
    private final int y, x;
    public ButtonXY(int y, int x, String content) {
        super(content);
        this.y = y;
        this.x = x;
    }
    public int gX() {
        return x;
    }
    public int gY() {
        return y;
    }
}