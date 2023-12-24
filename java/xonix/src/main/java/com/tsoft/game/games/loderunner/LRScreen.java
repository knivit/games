package com.tsoft.game.games.loderunner;

import com.tsoft.game.utils.TextScreen;

public class LRScreen extends TextScreen {

    public static final char EMPTY_CHAR = ' ';
    public static final char LADDER_CHAR = 'H';
    public static final char WALL_CHAR = 'X';
    public static final char TREASURE_CHAR = '*';
    public static final char ROPE_CHAR = '-';

    public static final int WIDTH = 80;
    public static final int HEIGHT = 25;

    public static final int FONT_WIDTH = 7;
    public static final int FONT_HEIGHT = 14;

    public LRScreen() {
        super(WIDTH, HEIGHT);
    }
}
