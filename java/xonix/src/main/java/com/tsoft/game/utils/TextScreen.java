package com.tsoft.game.utils;

import com.badlogic.gdx.graphics.Color;

public class TextScreen {

    private final int height;
    private final int width;

    private final TextSprite[][] screen;

    public TextScreen(int width, int height) {
        this.height = height;
        this.width = width;

        screen = new TextSprite[width][height];

        for (int y = 0; y < height; y ++) {
            for (int x = 0; x < width; x ++) {
                screen[x][y] = new TextSprite();
                screen[x][y].rgba = Color.WHITE.toIntBits();
            }
        }
    }

    public char getChar(int x, int y) {
        checkXY(x, y);
        return screen[x][y].ch;
    }

    public TextSprite sprite(int x, int y) {
        checkXY(x, y);
        return screen[x][y];
    }

    public char[] getLine(int y) {
        checkXY(0, y);
        char[] clone = new char[width];
        for (int x = 0; x < width; x ++) {
            TextSprite sprite = screen[x][y];
            clone[x] = (sprite == null) ? ' ' : sprite.ch;
        }
        return clone;
    }

    public void putChar(int x, int y, char ch) {
        checkXY(x, y);
        screen[x][y].ch = ch;
    }

    public int getColor(int x, int y) {
        checkXY(x, y);
        return screen[x][y].rgba;
    }

    public void putColor(int x, int y, int rgba) {
        checkXY(x, y);
        screen[x][y].rgba = rgba;
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
}
