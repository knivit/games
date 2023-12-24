package com.tsoft.game.games.xonix.actor;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.tsoft.game.games.xonix.XGScreen;

import static com.tsoft.game.games.xonix.XGGameState.*;

public class XGPlayer {

    public static final char PLAYER_CHAR = 127;
    public static final char PLAYER_PATH_CHAR = '+';

    private int x;
    private int y;
    private boolean inSpace;
    private char offChar;

    private static final char FILLED_CHAR = '1';
    private static final char NOT_FILLED_CHAR = '0';

    private boolean isNextLevel;

    public XGPlayer() {
        reset();
    }

    private void reset() {
        x = screen.getWidth() / 2;
        y = screen.getHeight() - 1;
        offChar = XGScreen.BORDER_CHAR;
        inSpace = false;
        isNextLevel = false;
    }

    private void show() {
        offChar = screen.getChar(x, y);
        screen.putChar(x, y, PLAYER_CHAR);
    }

    private void hide() {
        screen.putChar(x, y, offChar);
    }

    static class PlayerOffset {
        public int dX, dY;

        public boolean isMoved() {
            return dX != 0 || dY != 0;
        }
    }

    PlayerOffset getPlayerOffset() {
        PlayerOffset off = new PlayerOffset();

        if (Gdx.input.isKeyPressed(Input.Keys.LEFT))  {
            if (x > 0) off.dX = -1;
        } else if (Gdx.input.isKeyPressed(Input.Keys.RIGHT))  {
            if (x < (screen.getWidth() - 1)) off.dX = 1;
        } else if (Gdx.input.isKeyPressed(Input.Keys.UP))  {
            if (y < (screen.getHeight() - 1)) off.dY = 1;
        } else if (Gdx.input.isKeyPressed(Input.Keys.DOWN))  {
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

        if (inSpace) {
            screen.putChar(x, y, PLAYER_PATH_CHAR);
        } else {
            screen.putChar(x, y, XGScreen.BORDER_CHAR);
        }

        char ch = screen.getChar(x + off.dX, y + off.dY);
        if (ch == PLAYER_PATH_CHAR || ch == Fly.INNER_FLY_CHAR || ch == Fly.OUTER_FLY_CHAR) {
            removeLife();
            return;
        }

        if (ch == XGScreen.EMPTY_CHAR || ch == XGScreen.BORDER_CHAR) {
            if (ch == XGScreen.EMPTY_CHAR) {
                inSpace = true;
            } else {
                if (inSpace) {
                    inSpace = false;
                    fillArea();

                    int emptyCount = screen.getCharCount(0, 1, screen.getWidth(), screen.getHeight(), XGScreen.EMPTY_CHAR);
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
        status.addScore(replace(PLAYER_PATH_CHAR, XGScreen.BORDER_CHAR));

        for (int y = 1; y < screen.getHeight(); y ++) {
            for (int x = 0; x < screen.getWidth(); x ++) {
                if (screen.getChar(x, y) == XGScreen.EMPTY_CHAR) {
                    if (isEmpty(x, y)) {
                        status.addScore(replace(FILLED_CHAR, XGScreen.BORDER_CHAR));
                    } else {
                        replace(FILLED_CHAR, NOT_FILLED_CHAR);
                    }
                }
            }
        }

        replace(NOT_FILLED_CHAR, XGScreen.EMPTY_CHAR);
    }

    private boolean isEmpty(int x, int y) {
        if (x < 0 || x >= screen.getWidth() || y < 1 || y >= screen.getHeight()) {
            return true;
        }

        boolean result = true;

        // look left
        int col = x;
        while (col > 0 && (screen.getChar(col, y) == XGScreen.EMPTY_CHAR || screen.getChar(col, y) == Fly.INNER_FLY_CHAR)) {
            if (screen.getChar(col, y) == XGScreen.EMPTY_CHAR) {
                screen.putChar(col, y, FILLED_CHAR);
            } else {
                result = false;
            }

            if (y > 1 && screen.getChar(col, y - 1) == XGScreen.EMPTY_CHAR && !isEmpty(col, y - 1)) {
                result = false;
            }
            if (y < (screen.getHeight() - 1) && screen.getChar(col, y + 1) == XGScreen.EMPTY_CHAR && !isEmpty(col, y + 1)) {
                result = false;
            }
            col --;
        }

        // look right
        col = x + 1;
        while (col < screen.getWidth() && (screen.getChar(col, y) == XGScreen.EMPTY_CHAR || screen.getChar(col, y) == Fly.INNER_FLY_CHAR)) {
            if (screen.getChar(col, y) == XGScreen.EMPTY_CHAR) {
                screen.putChar(col, y, FILLED_CHAR);
            } else {
                result = false;
            }
            if (y > 1 && screen.getChar(col, y - 1) == XGScreen.EMPTY_CHAR && !isEmpty(col, y - 1)) {
                result = false;
            }
            if (y < (screen.getHeight() - 1) && screen.getChar(col, y + 1) == XGScreen.EMPTY_CHAR && !isEmpty(col, y + 1)) {
                result = false;
            }
            col ++;
        }

        return result;
    }

    private int replace(char src, char dest) {
        return screen.replace(0, 1, screen.getWidth(), screen.getHeight(), src, dest);
    }

    public boolean isInSpace() {
        return inSpace;
    }

    public void removeLife() {
        status.removeLife();

        hide();
        replace(PLAYER_PATH_CHAR, XGScreen.EMPTY_CHAR);

        reset();
    }

    public boolean isNextLevel() {
        return isNextLevel;
    }

    public String getLogString() {
        return "XGPlayer {" +
                "x=" + x +
                ", y=" + y +
                ", inSpace=" + inSpace +
                ", offChar=" + offChar +
                ", isNextLevel=" + isNextLevel +
                '}';
    }
}
