package com.tsoft.game.games.packman.actor;

import com.badlogic.gdx.graphics.Color;
import com.tsoft.game.utils.GameController;
import com.tsoft.game.utils.base.Point;

import java.util.List;

import static com.tsoft.game.games.packman.Packman.global;
import static com.tsoft.game.games.packman.misc.Screen.*;
import static com.tsoft.game.games.packman.misc.Sound.*;

public class Player {

    public boolean isNextLevel;
    public boolean isResetLevel;

    // start position
    private int sx;
    private int sy;

    // current position and move direction
    private int x;
    private int y;
    private int dx;
    private int dy;

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

    private static final char[] ALLOWED_CHARS = new char[] { EMPTY_CHAR, DOT_CHAR, MAGIC_CHAR, GHOST_CHAR};

    public void move(GameController.State controller) {
        FieldPos pos = new FieldPos();
        boolean useController = false;
        if (controller.dx != 0 || controller.dy != 0) {
            pos.move(x, y, controller.dx, controller.dy);
            if (pos.in(ALLOWED_CHARS)) {
                useController = true;
            }
        }

        if (!useController) {
            pos.move(x, y, dx, dy);
        }

        char ch = pos.ch;

        hide();

        boolean moved = true;
        if (ch == DOT_CHAR) {
            dotCount --;
            if (dotCount == 0) {
                isNextLevel = true;
            }

            global.status.addScore(10);

            global.sound.push(DOT_SOUND);
        } else if (ch == MAGIC_CHAR) {
            global.enemies.startEscaping();

            global.status.addScore(50);

            global.sound.push(MAGIC_SOUND);
        } else if (ch == GHOST_CHAR) {
            moved = false;
            isResetLevel = true;

            global.status.removeLife();

            global.sound.push(REMOVE_LIFE_SOUND);
        } else if (ch != EMPTY_CHAR) {
            moved = false;
        }

        if (moved) {
            x = pos.x;
            y = pos.y;
            dx = pos.dx;
            dy = pos.dy;
        }

        show();
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    private void hide() {
        global.screen.putChar(x, y, ' ', Color.WHITE);
    }

    private void show() {
        global.screen.putChar(x, y, PLAYER_CHAR, Color.WHITE);
    }
}
