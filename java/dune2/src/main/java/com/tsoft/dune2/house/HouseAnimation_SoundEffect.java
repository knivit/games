package com.tsoft.dune2.house;

/**
 * Voice information part of House Information. It is part of an array that
 *  stops when voiceID is 0xFF.
 */
public class HouseAnimation_SoundEffect {
    int animationID;                       /* The which AnimationID this SoundEffect belongs. */
    int voiceID;                           /* The SoundEffect to play. */
    int wait;                              /* How long to wait before we play this SoundEffect. */
}
