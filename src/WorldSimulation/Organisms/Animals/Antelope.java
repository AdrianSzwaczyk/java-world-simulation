package WorldSimulation.Organisms.Animals;

import WorldSimulation.Organisms.Organism;
import WorldSimulation.Tools.Img;

import java.awt.*;

public class Antelope extends Animal {
    private final static Img IMAGE = new Img("\uD83E\uDD8C", new Color(245, 235, 144));
    public final static int INITIATIVE = 4;
    private int STRENGTH = 4;
    private static final int ANTELOPE_REACH = 1;
    private static final double ANTELOPE_ESCAPE_CHANCE = 0.5;
    public Antelope(int row, int col) {
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
    @Override
    protected void randomMove() {
        oldY = y;
        oldX = x;
        do {
            moveBack();
            y = y - 1 - ANTELOPE_REACH + (int) (Math.random() * (3 + 2 * ANTELOPE_REACH));
            x = x - 1 - ANTELOPE_REACH + (int) (Math.random() * (3 + 2 * ANTELOPE_REACH));
        } while (!(world.ON_BOARD(y, x, 0, 0)) || (x == oldX && y == oldY));
    }
    @Override
    public boolean attackedReaction(Organism attacker) {
        if ((int)(Math.random() * (1 / ANTELOPE_ESCAPE_CHANCE)) > 1) {
            oldY = y;
            oldX = x;
            removeFromField();
            world.setOrganism(y, x, attacker);
            attacker.removeFromField();
            if (!moveToEmpty()) {
                world.messages.add(getClass().getSimpleName() + " killed " + attacker.getClass().getSimpleName());
                alive = false;
            } else {
                world.messages.add(getClass().getSimpleName() + " ran away from " + attacker.getClass().getSimpleName());
            }
            return true;
        }
        return false;
    }
    @Override
    protected void fight() {
        if ((int)(Math.random() * (1 / ANTELOPE_ESCAPE_CHANCE)) > 1) {
            removeFromField();
            moveToEmpty();
        } else {
            super.fight();
        }
    }
}
