package WorldSimulation.Organisms.Plants;

import WorldSimulation.Organisms.Organism;

public abstract class Plant extends Organism {
    private final static int INIT_MULTIPLY_CD = 5;
    private final static int STRENGTH = 0;
    private final static double PLANT_SPREAD_PROBABILITY = 0.02;
    public Plant(int row, int col) {
        super(row, col);
        multiplyCooldown = INIT_MULTIPLY_CD - 1;
    }
    @Override
    public int getStrength() {
        return STRENGTH;
    }
    @Override
    public int getInitiative() {
        return STRENGTH;
    }
    @Override
    public void action() {
        spread();
        decrementMultiplyCooldown();
    }
    @Override
    public void resetMultiplyCooldown() {
        multiplyCooldown = INIT_MULTIPLY_CD;
    }
    @Override
    public int getMultiplyCooldown() {
        return multiplyCooldown;
    }
    protected void spread() {
        if (multiplyCooldown == 0 && (int) (Math.random() * (1 / PLANT_SPREAD_PROBABILITY)) == 1) {
            if (multiply()) {
                resetMultiplyCooldown();
                world.messages.add(this.getClass().getSimpleName() + " spread");
            }
        }
    }
}
