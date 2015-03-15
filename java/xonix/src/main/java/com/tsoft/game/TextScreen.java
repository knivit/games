package com.tsoft.game;

import java.awt.image.BufferedImage;

public class TextScreen {
    private int height;
    private int width;

    private char[][] screen;
    private BufferedImage[] fontImages;

    public TextScreen(int width, int height, BufferedImage[] fontImages) {
        this.height = height;
        this.width = width;
        this.fontImages = fontImages;

        screen = new char[width][height];
    }

    public char getChar(int x, int y) {
        Assert.isTrue(x >= 0 && x < width, "Invalid argument x=" + x + ", must be in [0, " + (width - 1) + "]");
        Assert.isTrue(y >= 0 && y < height, "Invalid argument y=" + y + ", must be in [0, " + (height - 1) + "]");
        return screen[x][y];
    }

    public void putChar(int x, int y, char ch) {
        Assert.isTrue(x >= 0 && x < width, "Invalid argument x=" + x + ", must be in [0, " + (width - 1) + "]");
        Assert.isTrue(y >= 0 && y < height, "Invalid argument y=" + y + ", must be in [0, " + (height - 1) + "]");
        screen[x][y] = ch;
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

    public BufferedImage getFontImages(int charIndex) {
        return fontImages[charIndex];
    }

    public String getLogString() {
        StringBuilder buf = new StringBuilder("Screen\n");
        for (int y = 0; y < getHeight(); y ++) {
            for (int x = 0; x < getWidth(); x ++) {
                char ch = getChar(x, y);
                if (ch < 32) {
                    ch = ' ';
                }
                buf.append(ch);
            }
            buf.append('\n');
        }
        return buf.toString();
    }
}
