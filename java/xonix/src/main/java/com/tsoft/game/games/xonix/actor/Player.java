package com.tsoft.game.games.xonix.actor;

import com.tsoft.game.games.xonix.misc.Screen;
import com.tsoft.game.games.xonix.scene.PlayStatus;
import com.tsoft.game.utils.GameController;

import static com.tsoft.game.games.xonix.misc.Sound.REMOVE_LIFE_SOUND;
import static com.tsoft.game.games.xonix.misc.Sound.STEP_SOUND;
import static com.tsoft.game.games.xonix.Xonix.global;
import static com.tsoft.game.games.xonix.misc.Screen.*;

public class Player {

    private static final char FILLED_CHAR = '1';
    private static final char NOT_FILLED_CHAR = '0';

    private int x;
    private int y;
    private boolean inSpace;
    private char offChar;

    private final PlayStatus status;
    private boolean isNextLevel;

    public Player(PlayStatus status) {
        this.status = status;

        reset();
    }

    public void move(GameController.State controller) {
        hide();

        if (controller.dx == 0 && controller.dy == 0) {
            show();
            return;
        }

        hide();

        if (inSpace) {
            global.screen.putChar(x, y, PLAYER_PATH_CHAR);
        } else {
            global.screen.putChar(x, y, Screen.BORDER_CHAR);
        }

        int nx = Math.min(Math.max(x + controller.dx, 0), global.screen.getWidth() - 1);
        int ny = Math.min(Math.max(y + controller.dy, 1), global.screen.getHeight() - 1);

        char ch = global.screen.getChar(nx, ny);
        if (ch == PLAYER_PATH_CHAR || ch == INNER_FLY_CHAR || ch == OUTER_FLY_CHAR) {
            removeLife();
            return;
        }

        if (ch == Screen.EMPTY_CHAR || ch == Screen.BORDER_CHAR) {
            if (ch == Screen.EMPTY_CHAR) {
                inSpace = true;
                global.sound.push(STEP_SOUND);
            } else {
                if (inSpace) {
                    inSpace = false;
                    fillArea();

                    int emptyCount = global.screen.getCharCount(0, 1, global.screen.getWidth(), global.screen.getHeight(), Screen.EMPTY_CHAR);
                    if (emptyCount < 256) {
                        isNextLevel = true;
                    }
                }
            }

            x = nx;
            y = ny;
        }

        show();
    }

    private void reset() {
        x = global.screen.getWidth() / 2;
        y = global.screen.getHeight() - 1;
        offChar = Screen.BORDER_CHAR;
        inSpace = false;
        isNextLevel = false;
    }

    private void show() {
        offChar = global.screen.getChar(x, y);
        global.screen.putChar(x, y, PLAYER_CHAR);
    }

    private void hide() {
        global.screen.putChar(x, y, offChar);
    }

    private void fillArea() {
        status.addScore(replace(PLAYER_PATH_CHAR, Screen.BORDER_CHAR));

        for (int y = 1; y < global.screen.getHeight(); y ++) {
            for (int x = 0; x < global.screen.getWidth(); x ++) {
                if (global.screen.getChar(x, y) == Screen.EMPTY_CHAR) {
                    if (isEmpty(x, y)) {
                        status.addScore(replace(FILLED_CHAR, Screen.BORDER_CHAR));
                    } else {
                        replace(FILLED_CHAR, NOT_FILLED_CHAR);
                    }
                }
            }
        }

        replace(NOT_FILLED_CHAR, Screen.EMPTY_CHAR);
    }

    private boolean isEmpty(int x, int y) {
        if (x < 0 || x >= global.screen.getWidth() || y < 1 || y >= global.screen.getHeight()) {
            return true;
        }

        boolean result = true;

        // look left
        int col = x;
        while (col > 0) {
            char ch = global.screen.getChar(col, y);
            if (ch != Screen.EMPTY_CHAR && ch != INNER_FLY_CHAR) {
                break;
            }

            if (ch == Screen.EMPTY_CHAR) {
                global.screen.putChar(col, y, FILLED_CHAR);
            } else {
                result = false;
            }

            if (y > 1 && global.screen.getChar(col, y - 1) == Screen.EMPTY_CHAR && !isEmpty(col, y - 1)) {
                result = false;
            }

            if (y < (global.screen.getHeight() - 1) && global.screen.getChar(col, y + 1) == Screen.EMPTY_CHAR && !isEmpty(col, y + 1)) {
                result = false;
            }

            col --;
        }

        // look right
        col = x + 1;
        while (col < global.screen.getWidth()) {
            char ch = global.screen.getChar(col, y);
            if (ch != Screen.EMPTY_CHAR && ch != INNER_FLY_CHAR) {
                break;
            }

            if (ch == Screen.EMPTY_CHAR) {
                global.screen.putChar(col, y, FILLED_CHAR);
            } else {
                result = false;
            }

            if (y > 1 && global.screen.getChar(col, y - 1) == Screen.EMPTY_CHAR && !isEmpty(col, y - 1)) {
                result = false;
            }

            if (y < (global.screen.getHeight() - 1) && global.screen.getChar(col, y + 1) == Screen.EMPTY_CHAR && !isEmpty(col, y + 1)) {
                result = false;
            }

            col ++;
        }

        return result;
    }

    private int replace(char src, char dest) {
        return global.screen.replace(0, 1, global.screen.getWidth(), global.screen.getHeight(), src, dest);
    }

    public boolean isInSpace() {
        return inSpace;
    }

    public void removeLife() {
        status.removeLife();

        hide();
        replace(PLAYER_PATH_CHAR, Screen.EMPTY_CHAR);

        reset();

        global.sound.push(REMOVE_LIFE_SOUND);
    }

    public boolean isNextLevel() {
        return isNextLevel;
    }
}
