package WorldSimulation.Organisms.Animals;

import WorldSimulation.Organisms.Organism;


public abstract class Animal extends Organism {
    private final static int INIT_MULTIPLY_CD = 3;
    public Animal(int row, int col) {
        super(row, col);
        multiplyCooldown = INIT_MULTIPLY_CD - 1;
    }
    protected void fight() {
        if (!world.getOrganism(y, x).attackedReaction(this)) {
            if (world.getOrganism(y, x) instanceof Animal) {
                fightAnimal();
            } else {
                eatPlant();
            }
        }
    }
    protected void eatPlant() {
        if (getStrength() >= world.getOrganism(y, x).getStrength()) {
            killPlant();
        } else {
            getKilledByPlant();
        }
    }
    public void getKilledByPlant() {
        world.messages.add(world.getOrganism(y, x).getClass().getSimpleName() + " didn't taste well for " + getClass().getSimpleName());
        world.getOrganism(y, x).updateOldCoordinates();
        world.getOrganism(y, x).kill();
        world.getOrganism(y, x).removeFromField();
        alive = false;
        removeFromField();
    }

    protected void killPlant() {
        world.messages.add(getClass().getSimpleName() + " ate " + world.getOrganism(y, x).getClass().getSimpleName());
        world.getOrganism(y, x).kill();
        world.setOrganism(y, x, this);
        removeFromField();
    }

    protected void fightAnimal() {
        if (getStrength() >= world.getOrganism(y, x).getStrength()) {
            world.messages.add(getClass().getSimpleName() + " killed " + world.getOrganism(y, x).getClass().getSimpleName());
            world.getOrganism(y, x).kill();
            world.setOrganism(y, x, this);
            removeFromField();
        } else {
            world.messages.add(world.getOrganism(y, x).getClass().getSimpleName() + " killed " + getClass().getSimpleName());
            alive = false;
            removeFromField();
        }
    }

    @Override
    public void resetMultiplyCooldown() {
        multiplyCooldown = INIT_MULTIPLY_CD;
    }
    @Override
    public int getMultiplyCooldown() {
        return multiplyCooldown;
    }
    protected void breed() {
        if (multiplyCooldown == 0 && world.getOrganism(y, x).getMultiplyCooldown() == 0) {
            if (multiply()) {
                resetMultiplyCooldown();
                world.getOrganism(y, x).resetMultiplyCooldown();
                world.messages.add(getClass().getSimpleName() + " bred!");
            }
        }
    }
    protected void collision() {
        if (sameKind(world.getOrganism(y, x))) {
            breed();
            moveBack();
        } else {
            fight();
        }
    }
    protected void randomMove() {
        oldY = y;
        oldX = x;
        do {
            moveBack();
            y = y - 1 + (int) (Math.random() * 3);
            x = x - 1 + (int) (Math.random() * 3);
        } while (!(world.ON_BOARD(y, x, 0, 0)) || (x == oldX && y == oldY));
    }

    protected boolean moveToEmpty() {
        if (emptyAround() > 0) {
            oldY = y;
            oldX = x;
            do {
                moveBack();
                y = y - 1 + (int) (Math.random() * 3);
                x = x - 1 + (int) (Math.random() * 3);
            } while (!(world.ON_BOARD(y, x, 0, 0)) || (x == oldX && y == oldY) || world.getOrganism(y, x) != null);
            world.setOrganism(y, x, this);
            return true;
        }
        return false;
    }

    protected void moveHandling() {
        if (world.getOrganism(y, x) != this) {
            if (world.getOrganism(y,x) != null) {
                collision();
                if (alive) {
                }
            } else {
                world.setOrganism(y, x, this);
                removeFromField();
            }
        }
    }

    @Override
    public void action() {
        if (multiplyCooldown < INIT_MULTIPLY_CD && alive) {
            randomMove();
            moveHandling();
        }
        decrementMultiplyCooldown();
    }
}
