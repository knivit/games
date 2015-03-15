package com.tsoft.game.loderunner;

import com.tsoft.game.Assert;
import com.tsoft.game.TextScreen;

import java.awt.image.BufferedImage;

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

    public LRScreen(BufferedImage[] fontImages) {
        super(WIDTH, HEIGHT, fontImages);

        Assert.isTrue(FONT_WIDTH == fontImages[0].getWidth(), "Invalid font width=" + fontImages[0].getWidth() + ", must be " + FONT_WIDTH);
        Assert.isTrue(FONT_HEIGHT == fontImages[0].getHeight(), "Invalid font height=" + fontImages[0].getHeight() + ", must be " + FONT_HEIGHT);
    }
}
