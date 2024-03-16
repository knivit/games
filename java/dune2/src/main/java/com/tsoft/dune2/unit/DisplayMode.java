package com.tsoft.dune2.unit;

/**
 * Types of DisplayModes available in the game.
 */
public class DisplayMode {

    public static final int DISPLAYMODE_SINGLE_FRAME        = 0;
    public static final int DISPLAYMODE_UNIT                = 1;    /* Ground: N,NE,E,SE,S.  Air: N,NE,E. */
    public static final int DISPLAYMODE_ROCKET              = 2;    /* N,NNE,NE,ENE,E. */
    public static final int DISPLAYMODE_INFANTRY_3_FRAMES   = 3;    /* N,E,S; 3 frames per direction. */
    public static final int DISPLAYMODE_INFANTRY_4_FRAMES   = 4;    /* N,E,S; 4 frames per direction. */
    public static final int DISPLAYMODE_ORNITHOPTER         = 5;    /* N,NE,E; 3 frames per direction. */
}
