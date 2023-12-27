package com.tsoft.game.games.snake.actor;

import com.badlogic.gdx.graphics.Color;
import com.tsoft.game.games.snake.misc.Screen;
import com.tsoft.game.games.snake.scene.PlayStatus;
import com.tsoft.game.utils.TextSprite;
import com.tsoft.game.utils.geom.Point;

import java.util.List;

import static com.tsoft.game.games.snake.misc.Sound.TREASURE_SOUND;
import static com.tsoft.game.games.snake.Snake.state;
import static com.tsoft.game.games.snake.misc.Screen.*;
import static com.tsoft.game.games.snake.misc.Sound.STEP_SOUND;
import static com.tsoft.game.games.snake.misc.Sound.REMOVE_LIFE_SOUND;

public class Player {

    private final PlayStatus status;

    private final int[] xPos = new int[1024];
    private final int[] yPos = new int[1024];
    private final Point dir = new Point(0, 0);

    private int len;
    private int level;
    private int mouseCount;

    private boolean isNextLevel;

    public Player(PlayStatus status) {
        this.status = status;
    }

    public void create(int level) {
        this.level = level;

        List<Point> head = state.screen.findChar(0, 1, Screen.WIDTH, Screen.HEIGHT, SNAKE_HEAD_CHAR);
        xPos[0] = head.get(0).x;
        yPos[0] = head.get(0).y;
        len = 1;

        dir.x = 0;
        dir.y = 1;

        isNextLevel = false;
    }

    public void move() {
        hide();

        // find out move direction
        Point off = state.controller.offset;
        if (off.x != 0 || off.y != 0) {
            if (off.x != 0) {
                dir.x = off.x;
                dir.y = 0;
            } else {
                dir.x = 0;
                dir.y = off.y;
            }
        }

        // move head
        int hX = xPos[0];
        int hY = yPos[0];
        xPos[0] += dir.x;
        yPos[0] += dir.y;

        // collision check
        char ch = state.screen.getChar(xPos[0], yPos[0]);

        if (ch == MOUSE_CHAR) {
            // eat the mouse and grow
            len ++;
            xPos[len] = hX;
            yPos[len] = hY;

            mouseCount ++;
            if (mouseCount > 10 + level*3) {
                isNextLevel = true;
            }

            state.sound.push(TREASURE_SOUND);
            status.addScore(1);
        } else if (ch != EMPTY_CHAR) {
            status.removeLife();
            state.sound.push(REMOVE_LIFE_SOUND);
        } else {
            state.sound.push(STEP_SOUND);
        }

        show();
    }

    public boolean isNextLevel() {
        return isNextLevel;
    }

    private void hide() {
        for (int i = 0; i < len; i ++) {
            TextSprite sp = state.screen.sprite(xPos[i], yPos[i]);
            sp.ch = EMPTY_CHAR;
            sp.rgba = Color.BLACK.toIntBits();
        }
    }

    private void show() {
        for (int i = 0; i < len; i ++) {
            TextSprite sp = state.screen.sprite(xPos[i], yPos[i]);
            if (i == 0) {
                sp.ch = SNAKE_HEAD_CHAR;
                sp.rgba = Color.RED.toIntBits();
            } else {
                sp.ch = SNAKE_TAIL_CHAR;
                sp.rgba = Color.GREEN.toIntBits();
            }
        }
    }
}
