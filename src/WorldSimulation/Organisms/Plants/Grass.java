package WorldSimulation.Organisms.Plants;

import WorldSimulation.Tools.Img;

import java.awt.*;

public class Grass extends Plant {
    private final static Img IMAGE = new Img("\uD83C\uDF3E", Color.GREEN);
    public Grass(int row, int col) {
        super(row, col);
    }
    @Override
    public Img getImage() {
        return IMAGE;
    }
}
