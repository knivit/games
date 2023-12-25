package com.tsoft.game.games.loderunner;

import com.tsoft.game.utils.TextScreen;
import com.tsoft.game.utils.TextSprite;

public class LRScreen extends TextScreen {

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

    public LRScreen() {
        super(WIDTH, HEIGHT, 96);

        putColor(getCode(ROBOT_CHAR), 1, 0, 0);
        putColor(getCode(TREASURE_CHAR), 1, 1, 0);
    }

    @Override
    public void update(TextSprite sprite, int x, int y) {
        sprite.n = getCode(x, y);
        sprite.x = x * FONT_WIDTH;
        sprite.y = y * FONT_HEIGHT;
        getColor(sprite.color, sprite.n);
    }
}
