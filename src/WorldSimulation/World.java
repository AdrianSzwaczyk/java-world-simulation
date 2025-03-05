package WorldSimulation;

import WorldSimulation.Organisms.Organism;
import WorldSimulation.Organisms.Animals.*;
import WorldSimulation.Organisms.Plants.*;

import java.io.*;
import java.lang.reflect.Constructor;
import java.util.*;

public class World {
    private static final int HUMAN_SPAWN_PROTECTION = 1;
    private static final int INITIAL_SPAWN_RATE = 300;
    private static final int MAX_INITIATIVE = 7;
    private final int rows;
    private final int cols;
    private Organism[][] map;
    private final List<Organism>[] organismList;
    public final List<String> messages;
    private final List<String> oldMessages;
    private int exit = 0;
    private int direction = 999999;
    private boolean r = true;
    public World(int rows, int cols) {
        this.rows = rows;
        this.cols = cols;
        messages = new ArrayList<>();
        oldMessages = new ArrayList<>();
        organismList = new ArrayList[MAX_INITIATIVE + 1];
        for (int i = 0; i <= MAX_INITIATIVE; i++) {
            organismList[i] = new ArrayList<>();
        }
        generateMap();
        spawnEntities();
    }
    public World(String fileName) {
        int rows = 0;
        int cols = 0;
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String line;
            if ((line = reader.readLine()) != null) {
                int r = Integer.parseInt(line);
                rows = r;
            }

            if ((line = reader.readLine()) != null) {
                int c = Integer.parseInt(line);
                cols = c;
            }
        } catch (IOException e) {}
        this.rows = rows;
        this.cols = cols;
        messages = new ArrayList<>();
        oldMessages = new ArrayList<>();
        organismList = new ArrayList[MAX_INITIATIVE + 1];
        for (int i = 0; i <= MAX_INITIATIVE; i++) {
            organismList[i] = new ArrayList<>();
        }
        generateMap();
        readFromFile(fileName);
    }
    private void generateMap() {
        map = new Organism[rows][cols];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                map[i][j] = null;
            }
        }
    }
    public boolean ON_BOARD(int y, int x, int i , int j) {
        return ((y) + (i) >= 0 && (y) + (i) < rows && (x) + (j) >= 0 && (x) + (j) < cols);
    }
    public Organism getOrganism(int y, int x) {
        return map[y][x];
    }
    public void setOrganism(int y, int x, Organism newOrganism) {
        map[y][x] = newOrganism;
    }
    public int getRows() {
        return rows;
    }
    public int getCols() {
        return cols;
    }
    public boolean ultable() {
        return r;
    }
    public void setUltable(boolean u) {
        r = u;
    }
    public int getDirection() {
        return direction;
    }
    public void setDirection(int d) {
        direction = d;
    }
    public String throwMessages() {
        String msg = "";
        oldMessages.clear();
        for (int i = 0; i < messages.size(); i++) {
            msg += messages.get(i) + '\n';
            oldMessages.add(messages.get(i));
        }
        messages.clear();
        return msg;
    }
    public void endGame() {
        exit = 1;
    }
    private boolean createOrganisms(Class<?> organismClass) {
        int row, col, counter;
        int spawnRate = rows * cols / INITIAL_SPAWN_RATE;
        int quantity = new Random().nextInt(spawnRate + 1) + 2;
        for (counter = 0; counter < quantity; counter++) {
            if (counter >= rows * cols) {
                return false;
            }
            row = rows / 2;
            col = cols / 2;
            while (map[row][col] != null || ((Math.abs(rows / 2 - row)) <= HUMAN_SPAWN_PROTECTION && (Math.abs(cols / 2 - col)) <= HUMAN_SPAWN_PROTECTION)) {
                row = new Random().nextInt(rows);
                col = new Random().nextInt(cols);
            }
            try {
                Constructor<?> constructor = organismClass.getConstructor(int.class, int.class);
                Object newOrganism = constructor.newInstance(row, col);
                map[row][col] = (Organism) newOrganism;
                map[row][col].assignWorld(this);
                addOrganism((Organism) newOrganism);
                messages.add("Created " + map[row][col].getClass().getSimpleName() + " at " + row + ", " + col);
            } catch (Exception e) {}
        }
        return true;
    }
    public void spawnEntity(String name, int y, int x) {
        Organism organism = getClassByName(name, y, x);
        organism.assignWorld(this);
        organismList[organism.getInitiative()].add(organism);
        map[y][x] = organism;
    }
    private void spawnEntities() {
        map[rows / 2][cols / 2] = new Human(rows / 2, cols / 2);
        map[rows / 2][cols / 2].assignWorld(this);
        organismList[Human.INITIATIVE].add(map[rows / 2][cols / 2]);

        for (int organismI = MAX_INITIATIVE; organismI >= 0; organismI--) {
            switch (organismI) {
                case Plant.INITIATIVE:
                    createOrganisms(Dandelion.class);
                    createOrganisms(Grass.class);
                    createOrganisms(Guarana.class);
                    createOrganisms(Nightshade.class);
                    createOrganisms(SosnowskysHogweed.class);
                    break;
                case (Antelope.INITIATIVE & Sheep.INITIATIVE):
                    createOrganisms(Antelope.class);
                    createOrganisms(Sheep.class);
                    break;
                case Fox.INITIATIVE:
                    createOrganisms(Fox.class);
                    break;
                case Turtle.INITIATIVE:
                    createOrganisms(Turtle.class);
                    break;
                case Wolf.INITIATIVE:
                    createOrganisms(Wolf.class);
                    break;
                default:
                    break;
            }
        }
    }
    public void addOrganism(Organism organism) {
        organismList[organism.getInitiative()].add(organism);
    }
    private boolean deleteIfNotAlive(Organism organism, int initiative) {
        if (!organism.ifAlive()) {
            if (organism.getClass().equals(Human.class)) {
                endGame();
            }
            organismList[initiative].remove(organism);
            return true;
        }
        return false;
    }
    public int turn() {
        for (int initiative = MAX_INITIATIVE; initiative >= 0; initiative--) {
            for (int nr = 0; nr < organismList[initiative].size(); nr++) {
                Organism organism = organismList[initiative].get(nr);
                if (!deleteIfNotAlive(organism, initiative)) {
                    organism.action();
                    if (deleteIfNotAlive(organism, initiative)) {
                        nr--;
                    }
                } else {
                    nr--;
                }
            }
        }
        return exit;
    }
    public void saveDataToFile(String fileName) throws IOException {
        String folderPath = "saves";
        String filePath = folderPath + "/" + fileName + ".txt";
        File folder = new File(folderPath);
        if (!folder.exists()) {
            folder.mkdir();
        }
        FileWriter writer = null;
        try {
            writer = new FileWriter(filePath);
            writer.write(Integer.toString(rows) + '\n');
            writer.write(Integer.toString(cols) + '\n');
            writer.write(Integer.toString(direction) + '\n');
            for (int initiative = MAX_INITIATIVE; initiative >= 0; initiative--) {
                for (int nr = 0; nr < organismList[initiative].size(); nr++) {
                    Organism organism = organismList[initiative].get(nr);
                    writer.write("$\n");
                    writer.write(organism.getClass().getSimpleName() + '\n');
                    writer.write(Integer.toString(organism.getY()) + '\n');
                    writer.write(Integer.toString(organism.getX()) + '\n');
                    writer.write(Integer.toString(organism.getStrength()) + '\n');
                    if (organism instanceof Human) {
                        writer.write(Integer.toString(((Human) organism).getUltRemaining()) + '\n');
                        writer.write(Integer.toString(((Human) organism).getUltCooldown()) + '\n');
                    }
                }
            }
            writer.write("&\n");
            for (int i = 0; i < oldMessages.size(); i++) {
                writer.write(oldMessages.get(i) + '\n');
            }
        } finally {
            if (writer != null) {
                writer.close();
            }
        }
    }
    public void readFromFile(String fileName) {
        String filePath = fileName;
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line = "";
            reader.readLine();
            reader.readLine();
            direction = Integer.parseInt(reader.readLine());
            while (!"&".equals(line)) {
                line = reader.readLine();
                if (!"&".equals(line)) {
                    String name = reader.readLine();
                    int y = Integer.parseInt(reader.readLine());
                    int x = Integer.parseInt(reader.readLine());
                    int s = Integer.parseInt(reader.readLine());
                    Organism organism = getClassByName(name, y, x);
                    organism.addStrength(s - organism.getStrength());
                    if (organism instanceof Human) {
                        ((Human) organism).setUltRemaining(Integer.parseInt(reader.readLine()));
                        ((Human) organism).setUltCooldown(Integer.parseInt(reader.readLine()));
                    }
                    organism.assignWorld(this);
                    organismList[organism.getInitiative()].add(organism);
                    map[y][x] = organism;
                }
            }
            while ((line = reader.readLine()) != null) {
                messages.add(line);
            }
        } catch (IOException e) {}
    }
    public static Organism getClassByName(String name, int y, int x) {
        switch (name) {
            case "Antelope":
                return new Antelope(y, x);
            case "Fox":
                return new Fox(y, x);
            case "Human":
                return new Human(y, x);
            case "Sheep":
                return new Sheep(y, x);
            case "Turtle":
                return new Turtle(y, x);
            case "Wolf":
                return new Wolf(y, x);
            case "Dandelion":
                return new Dandelion(y, x);
            case "Grass":
                return new Grass(y, x);
            case "Guarana":
                return new Guarana(y, x);
            case "Nightshade":
                return new Nightshade(y, x);
            case "SosnowskysHogweed":
                return new SosnowskysHogweed(y, x);
            default:
                return null;
        }
    }
}
