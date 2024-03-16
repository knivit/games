package com.tsoft.dune2.animation;

/**
 * How a single command looks like.
 */
public class AnimationCommandStruct {

    public int command;                                         /*!< The command of this command (see AnimationCommand). */
    public int parameter;                                       /*!< The parameter for this command. */

    public AnimationCommandStruct(int command, int parameter) {
        this.command = command;
        this.parameter = parameter;
    }
}
