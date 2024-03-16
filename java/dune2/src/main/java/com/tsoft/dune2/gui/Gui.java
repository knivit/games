package com.tsoft.dune2.gui;

/**
 * flags used for GUI_DrawSprite()
 */
public class Gui {

    /* reverse X axis (void) (RTL = Right To Left) */
    public static final int DRAWSPRITE_FLAG_RTL = 0x0001;

    /* reverse Y axis (void) */
    public static final int DRAWSPRITE_FLAG_BOTTOMUP = 0x0002;

    /* Zoom (int zoom_factor_x, int zoomRatioY) UNUSED ? */
    public static final int DRAWSPRITE_FLAG_ZOOM = 0x0004;

    /* Remap (uint8* remap, int remapCount) */
    public static final int DRAWSPRITE_FLAG_REMAP = 0x0100;

    /* blur - SandWorm effect (void) */
    public static final int DRAWSPRITE_FLAG_BLUR = 0x0200;

    /* sprite has house colors (set internally, no need to be set by caller) */
    public static final int DRAWSPRITE_FLAG_SPRITEPAL = 0x0400;

    /* Set increment value for blur/sandworm effect (int) UNUSED ? */
    public static final int DRAWSPRITE_FLAG_BLURINCR = 0x1000;

    /* house colors argument (uint8 houseColors[16]) */
    public static final int DRAWSPRITE_FLAG_PAL = 0x2000;

    /* position relative to widget (void)*/
    public static final int DRAWSPRITE_FLAG_WIDGETPOS = 0x4000;

    /* position posX,posY is relative to center of sprite */
    public static final int DRAWSPRITE_FLAG_CENTER = 0x8000;
}
