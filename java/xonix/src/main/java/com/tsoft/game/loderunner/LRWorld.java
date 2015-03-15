package com.tsoft.game.loderunner;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

public class LRWorld {
    private LRScreen screen;
    private LRPhysic physic;

    private LRPlayer player;
    private Robots robots;

    public ArrayList<Point> robotStartPlaces = new ArrayList<Point>();
    public Point playerStartPlace;
    public HashMap<String, String> properties = new HashMap<String, String>();

    public LRWorld(BufferedImage[] fontImages) {
        screen = new LRScreen(fontImages);
        physic = new LRPhysic(screen);
    }

    public LRScreen getScreen() {
        return screen;
    }

    public LRPhysic getPhysic() {
        return physic;
    }

    public LRPlayer getPlayer() {
        return player;
    }

    public Robots getRobots() {
        return robots;
    }

    public void setPlayer(LRPlayer player) {
        this.player = player;
    }

    public void setRobots(Robots robots) {
        this.robots = robots;
    }

    private void clearWorld() {
        screen.fill(LRScreen.EMPTY_CHAR);
        robotStartPlaces.clear();
        playerStartPlace = null;
        player = null;
        robots = null;
    }

    public void loadLevel(int n) {
        clearWorld();

        URL resource = getClass().getResource("levels/" + n + ".txt");

        String fileName = resource.getFile();
        try {
            BufferedReader reader = new BufferedReader(new FileReader(fileName));
            int y = 0;
            String line;
            boolean isLevelStarted = false;
            while ((line = reader.readLine()) != null) {
                if (!isLevelStarted) {
                    // skip empty lines
                    if (line.trim().length() == 0) {
                        continue;
                    }

                    // get the level's properties
                    n = line.indexOf('=');
                    if (n == -1) {
                        isLevelStarted = true;
                    } else {
                        String propertyName = line.substring(0, n).trim();
                        String propertyValue = line.substring(n + 1).trim();
                        properties.put(propertyName, propertyValue);
                        continue;
                    }
                }

                for (int x = 0; x < LRScreen.WIDTH && x < line.length(); x ++) {
                    screen.putChar(x, y, line.charAt(x));

                    switch (line.charAt(x)) {
                        case Robot.START_PLACE_CHAR: {
                            addStartPlace(x, y);
                            break;
                        }

                        case LRPlayer.START_PLACE_CHAR: {
                            playerStartPlace = new Point(x, y);
                            break;
                        }
                    }
                }
                y ++;
            }
        } catch (FileNotFoundException ex) {
            System.out.println("File not found: " + fileName);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private void addStartPlace(int x, int y) {
        Point place = new Point(x, y);
        robotStartPlaces.add(place);
    }

    public String getLogString() {
        StringBuilder buf = new StringBuilder("World {");
        buf.append(screen.getLogString()).append('\n');
        buf.append(", playerStartPlace= \n").append(playerStartPlace.toString()).append('\n');
        buf.append(", robotStartPlaces= {\n");
        for (Point point : robotStartPlaces) {
            buf.append("  ").append(point.toString()).append('\n');
        }
        buf.append(" }\n");
        buf.append('}');

        return buf.toString();
    }
}
