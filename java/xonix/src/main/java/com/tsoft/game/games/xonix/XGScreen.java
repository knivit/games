package com.tsoft.game.games.xonix;

import com.tsoft.game.utils.TextScreen;

public class XGScreen extends TextScreen {

    public static final int WIDTH = 80;
    public static final int HEIGHT = 25;

    public static final int FONT_WIDTH = 7;
    public static final int FONT_HEIGHT = 14;

    public static final char EMPTY_CHAR = 0;
    public static final char BORDER_CHAR = 'X';

    public static final char PLAYER_CHAR = 127;
    public static final char PLAYER_PATH_CHAR = '+';

    public static final char INNER_FLY_CHAR = 'O';
    public static final char OUTER_FLY_CHAR = ' ';

    public XGScreen() {
        super(WIDTH, HEIGHT);
    }

    public void reset() {
        fill(EMPTY_CHAR);
        showBorder(1);
    }

    private void showBorder(int topY) {
        line(0, topY, 0, getHeight(), BORDER_CHAR);
        line(1, topY, 1, getHeight(), BORDER_CHAR);
        line(getWidth() - 2, topY, getWidth() - 2, getHeight(), BORDER_CHAR);
        line(getWidth() - 1, topY, getWidth() - 1, getHeight(), BORDER_CHAR);
        line(0, topY, getWidth(), topY, BORDER_CHAR);
        line(0, topY + 1, getWidth(), topY + 1, BORDER_CHAR);
        line(0, getHeight() - 2, getWidth(), getHeight() - 2, BORDER_CHAR);
        line(0, getHeight() - 1, getWidth(), getHeight() - 1, BORDER_CHAR);
    }

    public void showStartMenu() {
        fill(EMPTY_CHAR);
        showBorder(0);

        print(19, 17,  "XX   XX   XXXXX   XX    XX  XXXXXX  XX   XX");
        print(19, 16,  " XX XX   XX   XX  XXX   XX    XX     XX XX ");
        print(19, 15, "  XXX    XX   XX  XX XX XX    XX      XXX  ");
        print(19, 14, " XX XX   XX   XX  XX  XXXX    XX     XX XX ");
        print(19, 13, "XX   XX   XXXXX   XX    XX  XXXXXX  XX   XX");

        print(27, 10, "COPYRIGHT (C) 2011 BY TSOFT");
        print(32, 8, "PRESS FIRE TO START");
    }
}
