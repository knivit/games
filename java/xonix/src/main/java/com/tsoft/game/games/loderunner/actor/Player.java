package com.tsoft.game.games.loderunner.actor;

import com.tsoft.game.games.loderunner.misc.Screen;
import com.tsoft.game.games.loderunner.mode.PlayStatus;
import com.tsoft.game.utils.GameController;

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

    public void move(GameController.State controller) {
        hide();

        int dx = 0;
        int dy = 0;
        boolean falling = false;
        if (state.world.getPhysics().isFalling(x, y)) {
            dy = -1;
            falling = true;
        } else {
            if (controller.dx == -1 && canMoveLeft()) {
                dx = -1;
            } else if (controller.dx == 1 && canMoveRight()) {
                dx = 1;
            } if (controller.dy == 1 && canMoveUp()) {
                dy = 1;
            } else if (controller.dy == -1 && canMoveDown()) {
                dy = -1;
            }
        }

        if (dx == 0 && dy == 0) {
            show();
            return;
        }

        int nx = x + dx;
        int ny = y + dy;

        char nch = state.screen.getChar(nx, ny);

        boolean canMove = true;
        switch (nch) {
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
                state.screen.putChar(nx, ny, Screen.EMPTY_CHAR);
                isNextLevel = (status.treasureLeft == 0);

                state.sound.push(TREASURE_SOUND);
                break;
            }
        }

        if (canMove) {
            x = nx;
            y = ny;

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

    private boolean canMoveLeft() {
        return x > 0;
    }

    private boolean canMoveRight() {
        return x < state.screen.getWidth();
    }

    private boolean canMoveUp() {
        char ch = state.screen.getChar(x, y);
        return (y < state.screen.getHeight()) && (ch == LADDER_CHAR || ch == ROPE_CHAR || ch == TREASURE_CHAR);
    }

    private boolean canMoveDown() {
        char ch = state.screen.getChar(x, y);
        return (y > 1) && (ch == EMPTY_CHAR || ch == LADDER_CHAR || ch == ROPE_CHAR  || ch == TREASURE_CHAR);
    }
}
