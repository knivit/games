package com.tsoft.game.utils;

import com.badlogic.gdx.graphics.Color;

import java.util.Arrays;

public abstract class TextScreen {

    private final int height;
    private final int width;

    private final char[][] screen;
    private final float[][] colors;

    public abstract void update(TextSprite sprite, int x, int y);

    public TextScreen(int width, int height, int maxCode) {
        this.height = height;
        this.width = width;

        // the play field
        screen = new char[width][height];

        // create a color table and fill it with white
        colors = new float[maxCode][3];
        for (int i = 0; i < maxCode; i ++) {
            colors[i][0] = colors[i][1] = colors[i][2] = 1;
        }
    }

    public char getChar(int x, int y) {
        checkXY(x, y);
        return screen[x][y];
    }

    public int getCode(int x, int y) {
        return getCode(getChar(x, y));
    }

    public int getCode(char ch) {
        return Math.max(ch - ' ', 0);
    }

    public char[] getLine(int y) {
        checkXY(0, y);
        char[] clone = new char[width];
        for (int x = 0; x < width; x ++) {
            clone[x] = screen[x][y];
        }
        return clone;
    }

    public void putChar(int x, int y, char ch) {
        checkXY(x, y);
        screen[x][y] = ch;
    }

    public void getColor(Color color, int code) {
        checkCode(code);
        float[] rgb = colors[code];
        color.r = rgb[0];
        color.g = rgb[1];
        color.b = rgb[2];
        color.a = 1f;
    }

    public void putColor(int code, float r, float g, float b) {
        checkCode(code);
        colors[code][0] = r;
        colors[code][1] = g;
        colors[code][2] = b;
    }

    public void fill(int x1, int y1, int x2, int y2, char ch) {
        for (int y = y1; y < y2; y ++) {
            for (int x = x1; x < x2; x ++) {
                putChar(x, y, ch);
            }
        }
    }

    public void fill(char ch) {
        fill(0, 0, width, height, ch);
    }

    public void line(int x1, int y1, int x2, int y2, char ch) {
        if (x1 == x2) {
            for (int y = y1; y < y2; y++) {
                putChar(x1, y, ch);
            }
        } else {
            for (int x = x1; x < x2; x ++) {
                putChar(x, y1, ch);
            }
        }
    }

    public void print(int x, int y, String msg, Object ... params) {
        String formattedMsg = String.format(msg, params);
        for (int i = 0; i < formattedMsg.length(); i ++) {
            putChar(x, y, formattedMsg.charAt(i));
            x ++;
            if (x >= width) {
                x = 0;
                y ++;
                if (y >= height) {
                    y = 0;
                }
            }
        }
    }

    public int replace(int x1, int y1, int x2, int y2, char src, char dest) {
        int count = 0;
        for (int y = y1; y < y2; y ++) {
            for (int x = x1; x < x2; x ++) {
                if (getChar(x, y) == src) {
                    putChar(x, y, dest);
                    count ++;
                }
            }
        }
        return count;
    }

    public int getCharCount(int x1, int y1, int x2, int y2, char src) {
        int count = 0;
        for (int y = y1; y < y2; y ++) {
            for (int x = x1; x < x2; x ++) {
                if (getChar(x, y) == src) {
                    count ++;
                }
            }
        }
        return count;
    }

    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }

    private void checkXY(int x, int y) {
        if (x < 0 || x >= width) {
            throw new IllegalArgumentException("Invalid argument x=" + x + ", must be in [0, " + (width - 1) + "]");
        }

        if (y < 0 || y >= height) {
            throw new IllegalArgumentException("Invalid argument y=" + y + ", must be in [0, " + (height - 1) + "]");
        }
    }

    private void checkCode(int code) {
        if (code < 0 || code > colors.length) {
            throw new IllegalArgumentException("Invalid argument code = " + code + ", must be in [0 .. " + (colors.length - 1) + "]");
        }
    }
}
