package WorldSimulation.Organisms.Animals;

import WorldSimulation.Organisms.Organism;
import WorldSimulation.Tools.Img;

import java.awt.*;

public class Turtle extends Animal {
    private final static Img IMAGE = new Img("\uD83D\uDC22", new Color(16, 156, 0));
    public final static int INITIATIVE = 1;
    private int STRENGTH = 2;
    public Turtle(int row, int col) {
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
    @Override
    public void action() {
        if (Math.random() * 4 > 3) {
            super.action();
        }
    }
    @Override
    public boolean attackedReaction(Organism attacker) {
        if (attacker.getStrength() < 5) {
            world.messages.add("Turtle defended from " + attacker.getClass().getSimpleName());
            attacker.moveBack();
            return true;
        } else {
            return false;
        }
    }
}
