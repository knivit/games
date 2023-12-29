package com.tsoft.game.games.snake.misc;

import com.badlogic.gdx.graphics.Color;
import com.tsoft.game.utils.TextScreen;

public class Screen extends TextScreen {

    public static final int WIDTH = 80;
    public static final int HEIGHT = 25;

    public static final int FONT_WIDTH = 7;
    public static final int FONT_HEIGHT = 14;

    public static final char EMPTY_CHAR = ' ';
    public static final char BORDER_CHAR = 'X';
    public static final char MOUSE_CHAR = '$';
    public static final char SNAKE_HEAD_CHAR = 'O';

    public Screen() {
        super(WIDTH, HEIGHT);
    }

    public void showStartMenu() {
        reset();

        print(19, 17,  " XXXXX   XX   XX    XXXX    XX  XX  XXXXXXX");
        print(19, 16,  "XX    X  XXX  XX  XX    XX  XX XX   XX     ");
        print(19, 15,  "  XXX    XX X XX  XXXXXXXX  XXX     XXXXX  ");
        print(19, 14,  "X    XX  XX  XXX  XX    XX  XX XX   XX     ");
        print(19, 13,  " XXXXX   XX   XX  XX    XX  XX  XX  XXXXXXX");

        print(27, 10, "COPYRIGHT (C) 2011 BY TSOFT");
        print(36, 8, "SPEED (1-9): 1");
        print(36, 7, "START");
    }

    public void reset() {
        fill(EMPTY_CHAR);
        fill(Color.WHITE);
    }
}
