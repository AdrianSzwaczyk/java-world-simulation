package WorldSimulation.Organisms.Animals;

import WorldSimulation.Tools.Img;

import java.awt.*;

public class Sheep extends Animal {
    public final static int INITIATIVE = 4;
    private int STRENGTH = 4;
    public Sheep(int row, int col) {
        super(row, col);
    }
    private final static Img IMAGE = new Img("\uD83D\uDC11", Color.WHITE);
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
