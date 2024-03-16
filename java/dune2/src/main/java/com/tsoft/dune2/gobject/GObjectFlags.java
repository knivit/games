package com.tsoft.dune2.gobject;

/**
 * Flags for GObject structure
 */
public class GObjectFlags {

    public boolean used;                                  /*!< The Object is in use (no longer free in the pool). */
    public boolean allocated;                             /*!< The Object is allocated (created, and ready to be put on the map). */
    public boolean isNotOnMap;                            /*!< The Object is not on the map (under construction, in refinery, etc). */
    public boolean isSmoking;                             /*!< The Object has a smoke cloud coming out of it. */
    public boolean fireTwiceFlip;                         /*!< Used for Unit fire twice, to keep track if it is the second shot. */
    public boolean animationFlip;                         /*!< Used for Unit (bullet / missile) animation, to differ between two sprite groups. */
    public boolean bulletIsBig;                           /*!< If true, the Unit (bullet / sonic wave) is twice as big (visual only). */
    public boolean isWobbling;                            /*!< If true, the Unit will be wobbling during movement. */
    public boolean inTransport;                           /*!< The Unit is in transport (spaceport, reinforcement, harvester). */
    public boolean byScenario;                            /*!< The Unit is created by the scenario. */
    public boolean degrades;                              /*!< Structure degrades. Unit ?? */
    public boolean isHighlighted;                         /*!< The Object is currently highlighted. */
    public boolean isDirty;                               /*!< If true, the Unit will be redrawn next update. */
    public boolean repairing;                             /*!< Structure is being repaired. */
    public boolean onHold;                                /*!< Structure is on hold. */
    public boolean notused_4_8000;
    public boolean isUnit;                                /*!< If true, this is an Unit, otherwise a Structure. */
    public boolean upgrading;                             /*!< Structure is being upgraded. */
    public boolean notused_6_0004;
    public boolean notused_6_0100;

    public void reset() {
        used = false;
        allocated = false;
        isNotOnMap = false;
        isSmoking = false;
        fireTwiceFlip = false;
        animationFlip = false;
        bulletIsBig = false;
        isWobbling = false;
        inTransport = false;
        byScenario = false;
        degrades = false;
        isHighlighted = false;
        isDirty = false;
        repairing = false;
        onHold = false;
        notused_4_8000 = false;
        isUnit = false;
        upgrading = false;
        notused_6_0004 = false;
        notused_6_0100 = false;
    }
}
