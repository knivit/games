package com.tsoft.dune2.house;

/**
 * Subtitle information part of House Information. It is part of an array that
 *  stops when stringID is 0xFFFF.
 */
public class HouseAnimation_Subtitle {
    int stringID;                       /* StringID for the subtitle. */
    int colour;                         /* Colour of the subtitle. */
    int animationID;                    /* To which AnimationID this Subtitle belongs. */
    int top;                            /* The top of the subtitle, in pixels. */
    int waitFadein;                     /* How long to wait before we fadein this Subtitle. */
    int paletteFadein;                  /* How many ticks the palette update should take when appearing. */
    int waitFadeout;                    /* How long to wait before we fadeout this Subtitle. */
    int paletteFadeout;                 /* How many ticks the palette update should take when disappearing. */
}
