package com.tsoft.game.games.loderunner.actor;

import com.tsoft.game.games.loderunner.LRScreen;
import com.tsoft.game.games.loderunner.mode.LRPlayStatus;

import java.awt.*;

import static com.tsoft.game.games.loderunner.LRGameSound.*;
import static com.tsoft.game.games.loderunner.LRGameState.*;
import static com.tsoft.game.games.loderunner.LRScreen.*;

public class LRPlayer {

    public int x;
    public int y;
    private char offChar;

    private final LRPlayStatus status;
    private boolean isNextLevel;

    public LRPlayer(LRPlayStatus status) {
        this.status = status;
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
        return (ch == LADDER_CHAR || ch == ROPE_CHAR || ch == TREASURE_CHAR);
    }

    private boolean canMoveDown() {
        char ch = screen.getChar(x, y);
        return (ch == EMPTY_CHAR || ch == LADDER_CHAR || ch == ROPE_CHAR  || ch == TREASURE_CHAR);
    }

    public Point getPlayerOffset() {
        Point off = new Point();

        if (controller.leftPressed)  {
            if (x > 0) off.x = -1;
        } else if (controller.rightPressed)  {
            if (x < (screen.getWidth() - 1)) off.x = 1;
        } else if (controller.upPressed)  {
            if (y < (screen.getHeight() - 1) && canMoveUp()) off.y = 1;
        } else if (controller.downPressed)  {
            if (y > 1 && canMoveDown()) off.y = -1;
        }

        return off;
    }

    public void move() {
        hide();

        Point off;
        boolean falling = false;
        if (world.getPhysic().isFalling(x, y)) {
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
        char newCh = screen.getChar(newX, newY);

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
                screen.putChar(newX, newY, LRScreen.EMPTY_CHAR);
                isNextLevel = (status.treasureLeft == 0);

                sound.push(TREASURE);
                break;
            }
        }

        if (canMove) {
            x = newX;
            y = newY;

            if (!falling) {
                sound.push(STEP);
            }
        }

        show();
    }

    public void removeLife() {
        status.removeLife();

        hide();

        reset();

        sound.push(REMOVE_LIFE);
    }

    public boolean isNextLevel() {
        return isNextLevel;
    }
}
