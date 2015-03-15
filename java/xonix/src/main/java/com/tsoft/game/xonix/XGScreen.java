package com.tsoft.game.xonix;

import com.tsoft.game.Assert;
import com.tsoft.game.TextScreen;

import java.awt.image.BufferedImage;

public class XGScreen extends TextScreen {
    public static final int WIDTH = 80;
    public static final int HEIGHT = 25;

    public static final int FONT_WIDTH = 7;
    public static final int FONT_HEIGHT = 14;

    public static final char EMPTY_CHAR = 0;
    public static final char BORDER_CHAR = 'X';

    public XGScreen(int width, int height, BufferedImage[] fontImages) {
        super(width, height, fontImages);

        Assert.isTrue(FONT_WIDTH == fontImages[0].getWidth(), "Invalid font width=" + fontImages[0].getWidth() + ", must be " + FONT_WIDTH);
        Assert.isTrue(FONT_HEIGHT == fontImages[0].getHeight(), "Invalid font height=" + fontImages[0].getHeight() + ", must be " + FONT_HEIGHT);
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

        print(19, 8,  "XX   XX   XXXXX   XX    XX  XXXXXX  XX   XX");
        print(19, 9,  " XX XX   XX   XX  XXX   XX    XX     XX XX ");
        print(19, 10, "  XXX    XX   XX  XX XX XX    XX      XXX  ");
        print(19, 11, " XX XX   XX   XX  XX  XXXX    XX     XX XX ");
        print(19, 12, "XX   XX   XXXXX   XX    XX  XXXXXX  XX   XX");

        print(27, 15, "COPYRIGHT (C) 2011 BY TSOFT");
    }
}
