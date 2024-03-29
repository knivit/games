package com.tsoft.game.games.loderunner.misc;

import com.tsoft.game.utils.TextScreen;

public class Screen extends TextScreen {

    public static final char EMPTY_CHAR = ' ';
    public static final char LADDER_CHAR = 'H';
    public static final char WALL_CHAR = 'X';
    public static final char TREASURE_CHAR = '*';
    public static final char ROPE_CHAR = '-';

    public static final char ROBOT_START_CHAR = 'Z';
    public static final char ROBOT_CHAR = '@';

    public static final char PLAYER_START_CHAR = 'U';
    public static final char PLAYER_CHAR = 127;

    public static final int WIDTH = 80;
    public static final int HEIGHT = 25;

    public static final int FONT_WIDTH = 7;
    public static final int FONT_HEIGHT = 14;

    public Screen() {
        super(WIDTH, HEIGHT);
    }
}
