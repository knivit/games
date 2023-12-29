package com.tsoft.game.games.snake.actor;

import com.badlogic.gdx.graphics.Color;
import com.tsoft.game.games.snake.misc.Screen;
import com.tsoft.game.games.snake.scene.PlayStatus;
import com.tsoft.game.utils.GameController;
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

    private final int[] x = new int[1024];
    private final int[] y = new int[1024];

    private int dx;
    private int dy;
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
        x[0] = head.get(0).x;
        y[0] = head.get(0).y;
        len = 1;

        dx = 0;
        dy = 1;

        isNextLevel = false;
    }

    public void move(GameController.State controller) {
        hide();

        if (controller.dx != 0) {
            dx = controller.dx;
            dy = 0;
        } else if (controller.dy != 0) {
            dx = 0;
            dy = controller.dy;
        }

        int nx = Math.min(Math.max(x[0] + dx, 0), state.screen.getWidth() - 1);
        int ny = Math.min(Math.max(y[0] + dy, 1), state.screen.getHeight() - 1);

        char ch = state.screen.getChar(nx, ny);

        if (ch == MOUSE_CHAR) {
            grow();
            x[0] = nx;
            y[0] = ny;

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
            if (len < 5 + level*2) {
                grow();
            } else {
                shift(len - 1);
            }
            x[0] = nx;
            y[0] = ny;

            state.sound.push(STEP_SOUND);
        }

        show();
    }

    private void grow() {
        shift(len);
        len ++;
    }

    private void shift(int len) {
        for (int i = len; i > 0; i --) {
            x[i] = x[i - 1];
            y[i] = y[i - 1];
        }
    }

    public boolean isNextLevel() {
        return isNextLevel;
    }

    private void hide() {
        for (int i = 0; i < len; i ++) {
            TextSprite sp = state.screen.sprite(x[i], y[i]);
            sp.ch = EMPTY_CHAR;
            sp.color = Color.WHITE;
        }
    }

    private void show() {
        for (int i = 0; i < len; i ++) {
            state.screen.putChar(x[i], y[i], SNAKE_HEAD_CHAR);
        }
    }
}
