package com.tsoft.game.games.loderunner.actor;

import com.tsoft.game.games.loderunner.misc.Screen;
import com.tsoft.game.games.loderunner.mode.PlayStatus;

import java.awt.*;

import static com.tsoft.game.games.loderunner.misc.Sound.*;
import static com.tsoft.game.games.loderunner.misc.Screen.*;
import static com.tsoft.game.games.loderunner.LodeRunner.state;

public class Player {

    public int x;
    public int y;
    private char offChar;

    private final PlayStatus status;
    private boolean isNextLevel;

    public Player(PlayStatus status) {
        this.status = status;
        state.world.setPlayer(this);

        reset();
    }

    private void reset() {
        x = state.world.playerStartPlace.x;
        y = state.world.playerStartPlace.y;
        offChar = state.screen.getChar(x, y);
        isNextLevel = false;
    }

    private void show() {
        offChar = state.screen.getChar(x, y);
        state.screen.putChar(x, y, PLAYER_CHAR);
    }

    private void hide() {
        state.screen.putChar(x, y, offChar);
    }

    private boolean canMoveUp() {
        char ch = state.screen.getChar(x, y);
        return (ch == LADDER_CHAR || ch == ROPE_CHAR || ch == TREASURE_CHAR);
    }

    private boolean canMoveDown() {
        char ch = state.screen.getChar(x, y);
        return (ch == EMPTY_CHAR || ch == LADDER_CHAR || ch == ROPE_CHAR  || ch == TREASURE_CHAR);
    }

    public Point getPlayerOffset() {
        Point off = new Point();

        if (state.controller.leftPressed)  {
            if (x > 0) off.x = -1;
        } else if (state.controller.rightPressed)  {
            if (x < (state.screen.getWidth() - 1)) off.x = 1;
        } else if (state.controller.upPressed)  {
            if (y < (state.screen.getHeight() - 1) && canMoveUp()) off.y = 1;
        } else if (state.controller.downPressed)  {
            if (y > 1 && canMoveDown()) off.y = -1;
        }

        return off;
    }

    public void move() {
        hide();

        Point off;
        boolean falling = false;
        if (state.world.getPhysics().isFalling(x, y)) {
            off = new Point(0, -1);
            falling = true;
        } else {
            off = getPlayerOffset();
        }

        if (off.x == 0 && off.y == 0) {
            show();
            return;
        }

        int newX = x + off.x;
        int newY = y + off.y;
        char newCh = state.screen.getChar(newX, newY);

        boolean canMove = true;
        switch (newCh) {
            case WALL_CHAR: {
                canMove = false;
                break;
            }
            case ROBOT_CHAR: {
                removeLife();
                canMove = false;
                break;
            }
            case TREASURE_CHAR: {
                status.treasureFound();
                state.screen.putChar(newX, newY, Screen.EMPTY_CHAR);
                isNextLevel = (status.treasureLeft == 0);

                state.sound.push(TREASURE_SOUND);
                break;
            }
        }

        if (canMove) {
            x = newX;
            y = newY;

            if (!falling) {
                state.sound.push(STEP_SOUND);
            }
        }

        show();
    }

    public void removeLife() {
        status.removeLife();

        hide();

        reset();

        state.sound.push(REMOVE_LIFE_SOUND);
    }

    public boolean isNextLevel() {
        return isNextLevel;
    }
}
