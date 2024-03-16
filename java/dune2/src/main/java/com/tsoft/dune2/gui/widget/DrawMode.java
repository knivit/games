package com.tsoft.dune2.gui.widget;

/**
 * Types of DrawMode available in the game.
 */
public class DrawMode {

    public static final int DRAW_MODE_NONE                = 0;                      /*!< Draw nothing. */
    public static final int DRAW_MODE_SPRITE              = 1;                      /*!< Draw a sprite. */
    public static final int DRAW_MODE_TEXT                = 2;                      /*!< Draw text. */
    public static final int DRAW_MODE_UNKNOWN3            = 3;
    public static final int DRAW_MODE_CUSTOM_PROC         = 4;                      /*!< Draw via a custom defined function. */
    public static final int DRAW_MODE_WIRED_RECTANGLE     = 5;                      /*!< Draw a wired rectangle. */
    public static final int DRAW_MODE_XORFILLED_RECTANGLE = 6;                      /*!< Draw a filled rectangle using xor. */

    public static final int DRAW_MODE_MAX                 = 7;
}
