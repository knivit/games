package com.tsoft.dune2.unit;

import com.tsoft.dune2.gobject.GObject;
import com.tsoft.dune2.tile.Tile32;

/**
 * A Unit as stored in the memory.
 */
public class Unit {

    public GObject o = new GObject();                /* Common to Unit and Structures. */
    public Tile32 currentDestination = new Tile32(); /* Where the Unit is currently going to. */
    public int originEncoded;                        /* Encoded index, indicating the origin. */
    public int actionID;                            /* Current action. */
    public int nextActionID;                        /* Next action. */
    public int fireDelay;                            /* Delay between firing. In Dune2 this is an uint8. */
    public int distanceToDestination;                /* How much distance between where we are now and where currentDestination is. */
    public int targetAttack;                         /* Target to attack (encoded index). */
    public int targetMove;                           /* Target to move to (encoded index). */
    public int amount;                              /* Meaning depends on type:
                                                      * - Sandworm : units to eat before disappearing.
                                                      * - Harvester : harvested spice.
                                                      */
    public int  deviated;                             /* Strength of deviation. Zero if unit is not deviated. */
    public int  deviatedHouse;                        /* Which house it is deviated to. Only valid if 'deviated' is non-zero. */
    public Tile32 targetLast = new Tile32();          /* The last position of the Unit. Carry-alls will return the Unit here. */
    public Tile32 targetPreLast = new Tile32();       /* The position before the last position of the Unit. */
    public Dir24[]  orientation = new Dir24[2];       /* Orientation of the unit. [0] = base, [1] = top (turret, etc). */
    public int speedPerTick;                         /* Every tick this amount is added; if over 255 Unit is moved. */
    public int speedRemainder;                       /* Remainder of speedPerTick. */
    public int speed;                                /* The amount to move when speedPerTick goes over 255. */
    public int movingSpeed;                          /* The speed of moving as last set. */
    public int wobbleIndex;                          /* At which wobble index the Unit currently is. */
    public int spriteOffset;                         /* Offset of the current sprite for Unit. */
    public int blinkCounter;                         /* If non-zero, it indicates how many more ticks this unit is blinking. */
    public int team;                                 /* If non-zero, unit is part of team. Value 1 means team 0, etc. */
    public int timer;                                 /*!Timer used in animation, to count down when to do the next step. */
    public int[]  route = new int[14];                /*!The current route the Unit is following. */
}
