package WorldSimulation.Organisms.Animals;

import WorldSimulation.Organisms.Organism;
import WorldSimulation.Organisms.Plants.SosnowskysHogweed;
import WorldSimulation.Tools.Img;

import java.awt.*;
import java.awt.event.KeyEvent;

public class Human extends Animal {
    public final static Img IMAGE = new Img("\uD83D\uDC68", new Color(47, 0, 255));
    public final static int INITIATIVE = 4;
    private int STRENGTH = 5;
    private static final int ULT_DURATION = 5;
    private static final int ULT_COOLDOWN = 5;
    private int ultRemaining = 0;
    private int ultCooldown = 0;
    private boolean moving = false;
    public Human(int row, int col) {
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
    public int getUltRemaining() {
        return ultRemaining;
    }
    public int getUltCooldown() {
        return ultCooldown;
    }
    public void setUltRemaining(int u) {
        ultRemaining = u;
    }
    public void setUltCooldown(int u) {
        ultCooldown = u;
    }
    @Override
    public void action() {
        move();
        moveHandling();
        ultStateHandling();
    }
    private void move() {
        oldY = y;
        oldX = x;
        int newRow = y;
        int newCol = x;
        if (world.getDirection() == KeyEvent.VK_UP) {
            newRow--;
        } else if (world.getDirection() == KeyEvent.VK_DOWN) {
            newRow++;
        } else if (world.getDirection() == KeyEvent.VK_LEFT) {
            newCol--;
        } else if (world.getDirection() == KeyEvent.VK_RIGHT) {
            newCol++;
        } else if (world.getDirection() == KeyEvent.VK_R && ultCooldown == 0) {
            ultRemaining = ULT_DURATION + 1;
            ultCooldown = ULT_COOLDOWN;
        }
        if (newRow == 0) {
            world.setDirection(KeyEvent.VK_UP);
        } else if (newRow == world.getRows() - 1) {
            world.setDirection(KeyEvent.VK_DOWN);
        } else if (newCol == 0) {
            world.setDirection(KeyEvent.VK_LEFT);
        } else if (newCol == world.getCols() - 1) {
            world.setDirection(KeyEvent.VK_RIGHT);
        } else {
            world.setDirection(0);
        }
        if (world.ON_BOARD(newRow, newCol,0,0)) {
            y = newRow;
            x = newCol;
        }
        checkForHogweed();
    }

    private void checkForHogweed() {  // to end the game instantly when human enters death ring of Sosnowsky's Hogweed (he's already dead)
        for (int i = -1; i < 2; i++) {
            for (int j = -1; j < 2; j++) {
                if (world.ON_BOARD(y, x, i, j) && world.getOrganism(y + i, x + j) != null && world.getOrganism(y + i, x + j).getClass().equals(SosnowskysHogweed.class)) {
                    world.endGame();
                }
            }
        }
    }
    @Override
    public boolean attackedReaction(Organism attacker) {
        if (ultRemaining > 0) {
            world.messages.add("Human shielded " + attacker.getClass().getSimpleName() + " attack");
            attacker.removeFromField();
            ((Animal) attacker).moveToEmpty();
            return true;
        }
        return false;
    }
    private void ultStateHandling() {
        if (ultRemaining == ULT_DURATION + 1) {
            world.messages.add("Human ulted!");
        }
        if (ultRemaining > 0) {
            ultRemaining--;
            if (ultRemaining > 0) {
                world.messages.add("Human ult remaining turns: " + ultRemaining);
            } else {
                world.messages.add("Human ult cooldown: " + ultCooldown);
            }
        }
        else if (ultCooldown > 0) {
            ultCooldown--;
            if (ultCooldown > 0) {
                world.messages.add("Human ult cooldown: " + ultCooldown);
            }
        }
        if (ultRemaining + ultCooldown == 0) {
            world.setUltable(true);
        }
        else {
            world.setUltable(false);
        }
    }
}