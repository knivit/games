package com.tsoft.dune2.house;

/**
 * HouseAnimation flags
 * see GameLoop_PlayAnimation()
 */
public class HouseAnimationFlag {

    public static int HOUSEANIM_FLAGS_MODE0        = 0;	    /* no WSA, only text or voice */
    public static int HOUSEANIM_FLAGS_MODE1        = 1;	    /* WSA Looping */
    public static int HOUSEANIM_FLAGS_MODE2        = 2;	    /* WSA display from first to end frame*/
    public static int HOUSEANIM_FLAGS_MODE3        = 3;	    /* display WSA unique frame (frameCount field) */
    public static int HOUSEANIM_FLAGS_FADEINTEXT   = 0x04;	/* fade in text at the beginning */
    public static int HOUSEANIM_FLAGS_FADEOUTTEXT  = 0x08;	/* fade out text at the end */
    public static int HOUSEANIM_FLAGS_FADETOWHITE  = 0x10;	/* Fade palette to all while at the end */
    public static int HOUSEANIM_FLAGS_POS0_0       = 0x20;	/* Position (0,0) - default is (8,24) */
    public static int HOUSEANIM_FLAGS_DISPLAYFRAME = 0x40;	/* force display in frame buffer (not screen) */
    public static int HOUSEANIM_FLAGS_FADEIN2      = 0x80;	/*  */
    public static int HOUSEANIM_FLAGS_FADEIN       = 0x400;	/*  */
}
