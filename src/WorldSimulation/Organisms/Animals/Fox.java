package WorldSimulation.Organisms.Animals;

import WorldSimulation.Tools.Img;

import java.awt.*;

public class Fox extends Animal {
    public final static Img IMAGE = new Img("🦊", Color.ORANGE);
    public final static int INITIATIVE = 7;
    private int STRENGTH = 3;
    public Fox(int row, int col) {
        super(row, col);
    }
    @Override
    public void addStrength(int boost) {
        STRENGTH += boost;
    }
    @Override
    public int getStrength() {
        return STRENGTH;
    }
    @Override
    public int getInitiative() {
        return INITIATIVE;
    }
    @Override
    public Img getImage() {
        return IMAGE;
    }
    private int weakerAround() {
        int weakerCount = 0;
        for (int i = -1; i < 2; i++) {
            for (int j = -1; j < 2; j++) {
                if (world.ON_BOARD(y, x, i, j)) {
                    if (world.getOrganism(y + i, x + j) != null && world.getOrganism(y + i, x + j).getStrength() < getStrength()) {
                        weakerCount++;
                    }
                }
            }
        }
        return weakerCount;
    }
    @Override
    protected void randomMove() {
        if (emptyAround() + weakerAround() > 0) {
            oldY = y;
            oldX = x;
            do {
                y = oldY;
                x = oldX;
                y = y - 1 + (int) (Math.random() * 3);
                x = x - 1 + (int) (Math.random() * 3);
            } while (!(world.ON_BOARD(y, x, 0, 0)) || (x == oldX && y == oldY) || (world.getOrganism(y, x) != null && world.getOrganism(y, x).getStrength() > getStrength()));
        }
    }
}
