package com.tsoft.game.games.packman.actor;

import static com.tsoft.game.games.packman.Packman.global;

public class FieldPos {

    public int x;
    public int y;
    public int dx;
    public int dy;
    public char ch;

    public void move(int x, int y, int dx, int dy) {
        x += dx;
        if (x < 0) {
            x = global.screen.getWidth() - 1;
        }

        if (x > global.screen.getWidth() - 1) {
            x = 0;
        }

        y += dy;
        if (y < 0) {
            y = global.screen.getHeight() - 1;
        }

        if (y > global.screen.getHeight() - 1) {
            y = 1;
        }

        this.x = x;
        this.y = y;
        this.dx = dx;
        this.dy = dy;
        this.ch = global.screen.getChar(x, y);
    }

    public boolean in(char[] chs) {
        for (int i = 0; i < chs.length; i ++) {
            if (chs[i] == ch) {
                return true;
            }
        }
        return false;
    }
}
