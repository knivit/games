package com.tsoft.game.games.loderunner.actor;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.tsoft.game.games.loderunner.LRGameState;
import com.tsoft.game.games.loderunner.LRScreen;

import java.awt.*;

public class LRPlayer {

    public static final char START_PLACE_CHAR = 'U';
    public static final char PLAYER_CHAR = 127;

    public int x;
    public int y;
    private char offChar;

    private boolean isNextLevel;

    public LRPlayer() {
        LRGameState.world.setPlayer(this);

        reset();
    }

    private void reset() {
        x = LRGameState.world.playerStartPlace.x;
        y = LRGameState.world.playerStartPlace.y;
        offChar = LRGameState.screen.getChar(x, y);
        isNextLevel = false;
    }

    private void show() {
        offChar = LRGameState.screen.getChar(x, y);
        LRGameState.screen.putChar(x, y, PLAYER_CHAR);
    }

    private void hide() {
        LRGameState.screen.putChar(x, y, offChar);
    }

    private boolean canMoveUp() {
        char ch = LRGameState.screen.getChar(x, y);
        return (ch == LRScreen.LADDER_CHAR || ch == LRScreen.TREASURE_CHAR ||
                ch == LRScreen.TREASURE_CHAR);
    }

    private boolean canMoveDown() {
        char ch = LRGameState.screen.getChar(x, y);
        return (ch == LRScreen.EMPTY_CHAR || ch == LRScreen.LADDER_CHAR ||
                ch == LRScreen.ROPE_CHAR  || ch == LRScreen.TREASURE_CHAR);
    }

    public Point getPlayerOffset() {
        Point off = new Point();

        if (Gdx.input.isKeyPressed(Input.Keys.LEFT))  {
            if (x > 0) off.x = -1;
        } else if (Gdx.input.isKeyPressed(Input.Keys.RIGHT))  {
            if (x < (LRGameState.screen.getWidth() - 1)) off.x = 1;
        } else if (Gdx.input.isKeyPressed(Input.Keys.UP))  {
            if (y < (LRGameState.screen.getHeight() - 2) && canMoveDown()) off.y = 1;
        } else if (Gdx.input.isKeyPressed(Input.Keys.DOWN))  {
            if (y > 0 && canMoveUp()) off.y = -1;
        }

        return off;
    }

    public void move() {
        hide();

        Point off;
        if (LRGameState.world.getPhysic().isFalling(x, y)) {
            off = new Point(0, 1);
        } else {
            off = getPlayerOffset();
        }

        int newX = x + off.x;
        int newY = y + off.y;
        char newCh = LRGameState.screen.getChar(newX, newY);
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
                LRGameState.status.addScore(10);
                LRGameState.screen.putChar(newX, newY, LRScreen.EMPTY_CHAR);

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
                if (LRGameState.screen.getChar(x, y) == LRScreen.TREASURE_CHAR) {
                    count ++;
                }
            }
        }
        return count;
    }

    public void removeLife() {
        LRGameState.status.removeLife();

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
