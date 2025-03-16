package com.tsoft.dune2.gobject;

public class GObjectInfoFlags {

    public boolean hasShadow;                /* If true, the Unit has a shadow below it. */
    public boolean factory;                  /* Structure can build other Structures or Units. */
    public boolean notOnConcrete;            /* Structure cannot be build on concrete. */
    public boolean busyStateIsIncoming;      /* If true, the Structure has lights to indicate a Unit is incoming. This is then the BUSY state, where READY means it is processing the Unit. */
    public boolean blurTile;                 /* If true, this blurs the tile the Unit is on. */
    public boolean hasTurret;                /* If true, the Unit has a turret seperate from his base unit. */
    public boolean conquerable;              /* Structure can be invaded and subsequently conquered when hitpoints are low. */
    public boolean canBePickedUp;            /* If true, it can be picked up (by a CarryAll). */
    public boolean noMessageOnDeath;         /* Do not show a message (or sound) when this Structure / Unit is destroyed. */
    public boolean tabSelectable;            /* Is Structure / Unit selectable by pressing tab (which cycles through all Units and Structures). */
    public boolean scriptNoSlowdown;         /* If Structure / Unit is outside viewport, do not slow down scripting. */
    public boolean targetAir;                /* Can target (and shoot) air units. */
    public boolean priority;                 /* If not set, it is never seen as any priority for Units (for auto-attack). */
}
