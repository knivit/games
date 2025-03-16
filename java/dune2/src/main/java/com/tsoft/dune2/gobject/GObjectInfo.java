package com.tsoft.dune2.gobject;

/**
 * Data common to StructureInfo and UnitInfo.
 */
public class GObjectInfo {

    public int stringID_abbrev;                /* StringID of abbreviated name of Structure / Unit. */
	public String name;                        /* Pointer to name of Structure / Unit. */
    public int stringID_full;                  /* StringID of full name of Structure / Unit. */
	public String wsa;                         /* Pointer to name of .wsa file. */
    public GObjectInfoFlags flags = new GObjectInfoFlags(); /*!< General flags of the ObjectInfo. */
    public int spawnChance;                    /* Chance of spawning a Unit (if Structure: on destroying of Structure). */
    public int hitpoints;                      /* Default hitpoints for this Structure / Unit. */
    public int fogUncoverRadius;               /* Radius of fog to uncover. */
    public int spriteID;                       /* SpriteID of Structure / Unit. */
    public int buildCredits;                   /* How much credits it cost to build this Structure / Unit. Upgrading is 50% of this value. */
    public int buildTime;                      /* Time required to build this Structure / Unit. */
    public int availableCampaign;              /* In which campaign (starting at 1) this Structure / Unit is available. */
    public long structuresRequired;            /* Which structures are required before this Structure / Unit is available. */
    public int sortPriority;                   /* ?? */
    public int upgradeLevelRequired;           /* Which level of upgrade the Structure / Unit has to have before this is avialable. */
    public int[] actionsPlayer = new int[4];   /* Actions for player Structure / Unit. */
    public int available;                      /* If this Structure / Unit is ordered (Starport) / available (Rest). 1+=yes (volume), 0=no, -1=upgrade-first. */
    public int hintStringID;                   /* StringID of the hint shown for this Structure / Unit. */
    public int priorityBuild;                  /* The amount of priority a Structure / Unit has when a new Structure / Unit has to be build. */
    public int priorityTarget;                 /* The amount of priority a Structure / Unit has when being targetted. */
    public int  availableHouse;                /* To which house this Structure / Unit is available. */
}
