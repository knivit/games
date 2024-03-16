package com.tsoft.dune2.structure;

import com.tsoft.dune2.gobject.GObject;

/**
 * A Structure as stored in the memory.
 */
public class Structure {

    public GObject o = new GObject();       /* Common to Unit and Structures. */
    public int creatorHouseID;              /* The Index of the House who created this Structure. Required in case of take-overs. */
    public int rotationSpriteDiff;          /* Which sprite to show for the current rotation of Turrets etc. */
    public int objectType;                  /* Type of Unit/Structure we are building. */
    public int upgradeLevel;                /* The current level of upgrade of the Structure. */
    public int upgradeTimeLeft;             /* Time left before upgrade is complete, or 0 if no upgrade available. */
    public int countDown;                   /* General countdown for various of functions. */
    public int buildCostRemainder;          /* The remainder of the buildCost for next tick. */
    public int state;                       /* The state of the structure. @see StructureState. */
    public int hitpointsMax;                /* Max amount of hitpoints. */
}
