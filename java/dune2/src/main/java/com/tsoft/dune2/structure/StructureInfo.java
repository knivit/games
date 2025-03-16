package com.tsoft.dune2.structure;

import com.tsoft.dune2.gobject.GObjectInfo;

/**
 * Static information per Structure type.
 */
public class StructureInfo {

    public GObjectInfo o = new GObjectInfo();       /* Common to UnitInfo and StructureInfo. */
    public long enterFilter;                        /* Bitfield determining which unit is allowed to enter the structure. If bit n is set, then units of type n may enter */
    public int creditsStorage;                      /* How many credits this Structure can store. */
    public int powerUsage;                          /* How much power this Structure uses (positive value) or produces (negative value). */
    public int layout;                              /* Layout type of Structure. */
    public int iconGroup;                           /* In which IconGroup the sprites of the Structure belongs. */
    public int[] animationIndex = new int[3];       /* The index inside g_table_animation_structure for the Animation of the Structure. */
    public int[] buildableUnits = new int[8];       /* Which units this structure can produce. */
    public int[] upgradeCampaign = new int[3];      /* Minimum campaign for upgrades. */
}
