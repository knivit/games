package com.tsoft.game.games.loderunner;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.tsoft.game.games.loderunner.actor.LRPhysic;
import com.tsoft.game.games.loderunner.actor.Robot;
import com.tsoft.game.games.loderunner.actor.LRPlayer;
import com.tsoft.game.games.loderunner.actor.Robots;

import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.tsoft.game.games.loderunner.LRGameState.*;

public class LRWorld {

    private LRPhysic physic;

    private LRPlayer player;
    private Robots robots;

    public ArrayList<Point> robotStartPlaces = new ArrayList<>();
    public Point playerStartPlace;
    public Map<String, String> properties = new HashMap<>();

    public LRWorld() {
        screen = new LRScreen();
        physic = new LRPhysic();
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

        FileHandle resource = Gdx.files.internal("assets/loderunner/levels/" + n + ".txt");

        try (BufferedReader reader = resource.reader(1024)) {
            int y = LRScreen.HEIGHT - 1;
            String line;
            boolean isLevelStarted = false;

            while ((line = reader.readLine()) != null) {
                if (!isLevelStarted) {
                    // skip empty lines
                    if (line.trim().isEmpty()) {
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
                y --;
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private void addStartPlace(int x, int y) {
        Point place = new Point(x, y);
        robotStartPlaces.add(place);
    }

    public int getTreasureNumber() {
        int count = 0;
        for (int y = 0; y < (LRScreen.HEIGHT - 1); y ++) {
            for (int x = 0; x < LRScreen.WIDTH; x ++) {
                if (screen.getChar(x, y) == LRScreen.TREASURE_CHAR) {
                    count ++;
                }
            }
        }
        return count;
    }
}
