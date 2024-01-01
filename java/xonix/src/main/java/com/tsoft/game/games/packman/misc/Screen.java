package com.tsoft.game.games.packman.misc;

import com.badlogic.gdx.graphics.Color;
import com.tsoft.game.utils.TextScreen;

public class Screen extends TextScreen {

    public static final int WIDTH = 80;
    public static final int HEIGHT = 32;

    public static final int FONT_WIDTH = 7;
    public static final int FONT_HEIGHT = 14;

    public static final char EMPTY_CHAR = ' ';
    public static final char DOT_CHAR = '.';
    public static final char MAGIC_CHAR = 'O';
    public static final char ENEMY_CHAR = '@';
    public static final char PLAYER_CHAR = 127;

    public Screen() {
        super(WIDTH, HEIGHT);
    }

    public void showStartMenu() {
        fill(EMPTY_CHAR, Color.WHITE);

        print(10, 22, "XXXXXX    XXXXX    XXXXXX   XXXXXX  XX   XX   XXXXX   XX   XX");
        print(10, 21, "XX   XX  XX   XX  XX     X    XX    XXX XXX  XX   XX  XXX  XX");
        print(10, 20, "XXXXXX   XXXXXXX  XX          XX    XX X XX  XXXXXXX  XX X XX");
        print(10, 19, "XX       XX   XX  XX     X    XX    XX   XX  XX   XX  XX  XXX");
        print(10, 18, "XX       XX   XX   XXXXXX   XXXXXX  XX   XX  XX   XX  XX   XX");

        print(27, 15, "COPYRIGHT (C) 2023 BY TSOFT");
        print(32, 3, "PRESS FIRE TO START");
    }
}
