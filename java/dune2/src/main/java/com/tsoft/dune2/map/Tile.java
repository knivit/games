package com.tsoft.dune2.map;

/**
 * A Tile as stored in the memory in the map.
 */
public class Tile {

    /* 0000 01FF */ public int groundTileID;              /* The "Icon" which is drawn on this Tile. */
    /* 0000 FE00 */ public int overlayTileID;             /* The Overlay which is drawn over this Tile. */
    /* 0007 0000 */ public int houseID;                   /* Which House owns this Tile. */
    /* 0008 0000 */ public boolean isUnveiled;            /* There is no fog on the Tile. */
    /* 0010 0000 */ public boolean hasUnit;               /* There is a Unit on the Tile. */
    /* 0020 0000 */ public boolean hasStructure;          /* There is a Structure on the Tile. */
    /* 0040 0000 */ public boolean hasAnimation;          /* There is animation going on the Tile. */
    /* 0080 0000 */ public boolean hasExplosion;          /* There is an explosion on the Tile. */
    /* FF00 0000 */ public int index;                     /* Index of the Structure / Unit (index 1 is Structure/Unit 0, etc). */
}
