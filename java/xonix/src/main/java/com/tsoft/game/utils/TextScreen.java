package com.tsoft.game.utils;

import com.badlogic.gdx.graphics.Color;
import com.tsoft.game.utils.base.Point;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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
                screen[x][y].color = Color.WHITE;
            }
        }
    }

    public char getChar(int x, int y) {
        checkXY(x, y);
        return screen[x][y].ch;
    }

    public Color getColor(int x, int y) {
        checkXY(x, y);
        return screen[x][y].color;
    }

    public TextSprite sprite(int x, int y) {
        checkXY(x, y);
        return screen[x][y];
    }

    public char[] getLine(int y) {
        checkXY(0, y);
        char[] clone = new char[width];
        for (int x = 0; x < width; x ++) {
            clone[x] = screen[x][y].ch;
        }
        return clone;
    }

    public void putChar(int x, int y, char ch) {
        putChar(x, y, ch, null);
    }

    public void putChar(int x, int y, char ch, Color color) {
        checkXY(x, y);
        screen[x][y].ch = ch;
        if (color != null) {
            screen[x][y].color = color;
        }
    }

    public void putColor(int x, int y, Color color) {
        checkXY(x, y);
        screen[x][y].color = color;
    }

    public void fill(int x1, int y1, int x2, int y2, char ch) {
        fill(x1, y1, x2, y2, ch, null);
    }

    public void fill(int x1, int y1, int x2, int y2, char ch, Color color) {
        for (int y = y1; y < y2; y ++) {
            for (int x = x1; x < x2; x ++) {
                putChar(x, y, ch, color);
            }
        }
    }

    public void fill(char ch) {
        fill(0, 0, width, height, ch);
    }

    public void fill(char ch, Color color) {
        fill(0, 0, width, height, ch, color);
    }

    public void line(int x1, int y1, int x2, int y2, char ch) {
        line(x1, y1, x2, y2, ch, null);
    }

    public void line(int x1, int y1, int x2, int y2, char ch, Color color) {
        if (x1 == x2) {
            for (int y = y1; y < y2; y++) {
                putChar(x1, y, ch, color);
            }
        } else {
            for (int x = x1; x < x2; x ++) {
                putChar(x, y1, ch, color);
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
                y --;
                if (y < 0) {
                    y = height - 1;
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

    public List<Point> findChar(char ch) {
        return findChar(0, 0, width, height, ch);
    }

    public List<Point> findChar(int x1, int y1, int x2, int y2, char ch) {
        List<Point> result = null;
        for (int y = y1; y < y2; y ++) {
            for (int x = x1; x < x2; x ++) {
                if (getChar(x, y) == ch) {
                    if (result == null) {
                        result = new ArrayList<>();
                    }
                    result.add(new Point(x, y));
                }
            }
        }

        return (result == null) ? Collections.emptyList() : result;
    }

    public int getCharCount(char ch) {
        return getCharCount(0, 0, width, height, ch);
    }

    public int getCharCount(int x1, int y1, int x2, int y2, char ch) {
        int count = 0;

        for (int y = y1; y < y2; y ++) {
            for (int x = x1; x < x2; x ++) {
                if (getChar(x, y) == ch) {
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
