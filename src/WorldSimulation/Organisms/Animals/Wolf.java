package WorldSimulation.Organisms.Animals;

import WorldSimulation.Tools.Img;

import java.awt.*;

public class Wolf extends Animal {
    private final static Img IMAGE = new Img("\uD83D\uDC3A", new Color(112, 112, 112));
    public final static int INITIATIVE = 5;
    private int STRENGTH = 9;
    public Wolf(int row, int col) {
        super(row, col);
    }
    @Override
    public void addStrength(int boost) {
        STRENGTH += boost;
    }
    @Override
    public Img getImage() {
        return IMAGE;
    }
    @Override
    public int getStrength() {
        return STRENGTH;
    }
    @Override
    public int getInitiative() {
        return INITIATIVE;
    }
}
