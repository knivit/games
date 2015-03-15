package com.tsoft.game.loderunner;

import com.tsoft.game.InputController;
import com.tsoft.game.keyboard.ActionKeyboard;

import java.awt.*;

public class LRPlayer {
    public static final char START_PLACE_CHAR = 'U';
    public static final char PLAYER_CHAR = 127;

    private LRWorld world;
    private LRScreen screen;
    private ActionKeyboard keyboard;
    private LRGameStatus status;

    public int x;
    public int y;
    private char offChar;

    private boolean isNextLevel;

    public LRPlayer(LRWorld world, InputController inputController, LRGameStatus status) {
        this.world = world;
        this.keyboard = inputController.getActionKeyboard();
        this.status = status;
        this.screen = world.getScreen();

        world.setPlayer(this);
        reset();
    }

    private void reset() {
        x = world.playerStartPlace.x;
        y = world.playerStartPlace.y;
        offChar = screen.getChar(x, y);
        isNextLevel = false;
    }

    private void show() {
        offChar = screen.getChar(x, y);
        screen.putChar(x, y, PLAYER_CHAR);
    }

    private void hide() {
        screen.putChar(x, y, offChar);
    }

    private boolean canMoveUp() {
        char ch = screen.getChar(x, y);
        return (ch == LRScreen.LADDER_CHAR || ch == LRScreen.TREASURE_CHAR ||
                ch == LRScreen.TREASURE_CHAR);
    }

    private boolean canMoveDown() {
        char ch = screen.getChar(x, y);
        return (ch == LRScreen.EMPTY_CHAR || ch == LRScreen.LADDER_CHAR ||
                ch == LRScreen.ROPE_CHAR  || ch == LRScreen.TREASURE_CHAR);
    }

    public Point getPlayerOffset() {
        Point off = new Point();
        ActionKeyboard.PressedKey key = keyboard.getFirstPressedKey();

        switch (key) {
            case LEFT: {
                if (x > 0) off.x = -1;
                break;
            }
            case RIGHT: {
                if (x < (screen.getWidth() - 1)) off.x = 1;
                break;
            }
            case UP: {
                if (y > 0 && canMoveUp()) off.y = -1;
                break;
            }
            case DOWN: {
                if (y < (screen.getHeight() - 2) && canMoveDown()) off.y = 1;
                break;
            }
        }
        return off;
    }

    public void move() {
        hide();

        Point off;
        if (world.getPhysic().isFalling(x, y)) {
            off = new Point(0, 1);
        } else {
            off = getPlayerOffset();
        }

        int newX = x + off.x;
        int newY = y + off.y;
        char newCh = screen.getChar(newX, newY);
        boolean canMove = true;
        switch (newCh) {
            case LRScreen.WALL_CHAR: {
                canMove = false;
                break;
            }
            case Robot.ROBOT_CHAR: {
                removeLife();
                canMove = false;
                break;
            }
            case LRScreen.TREASURE_CHAR: {
                status.addScore(10);
                screen.putChar(newX, newY, LRScreen.EMPTY_CHAR);

                isNextLevel = getTreasureNumber() == 0;
                break;
            }
        }

        if (canMove) {
            x = newX;
            y = newY;
        }

        show();
    }

    private int getTreasureNumber() {
        int count = 0;
        for (int y = 0; y < ((LRScreen.HEIGHT - 1)); y ++) {
            for (int x = 0; x < (LRScreen.WIDTH); x ++) {
                if (screen.getChar(x, y) == LRScreen.TREASURE_CHAR) {
                    count ++;
                }
            }
        }
        return count;
    }

    public void removeLife() {
        status.removeLife();

        hide();

        reset();
    }

    public boolean isNextLevel() {
        return isNextLevel;
    }

    public String getLogString() {
        return "LRPlayer {" +
                "x=" + x +
                ", y=" + y +
                ", offChar=" + offChar +
                '}';
    }
}
