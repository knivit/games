package com.tsoft.game.games.packman.actor;

import com.badlogic.gdx.graphics.Color;
import com.tsoft.game.utils.GameController;
import com.tsoft.game.utils.geom.Point;

import java.util.List;

import static com.tsoft.game.games.packman.Packman.global;
import static com.tsoft.game.games.packman.misc.Screen.*;
import static com.tsoft.game.games.packman.misc.Sound.*;

public class Player {

    public boolean isNextLevel;
    public boolean isResetLevel;

    private int x;
    private int y;
    private int dx;
    private int dy;

    private int sx;
    private int sy;
    private int dotCount;

    public void create() {
        dotCount = global.screen.getCharCount(DOT_CHAR);

        List<Point> points = global.screen.findChar('P');
        sx = points.get(0).x;
        sy = points.get(0).y;

        reset();
    }

    public void reset() {
        isNextLevel = false;
        isResetLevel = false;

        hide();
        x = sx;
        y = sy;
        dx = -1;
        dy = 0;
        show();
    }

    public void move(GameController.State controller) {
        if (controller.dx != 0) {
            dx = controller.dx;
            dy = 0;
        } else if (controller.dy != 0) {
            dx = 0;
            dy = controller.dy;
        }

        int nx = Math.min(Math.max(x + dx, 0), global.screen.getWidth() - 1);
        int ny = Math.min(Math.max(y + dy, 1), global.screen.getHeight() - 1);

        char ch = global.screen.getChar(nx, ny);

        hide();

        if (ch == DOT_CHAR) {
            x = nx;
            y = ny;

            dotCount --;
            if (dotCount == 0) {
                isNextLevel = true;
            }

            global.sound.push(DOT_SOUND);
            global.status.addScore(10);
        } else if (ch == MAGIC_CHAR) {
            x = nx;
            y = ny;

            global.enemies.startEscaping();

            global.sound.push(MAGIC_SOUND);
            global.status.addScore(50);
        } else if (ch == ENEMY_CHAR) {
            isResetLevel = true;

            global.status.removeLife();
            global.sound.push(REMOVE_LIFE_SOUND);
        } else if (ch == EMPTY_CHAR) {
            x = nx;
            y = ny;
        }

        show();
    }

    private void hide() {
        global.screen.putChar(x, y, ' ', Color.WHITE);
    }

    private void show() {
        global.screen.putChar(x, y, PLAYER_CHAR, Color.WHITE);
    }
}
