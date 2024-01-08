package com.tsoft.game.games.packman.actor;

import com.badlogic.gdx.graphics.Color;
import com.tsoft.game.utils.base.Point;
import com.tsoft.game.utils.timer.SingleTimer;

import java.util.List;
import java.util.Random;

import static com.tsoft.game.games.packman.Packman.global;
import static com.tsoft.game.games.packman.misc.Screen.*;
import static com.tsoft.game.games.packman.misc.Screen.MAGIC_CHAR;

public class Enemies {

    private static final Random random = new Random();

    private static class Ghost {
        // start position
        int sx;
        int sy;

        // current position and move direction
        int x;
        int y;
        int dx;
        int dy;
        int len;

        // personal attributes
        boolean inside;
        Color color;
        char offCh;
        Color offColor;
    }

    private static final Color[] ghostColors = new Color[] { Color.RED, Color.YELLOW, Color.PINK, Color.MAGENTA };

    private final SingleTimer escapeTimer = new SingleTimer();

    private final Ghost[] ghosts = new Ghost[4];
    private final Point gate = new Point();
    private boolean escaping;

    public void create() {
        List<Point> startPoints = global.screen.findChar(GHOST_CHAR);

        int n = 0;
        for (Point p : startPoints) {
            Ghost e = new Ghost();
            e.sx = e.x = p.x;
            e.sy = e.y = p.y;
            e.len = 0;
            e.inside = true;
            e.color = ghostColors[n];
            e.offCh = EMPTY_CHAR;
            e.offColor = Color.BLACK;

            ghosts[n] = e;
            n ++;
        }

        List<Point> gates = global.screen.findChar(GATE_CHAR);
        gate.x = gates.get(0).x;
        gate.y = gates.get(0).y + 1;
    }

    public void reset() {
        escapeTimer.stop();
        escaping = false;

        hide();

        for (Ghost e : ghosts) {
            e.x = e.sx;
            e.y = e.sy;
            e.len = 0;
        }

        show();
    }

    public void move() {
        escaping = escapeTimer.isActive(global.time);

        hide();

        for (Ghost e : ghosts) {
            if (escaping) {
                escape(e);
            } else {
                hunt(e);
            }
        }

        show();
    }

    public void startEscaping() {
        escapeTimer.start(global.time, 5000);
    }

    private static final char[] ALLOWED_CHARS = new char[] { EMPTY_CHAR, DOT_CHAR, MAGIC_CHAR, GHOST_CHAR};

    private static final int[] DX_ROTATION = new int[] { 0, 1, 0, -1 };
    private static final int[] DY_ROTATION = new int[] { 1, 0, -1, 0 };

    private void hunt(Ghost e) {
        FieldPos pos = new FieldPos();

        if (e.len == 0) {
            // random initial direction to move
            int n = random.nextInt(4);

            // check where it can move
            int cnt = 0;
            int[] allowedDirs = new int[4];
            for (int i = 0; i < 4; i ++) {
                int ndx = DX_ROTATION[n];
                int ndy = DY_ROTATION[n];

                pos.move(e.x, e.y, ndx, ndy);

                if (pos.in(ALLOWED_CHARS)) {
                    allowedDirs[cnt] = i;
                    cnt ++;
                }

                n = (n + 1) % 4;
            }

            if (cnt == 0) {
                // nowhere to go
                return;
            }

            // select a direction
            n = random.nextInt(cnt);
            e.dx = DX_ROTATION[allowedDirs[n]];
            e.dy = DY_ROTATION[allowedDirs[n]];

            // choose how long to move without retargeting
            e.len = random.nextInt(20);
        }

        // move
        pos.move(e.x, e.y, e.dx, e.dy);
        if (pos.in(ALLOWED_CHARS)) {
            e.x = pos.x;
            e.y = pos.y;
        } else {
            e.len = 0;
        }

        // decrease the path
        if (e.len > 0) {
            e.len --;
        }
    }

    private void escape(Ghost e) {
    }

    private void hide() {
        for (int i = 0; i < ghosts.length; i ++) {
            Ghost e = ghosts[i];
            global.screen.putChar(e.x, e.y, e.offCh, e.offColor);
        }
    }

    private void show() {
        for (int i = ghosts.length - 1; i >= 0; i --) {
            Ghost e = ghosts[i];

            e.offCh = global.screen.getChar(e.x, e.y);
            e.offColor = global.screen.getColor(e.x, e.y);

            Color color = escaping ? Color.BLUE : e.color;
            global.screen.putChar(e.x, e.y, GHOST_CHAR, color);
        }
    }
}
