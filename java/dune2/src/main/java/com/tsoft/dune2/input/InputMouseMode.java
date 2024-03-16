package com.tsoft.dune2.input;

/**
 * There are three different mouse modes.
 *  It looks like only the first (normal) mode is ever used.
 */
public class InputMouseMode {

    public static final int INPUT_MOUSE_MODE_NORMAL = 0;                            /*!< Normal mouse mode. */
    public static final int INPUT_MOUSE_MODE_RECORD = 1;                            /*!< Record mouse events to a file. */
    public static final int INPUT_MOUSE_MODE_PLAY   = 2;                            /*!< Plays mouse events from a file. */
}