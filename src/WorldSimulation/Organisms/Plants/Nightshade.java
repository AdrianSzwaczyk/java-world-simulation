package WorldSimulation.Organisms.Plants;

import WorldSimulation.Organisms.Animals.Animal;
import WorldSimulation.Organisms.Organism;
import WorldSimulation.Tools.Img;

import java.awt.*;

public class Nightshade extends Plant {
    private final static Img IMAGE = new Img("\uD83E\uDED0", new Color(109, 0, 156));
    private int STRENGTH = 99;
    public Nightshade(int row, int col) {
        super(row, col);
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
    public boolean attackedReaction(Organism attacker) {
        ((Animal) attacker).getKilledByPlant();
        return true;
    }
}
