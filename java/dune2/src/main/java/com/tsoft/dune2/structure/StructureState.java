package com.tsoft.dune2.structure;

/** States a structure can be in */
public class StructureState {

    public static int STRUCTURE_STATE_DETECT    = -2;    /* Used when setting state, meaning to detect which state it has by looking at other properties. */
    public static int STRUCTURE_STATE_JUSTBUILT = -1;    /* This shows you the building animation etc. */
    public static int STRUCTURE_STATE_IDLE      = 0;     /* Structure is doing nothing. */
    public static int STRUCTURE_STATE_BUSY      = 1;     /* Structure is busy (harvester in refinery, unit in repair, .. */
    public static int STRUCTURE_STATE_READY     = 2;     /* Structure is ready and unit will be deployed soon. */
}
