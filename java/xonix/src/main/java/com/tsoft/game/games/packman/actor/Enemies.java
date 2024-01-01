package com.tsoft.game.games.packman.actor;

import com.badlogic.gdx.graphics.Color;
import com.tsoft.game.utils.geom.Point;
import com.tsoft.game.utils.timer.SingleTimer;

import java.util.List;

import static com.tsoft.game.games.packman.Packman.global;
import static com.tsoft.game.games.packman.misc.Screen.EMPTY_CHAR;
import static com.tsoft.game.games.packman.misc.Screen.ENEMY_CHAR;

public class Enemies {

    private static class Enemy {
        int sx;
        int sy;

        int x;
        int y;
        int dx;
        int dy;

        Color color;
        char offCh;
        Color offColor;
    }

    private static final Color[] enemyColors = new Color[] { Color.RED, Color.YELLOW, Color.PINK, Color.MAGENTA };

    private final SingleTimer escapeTimer = new SingleTimer();

    private final Enemy[] enemyArr = new Enemy[4];
    private boolean escaping;

    public void create() {
        List<Point> points = global.screen.findChar('@');

        int n = 0;
        for (Point p : points) {
            Enemy e = new Enemy();
            e.sx = p.x;
            e.sy = p.y;
            e.x = p.x;
            e.y = p.y;
            e.offCh = EMPTY_CHAR;
            e.color = enemyColors[n];

            enemyArr[n] = e;
            n ++;
        }
    }

    public void reset() {
        escapeTimer.stop();
        escaping = false;

        for (Enemy e : enemyArr) {
            hide(e);
            e.x = e.sx;
            e.y = e.sy;
            show(e);
        }
    }

    public void move() {
        escaping = escapeTimer.isActive(global.time);

        for (Enemy e : enemyArr) {
            if (escaping) {
                escape(e);
            } else {
                hunt(e);
            }
        }
    }

    public void startEscaping() {
        escapeTimer.start(global.time, 5000);
    }

    private void hunt(Enemy e) {
    }

    private void escape(Enemy e) {
    }

    private void hide(Enemy e) {
        global.screen.putChar(e.x, e.y, e.offCh, e.offColor);
    }

    private void show(Enemy e) {
        e.offCh = global.screen.getChar(e.x, e.y);
        e.offColor = global.screen.getColor(e.x, e.y);

        Color color = escaping ? Color.BLUE : e.color;
        global.screen.putChar(e.x, e.y, ENEMY_CHAR, color);
    }
}
