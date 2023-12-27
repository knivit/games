package com.tsoft.game.games.xonix.actor;

import com.tsoft.game.games.xonix.misc.Screen;
import com.tsoft.game.games.xonix.scene.PlayStatus;

import static com.tsoft.game.games.xonix.misc.Sound.REMOVE_LIFE;
import static com.tsoft.game.games.xonix.misc.Sound.STEP;
import static com.tsoft.game.games.xonix.Xonix.state;
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

    private void reset() {
        x = state.screen.getWidth() / 2;
        y = state.screen.getHeight() - 1;
        offChar = Screen.BORDER_CHAR;
        inSpace = false;
        isNextLevel = false;
    }

    private void show() {
        offChar = state.screen.getChar(x, y);
        state.screen.putChar(x, y, PLAYER_CHAR);
    }

    private void hide() {
        state.screen.putChar(x, y, offChar);
    }

    static class PlayerOffset {
        public int dX, dY;

        public boolean isMoved() {
            return dX != 0 || dY != 0;
        }
    }

    PlayerOffset getPlayerOffset() {
        PlayerOffset off = new PlayerOffset();

        if (state.controller.leftPressed)  {
            if (x > 0) off.dX = -1;
        } else if (state.controller.rightPressed)  {
            if (x < (state.screen.getWidth() - 1)) off.dX = 1;
        } else if (state.controller.upPressed)  {
            if (y < (state.screen.getHeight() - 1)) off.dY = 1;
        } else if (state.controller.downPressed)  {
            if (y > 1) off.dY = -1;
        }
        return off;
    }

    public void move() {
        hide();

        PlayerOffset off = getPlayerOffset();
        if (!off.isMoved()) {
            show();
            return;
        }

        hide();

        if (inSpace) {
            state.screen.putChar(x, y, PLAYER_PATH_CHAR);
        } else {
            state.screen.putChar(x, y, Screen.BORDER_CHAR);
        }

        char ch = state.screen.getChar(x + off.dX, y + off.dY);
        if (ch == PLAYER_PATH_CHAR || ch == INNER_FLY_CHAR || ch == OUTER_FLY_CHAR) {
            removeLife();
            return;
        }

        if (ch == Screen.EMPTY_CHAR || ch == Screen.BORDER_CHAR) {
            if (ch == Screen.EMPTY_CHAR) {
                inSpace = true;
                state.sound.push(STEP);
            } else {
                if (inSpace) {
                    inSpace = false;
                    fillArea();

                    int emptyCount = state.screen.getCharCount(0, 1, state.screen.getWidth(), state.screen.getHeight(), Screen.EMPTY_CHAR);
                    if (emptyCount < 256) {
                        isNextLevel = true;
                    }
                }
            }

            x += off.dX;
            y += off.dY;
        }

        show();
    }

    private void fillArea() {
        status.addScore(replace(PLAYER_PATH_CHAR, Screen.BORDER_CHAR));

        for (int y = 1; y < state.screen.getHeight(); y ++) {
            for (int x = 0; x < state.screen.getWidth(); x ++) {
                if (state.screen.getChar(x, y) == Screen.EMPTY_CHAR) {
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
        if (x < 0 || x >= state.screen.getWidth() || y < 1 || y >= state.screen.getHeight()) {
            return true;
        }

        boolean result = true;

        // look left
        int col = x;
        while (col > 0) {
            char ch = state.screen.getChar(col, y);
            if (ch != Screen.EMPTY_CHAR && ch != INNER_FLY_CHAR) {
                break;
            }

            if (ch == Screen.EMPTY_CHAR) {
                state.screen.putChar(col, y, FILLED_CHAR);
            } else {
                result = false;
            }

            if (y > 1 && state.screen.getChar(col, y - 1) == Screen.EMPTY_CHAR && !isEmpty(col, y - 1)) {
                result = false;
            }

            if (y < (state.screen.getHeight() - 1) && state.screen.getChar(col, y + 1) == Screen.EMPTY_CHAR && !isEmpty(col, y + 1)) {
                result = false;
            }

            col --;
        }

        // look right
        col = x + 1;
        while (col < state.screen.getWidth()) {
            char ch = state.screen.getChar(col, y);
            if (ch != Screen.EMPTY_CHAR && ch != INNER_FLY_CHAR) {
                break;
            }

            if (ch == Screen.EMPTY_CHAR) {
                state.screen.putChar(col, y, FILLED_CHAR);
            } else {
                result = false;
            }

            if (y > 1 && state.screen.getChar(col, y - 1) == Screen.EMPTY_CHAR && !isEmpty(col, y - 1)) {
                result = false;
            }

            if (y < (state.screen.getHeight() - 1) && state.screen.getChar(col, y + 1) == Screen.EMPTY_CHAR && !isEmpty(col, y + 1)) {
                result = false;
            }

            col ++;
        }

        return result;
    }

    private int replace(char src, char dest) {
        return state.screen.replace(0, 1, state.screen.getWidth(), state.screen.getHeight(), src, dest);
    }

    public boolean isInSpace() {
        return inSpace;
    }

    public void removeLife() {
        status.removeLife();

        hide();
        replace(PLAYER_PATH_CHAR, Screen.EMPTY_CHAR);

        reset();

        state.sound.push(REMOVE_LIFE);
    }

    public boolean isNextLevel() {
        return isNextLevel;
    }
}
