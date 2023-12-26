package com.tsoft.game.games.snake;

import com.tsoft.game.utils.TextScreen;

public class SNScreen extends TextScreen {

    public static final int WIDTH = 80;
    public static final int HEIGHT = 25;

    public static final int FONT_WIDTH = 7;
    public static final int FONT_HEIGHT = 14;

    public static final char EMPTY_CHAR = 0;
    public static final char BORDER_CHAR = 'X';
    public static final char MOUSE_CHAR = '@';
    public static final char SNAKE_HEAD_CHAR = 'S';
    public static final char SNAKE_TAIL_CHAR = '*';

    public SNScreen() {
        super(WIDTH, HEIGHT);
    }

    public void showStartMenu() {
        fill(EMPTY_CHAR);
        showBorder();

        print(19, 17,  " XXXXX   XX   XX    XXXX    XX  XX  XXXXXXX");
        print(19, 16,  "XX    X  XXX  XX  XX    XX  XX XX   XX     ");
        print(19, 15,  "  XXX    XX X XX  XXXXXXXX  XXX     XXXXX  ");
        print(19, 14,  "X    XX  XX  XXX  XX    XX  XX XX   XX     ");
        print(19, 13,  " XXXXX   XX   XX  XX    XX  XX  XX  XXXXXXX");

        print(27, 10, "COPYRIGHT (C) 2011 BY TSOFT");
        print(36, 8, "SPEED (1-9): 1");
        print(42, 6, "START");
    }

    private void showBorder() {
        line(0, 0, 0, getHeight(), BORDER_CHAR);
        line(getWidth() - 1, 0, getWidth() - 1, getHeight(), BORDER_CHAR);
        line(0, 0, getWidth(), 0, BORDER_CHAR);
        line(0, getHeight() - 1, getWidth(), getHeight() - 1, BORDER_CHAR);
    }
}
