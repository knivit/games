package com.tsoft.game.games.loderunner.actor;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.tsoft.game.games.loderunner.misc.Screen;
import com.tsoft.game.utils.TextSprite;
import com.tsoft.game.utils.base.Point;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.tsoft.game.games.loderunner.LodeRunner.global;
import static com.tsoft.game.games.loderunner.misc.Screen.*;

public class World {

    private final Physics physics = new Physics();

    private Player player;

    public ArrayList<Point> robotStartPlaces = new ArrayList<>();
    public Point playerStartPlace;
    public Map<String, String> properties = new HashMap<>();

    public Physics getPhysics() {
        return physics;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    private void clearWorld() {
        global.screen.fill(Screen.EMPTY_CHAR);

        robotStartPlaces.clear();
        playerStartPlace = null;
        player = null;
    }

    public void loadLevel(int n) {
        clearWorld();

        FileHandle resource = Gdx.files.internal("assets/loderunner/levels/" + n + ".txt");

        try (BufferedReader reader = resource.reader(1024)) {
            int y = Screen.HEIGHT - 1;
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

                for (int x = 0; x < Screen.WIDTH && x < line.length(); x ++) {
                    char ch = line.charAt(x);
                    TextSprite sp = global.screen.sprite(x, y);
                    sp.ch = ch;
                    sp.color = Color.WHITE;

                    switch (ch) {
                        case ROBOT_START_CHAR: {
                            addStartPlace(x, y);
                            global.screen.putColor(x, y, Color.RED);
                            break;
                        }

                        case PLAYER_START_CHAR: {
                            playerStartPlace = new Point(x, y);
                            break;
                        }

                        case TREASURE_CHAR: {
                            global.screen.putColor(x, y, Color.YELLOW);
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
        for (int y = 1; y < Screen.HEIGHT; y ++) {
            for (int x = 0; x < Screen.WIDTH; x ++) {
                if (global.screen.getChar(x, y) == Screen.TREASURE_CHAR) {
                    count ++;
                }
            }
        }

        return count;
    }
}
