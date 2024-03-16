package com.tsoft.dune2.gui;

/**
 * The possible selection types.
 */
public class SelectionType {

    public static final int SELECTIONTYPE_MENTAT    = 0;     /*!< Used in most mentat screens. */
    public static final int SELECTIONTYPE_TARGET    = 1;     /*!< Used when attacking or moving a unit, the target screen. */
    public static final int SELECTIONTYPE_PLACE     = 2;     /*!< Used when placing a structure. */
    public static final int SELECTIONTYPE_UNIT      = 3;     /*!< Used when selecting a Unit. */
    public static final int SELECTIONTYPE_STRUCTURE = 4;     /*!< Used when selecting a Structure or nothing. */
    public static final int SELECTIONTYPE_DEBUG     = 5;     /*!< Used when debugging scenario. */
    public static final int SELECTIONTYPE_UNKNOWN6  = 6;     /*!< ?? */
    public static final int SELECTIONTYPE_INTRO     = 7;     /*!< Used in intro of the game. */

    public static final int SELECTIONTYPE_MAX       = 8;
}
