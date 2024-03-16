package com.tsoft.dune2.animation;

/**
 * The valid types for command in AnimationCommandStruct.
 */
public class AnimationCommand {

    public static final int ANIMATION_STOP = 1;                 /*!< Gracefully stop with animation. Clean up the tiles etc. */
    public static final int ANIMATION_ABORT = 2;                /*!< Abort animation. Leave it as it is. */
    public static final int ANIMATION_SET_OVERLAY_TILE = 3;     /*!< Set a new overlay tile. Param: the new overlay tile. */
    public static final int ANIMATION_PAUSE = 4;                /*!< Pause the animation. Param: amount of ticks to pause. */
    public static final int ANIMATION_REWIND = 5;               /*!< Rewind the animation.*/
    public static final int ANIMATION_PLAY_VOICE = 6;           /*!< Play a voice. Param: the voice to play. */
    public static final int ANIMATION_SET_GROUND_TILE = 7;      /*!< Set a new ground tile. Param: the new ground tile. */
    public static final int ANIMATION_FORWARD = 8;              /*!< Forward the animation. Param: how many commands to forward. */
    public static final int ANIMATION_SET_ICONGROUP = 9;        /*!< Set a newicongroup. Param: the new icongroup. */
}
