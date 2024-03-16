package com.tsoft.dune2.house;

/**
 * The information for a single animation frame in House Animation. It is part
 *  of an array that stops when duration is 0.
 */
public class HouseAnimation_Animation {
	String string;                                   /* Name of the WSA for this animation. */
    int duration;                                    /* Duration of this animation (in 1/10th sec). */
    int frameCount;                                  /* Amount of frames in this animation. */
    int flags;                                       /* Flags of the animation. */
}
