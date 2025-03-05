package WorldSimulation.Organisms.Plants;

import WorldSimulation.Tools.Img;

import java.awt.*;

public class Dandelion extends Plant {
    private final static Img IMAGE = new Img("\uD83C\uDF3C", new Color(234, 255, 0));
    private final static int DANDELION_SPREAD_MULTIPLY = 3;
    public Dandelion(int row, int col) {
        super(row, col);
    }
    @Override
    public Img getImage() {
        return IMAGE;
    }
    @Override
    public void action() {
        for (int i = 0; i < DANDELION_SPREAD_MULTIPLY; i++) {
            spread();
        }
        decrementMultiplyCooldown();
    }
}
