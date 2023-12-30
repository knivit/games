package com.tsoft.game.games.xonix.actor;

import static com.tsoft.game.games.xonix.Xonix.global;
import static com.tsoft.game.games.xonix.misc.Screen.*;

public class Fly {

    private int x;
    private int y;
    private FlyDir dir;

    private final char onChar;
    private final char offChar;

    public Fly(int x, int y, FlyDir dir, char onChar, char offChar) {
        this.x = x;
        this.y = y;
        this.dir = dir;
        this.onChar = onChar;
        this.offChar = offChar;
    }

    public static Fly getRandom(char onChar, char offChar) {
        int x, y;
        do {
            x = (int)(Math.random() * global.screen.getWidth());
            y = (int)(Math.random() * (global.screen.getHeight() - 1)) + 1;
        } while (global.screen.getChar(x, y) != offChar);
        global.screen.putChar(x, y, onChar);

        return new Fly(x, y, FlyDir.getRandom(), onChar, offChar);
    }

    public void move() {
        global.screen.putChar(x, y, offChar);

        FlyDir newDir = dir;
        for (int i = 0; i < FlyDir.values().length; i ++) {
            int newX = x + newDir.getdX();
            int newY = y + newDir.getdY();
            char newChar;

            if (newX > -1 && newX < global.screen.getWidth() && newY > 0 && newY < global.screen.getHeight()) {
                newChar = global.screen.getChar(newX, newY);
            } else {
                newDir = changeDir(newX, newY, newDir);
                continue;
            }

            boolean hitByInnerFly = (onChar == INNER_FLY_CHAR &&
                    ((newChar == PLAYER_CHAR || newChar == PLAYER_PATH_CHAR)) &&
                    global.player.isInSpace());

            boolean hitByOuterFly = (onChar == OUTER_FLY_CHAR && (newChar == PLAYER_CHAR) &&
                    (!global.player.isInSpace()));

            if (hitByInnerFly || hitByOuterFly) {
                global.player.removeLife();
            }

            boolean canMoveFly = true;
            if (onChar == INNER_FLY_CHAR && newChar != EMPTY_CHAR) {
                canMoveFly = false;
            }

            if (onChar == OUTER_FLY_CHAR && newChar != BORDER_CHAR && newChar != OUTER_FLY_CHAR) {
                canMoveFly = false;
            }

            if (canMoveFly) {
                x = newX;
                y = newY;
                dir = newDir;
                break;
            } else {
                newDir = changeDir(newX, newY, newDir);
            }
        }

        global.screen.putChar(x, y, onChar);
    }

    private FlyDir changeDir(int x, int y, FlyDir dir) {
        switch (dir) {
            case LEFT_UP: {
                char ch1 = getChar(x, y + 1);
                char ch2 = getChar(x + 1, y);
                if (ch1 == offChar) {
                    return FlyDir.LEFT_DOWN;
                }
                if (ch2 == offChar) {
                    return FlyDir.RIGHT_UP;
                }
                return FlyDir.RIGHT_DOWN;
            }

            case RIGHT_UP: {
                char ch1 = getChar(x - 1, y);
                char ch2 = getChar(x, y + 1);
                if (ch1 == offChar) {
                    return FlyDir.LEFT_UP;
                }
                if (ch2 == offChar) {
                    return FlyDir.RIGHT_DOWN;
                }
                return FlyDir.LEFT_DOWN;
            }

            case LEFT_DOWN: {
                char ch1 = getChar(x + 1, y);
                char ch2 = getChar(x, y - 1);
                if (ch1 == offChar) {
                    return FlyDir.RIGHT_DOWN;
                }
                if (ch2 == offChar) {
                    return FlyDir.LEFT_UP;
                }
                return FlyDir.RIGHT_UP;
            }

            case RIGHT_DOWN: {
                char ch1 = getChar(x, y - 1);
                char ch2 = getChar(x - 1, y);
                if (ch1 == offChar) {
                    return FlyDir.RIGHT_UP;
                }
                if (ch2 == offChar) {
                    return FlyDir.LEFT_DOWN;
                }
                return FlyDir.LEFT_UP;
            }
        }

        return dir;
    }

    private char getChar(int x, int y) {
        if (x >= 0 && x < global.screen.getWidth() && y > 0 && y < global.screen.getHeight()) {
            return global.screen.getChar(x, y);
        }
        return EMPTY_CHAR;
    }
}
