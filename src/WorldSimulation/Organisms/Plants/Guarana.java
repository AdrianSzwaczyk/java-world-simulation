package WorldSimulation.Organisms.Plants;

import WorldSimulation.Organisms.Organism;
import WorldSimulation.Tools.Img;

import java.awt.*;

public class Guarana extends Plant {
    private final static Img IMAGE = new Img("\uD83E\uDEB7", new Color(255, 3, 3));
    private final static int GUARANA_STRENGTH_BOOST = 3;
    public Guarana(int row, int col) {
        super(row, col);
    }
    @Override
    public Img getImage() {
        return IMAGE;
    }
    @Override
    public boolean attackedReaction(Organism attacker) {
        attacker.addStrength(GUARANA_STRENGTH_BOOST);
        return false;
    }
}
