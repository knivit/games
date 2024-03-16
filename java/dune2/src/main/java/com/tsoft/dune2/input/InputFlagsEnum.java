package com.tsoft.dune2.input;

/**
 * Several flags for input handling.
 */
public class InputFlagsEnum {

    public static final int INPUT_FLAG_KEY_REPEAT   = 0x0001;      /*!< Allow repeated input of the same key. */
    public static final int INPUT_FLAG_NO_TRANSLATE = 0x0002;      /*!< Don't translate a key. */
    public static final int INPUT_FLAG_UNKNOWN_0004 = 0x0004;      /*!< ?? */
    public static final int INPUT_FLAG_UNKNOWN_0008 = 0x0008;      /*!< ?? */
    public static final int INPUT_FLAG_UNKNOWN_0010 = 0x0010;      /*!< ?? */
    public static final int INPUT_FLAG_UNKNOWN_0020 = 0x0020;      /*!< ?? */
    public static final int INPUT_FLAG_UNKNOWN_0040 = 0x0040;      /*!< ?? */
    public static final int INPUT_FLAG_UNKNOWN_0080 = 0x0080;      /*!< ?? */
    public static final int INPUT_FLAG_UNKNOWN_0100 = 0x0100;      /*!< ?? */
    public static final int INPUT_FLAG_UNKNOWN_0200 = 0x0200;      /*!< ?? */
    public static final int INPUT_FLAG_UNKNOWN_0400 = 0x0400;      /*!< ?? */
    public static final int INPUT_FLAG_KEY_RELEASE  = 0x0800;      /*!< Record release of keys (not for buttons). */
    public static final int INPUT_FLAG_MOUSE_EMUL   = 0x1000;      /*!< Enable mouse emulation with numeric keypad */
    public static final int INPUT_FLAG_KBD_MOUSE_CLK = 0x2000;     /*!< Enable mouse click emulation with Enter, etc. */
}

