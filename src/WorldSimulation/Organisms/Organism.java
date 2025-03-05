package WorldSimulation.Organisms;
import WorldSimulation.World;

import java.lang.reflect.Constructor;


public abstract class Organism implements OrganismInterface {
    private int STRENGTH;
    protected int y = 0;
    protected int x = 0;
    protected int oldX = -1;
    protected int oldY = -1;
    protected boolean alive = true;
    protected int multiplyCooldown = 0;
    protected World world = null;
    public Organism(int row, int col) {
        this.y = row;
        this.x = col;
    }
    public void assignWorld(World w) {
        world = w;
    }
    public boolean sameKind(Organism org) {
        return (this.getImage().equals(org.getImage()));
    }
    public void kill() {
        alive = false;
    }
    public boolean attackedReaction(Organism attacker) {
        return false;
    }
    public void moveBack() {
        x = oldX;
        y = oldY;
    }
    public void updateOldCoordinates() {
        oldX = x;
        oldY = y;
    }
    public int getY() {
        return y;
    }
    public int getX() {
        return x;
    }
    public boolean ifAlive() {
        return alive;
    }
    public void addStrength(int boost) {
        STRENGTH += boost;
    }
    public void removeFromField() {
        if (oldX == -1 || oldY == -1) {
            oldX = x;
            oldY = y;
        }
        world.setOrganism(oldY, oldX, null);
    }
    protected int emptyAround() {
        int emptyCount = 0;
        for (int i = -1; i < 2; i++) {
            for (int j = -1; j < 2; j++) {
                if (world.ON_BOARD(y, x, i, j)) {
                    if (world.getOrganism(y + i, x + j) == null) {
                        emptyCount++;
                    }
                }
            }
        }
        return emptyCount;
    }
    protected boolean multiply() {
        int newX, newY;
        if (emptyAround() > 0) {
            do {
                newY = y - 1 + (int) (Math.random() * 3);
                newX = x - 1 + (int) (Math.random() * 3);
            } while (!(world.ON_BOARD(newY, newX, 0, 0)) || world.getOrganism(newY, newX) != null);

            try {
                Class<?> derivedClass = this.getClass();
                Constructor<?> constructor = derivedClass.getConstructor(int.class, int.class);
                Organism newOrganism = (Organism) constructor.newInstance(newY, newX);
                world.setOrganism(newY, newX, newOrganism);
                newOrganism.assignWorld(this.world);
                world.addOrganism(newOrganism);
                return true;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return false;
    }
    protected void decrementMultiplyCooldown() {
        if (multiplyCooldown > 0) {
            multiplyCooldown--;
        }
    }
}