package WorldSimulation.Organisms.Plants;

import WorldSimulation.Organisms.Animals.Animal;
import WorldSimulation.Organisms.Organism;
import WorldSimulation.Tools.Img;

import java.awt.*;

public class SosnowskysHogweed extends Plant {
    private final static Img IMAGE = new Img("☢", new Color(87, 255, 146));
    private int STRENGTH = 10;
    public SosnowskysHogweed(int row, int col) {
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
    private void killAura() {
        Organism ifAnimal;
        for (int i = -1; i < 2; i++) {
            for (int j = -1; j < 2; j++) {
                if (world.ON_BOARD(y, x, i, j) && (i != 0 || j != 0)) {
                    ifAnimal = world.getOrganism(y + i, x + j);
                    if (world.getOrganism(y + i, x + j) != null && !world.getOrganism(y + i, x + j).getClass().equals(SosnowskysHogweed.class) && ifAnimal instanceof Animal) {
                        world.messages.add(getClass().getSimpleName() + " obliterated " + world.getOrganism(y + i, x + j).getClass().getSimpleName());
                        world.getOrganism(y + i, x + j).kill();
                        world.getOrganism(y + i, x + j).updateOldCoordinates();
                        world.getOrganism(y + i, x + j).removeFromField();
                    }
                }
            }
        }
    }
    @Override
    public boolean attackedReaction(Organism attacker) {
        ((Animal) attacker).getKilledByPlant();
        return true;
    }
    @Override
    public void action() {
        killAura();
        super.action();
    }
}
