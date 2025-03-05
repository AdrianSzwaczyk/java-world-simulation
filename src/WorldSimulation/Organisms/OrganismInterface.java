package WorldSimulation.Organisms;

import WorldSimulation.Tools.Img;
import WorldSimulation.World;

public interface OrganismInterface {
    int INITIATIVE = 0;
    Img getImage();
    int getStrength();
    int getInitiative();
    int getMultiplyCooldown();
    int getY();
    int getX();
    void action();
    void assignWorld(World w);
    void addStrength(int boost);
    void resetMultiplyCooldown();
    boolean sameKind(Organism org);
    void kill();
    boolean attackedReaction(Organism attacker);
    boolean ifAlive();
    void moveBack();
    void updateOldCoordinates();
    void removeFromField();
}
