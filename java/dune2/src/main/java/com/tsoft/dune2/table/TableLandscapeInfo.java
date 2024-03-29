package com.tsoft.dune2.table;

import com.tsoft.dune2.map.LandscapeInfo;

public class TableLandscapeInfo {

    public static final LandscapeInfo[] g_table_landscapeInfo = new LandscapeInfo[] {
        new LandscapeInfo( /* 0 / LST_NORMAL_SAND */
            /* movementSpeed        */ new int[] { 112, 112, 112, 160, 255, 192 },
            /* letUnitWobble        */ false,
            /* isValidForStructure  */ false,
            /* isSand               */ true,
            /* isValidForStructure2 */ false,
            /* canBecomeSpice       */ true,
            /* craterType           */ 1,
            /* radarColour          */ 88,
            /* spriteID             */ 37
        ),

        new LandscapeInfo( /* 1 / LST_PARTIAL_ROCK */
            /* movementSpeed        */ new int[] { 160, 112, 112, 64, 255, 0 },
            /* letUnitWobble        */ true,
            /* isValidForStructure  */ false,
            /* isSand               */ false,
            /* isValidForStructure2 */ false,
            /* canBecomeSpice       */ false,
            /* craterType           */ 1,
            /* radarColour          */ 28,
            /* spriteID             */ 39
        ),

        new LandscapeInfo( /* 2 / LST_ENTIRELY_DUNE */
            /* movementSpeed        */ new int[] { 112, 160, 160, 160, 255, 192 },
            /* letUnitWobble        */ false,
            /* isValidForStructure  */ false,
            /* isSand               */ true,
            /* isValidForStructure2 */ false,
            /* canBecomeSpice       */ true,
            /* craterType           */ 1,
            /* radarColour          */ 92,
            /* spriteID             */ 41
        ),

        new LandscapeInfo( /* 3 / LST_PARTIAL_DUNE */
            /* movementSpeed        */ new int[] { 112, 160, 160, 160, 255, 192 },
            /* letUnitWobble        */ false,
            /* isValidForStructure  */ false,
            /* isSand               */ true,
            /* isValidForStructure2 */ false,
            /* canBecomeSpice       */ true,
            /* craterType           */ 1,
            /* radarColour          */ 89,
            /* spriteID             */ 43
        ),

        new LandscapeInfo( /* 4 / LST_ENTIRELY_ROCK */
            /* movementSpeed        */ new int[] { 112, 160, 160, 112, 255, 0 },
            /* letUnitWobble        */ true,
            /* isValidForStructure  */ true,
            /* isSand               */ false,
            /* isValidForStructure2 */ true,
            /* canBecomeSpice       */ false,
            /* craterType           */ 2,
            /* radarColour          */ 30,
            /* spriteID             */ 45
        ),

        new LandscapeInfo( /* 5 / LST_MOSTLY_ROCK */
            /* movementSpeed        */ new int[] { 160, 160, 160, 160, 255, 0 },
            /* letUnitWobble        */ true,
            /* isValidForStructure  */ true,
            /* isSand               */ false,
            /* isValidForStructure2 */ true,
            /* canBecomeSpice       */ false,
            /* craterType           */ 2,
            /* radarColour          */ 29,
            /* spriteID             */ 47
        ),

        new LandscapeInfo( /* 6 / LST_ENTIRELY_MOUNTAIN */
            /* movementSpeed        */ new int[] { 64, 0, 0, 0, 255, 0 },
            /* letUnitWobble        */ true,
            /* isValidForStructure  */ false,
            /* isSand               */ false,
            /* isValidForStructure2 */ false,
            /* canBecomeSpice       */ false,
            /* craterType           */ 0,
            /* radarColour          */ 12,
            /* spriteID             */ 49
        ),

        new LandscapeInfo( /* 7 / LST_PARTIAL_MOUNTAIN */
            /* movementSpeed        */ new int[] { 64, 0, 0, 0, 255, 0 },
            /* letUnitWobble        */ true,
            /* isValidForStructure  */ false,
            /* isSand               */ false,
            /* isValidForStructure2 */ false,
            /* canBecomeSpice       */ false,
            /* craterType           */ 0,
            /* radarColour          */ 133,
            /* spriteID             */ 51
        ),

        new LandscapeInfo( /* 8 / LST_SPICE */
            /* movementSpeed        */ new int[] { 112, 160, 160, 160, 255, 192 },
            /* letUnitWobble        */ false,
            /* isValidForStructure  */ false,
            /* isSand               */ true,
            /* isValidForStructure2 */ false,
            /* canBecomeSpice       */ true,
            /* craterType           */ 1,
            /* radarColour          */ 215, /* was 88, but is changed on startup */
            /* spriteID             */ 53   /* was 37, but is changed on startup */
        ),

        new LandscapeInfo( /* 9 / LST_THICK_SPICE */
            /* movementSpeed        */ new int[] { 112, 160, 160, 160, 255, 192 },
            /* letUnitWobble        */ true,
            /* isValidForStructure  */ false,
            /* isSand               */ true,
            /* isValidForStructure2 */ false,
            /* canBecomeSpice       */ true,
            /* craterType           */ 1,
            /* radarColour          */ 216, /* was 88, but is changed on startup */
            /* spriteID             */ 53   /* was 37, but is changed on startup */
        ),

        new LandscapeInfo( /* 10 / LST_CONCRETE_SLAB */
            /* movementSpeed        */ new int[] { 255, 255, 255, 255, 255, 0 },
            /* letUnitWobble        */ false,
            /* isValidForStructure  */ true,
            /* isSand               */ false,
            /* isValidForStructure2 */ false,
            /* canBecomeSpice       */ false,
            /* craterType           */ 2,
            /* radarColour          */ 133,
            /* spriteID             */ 51
        ),

        new LandscapeInfo( /* 11 / LST_WALL */
            /* movementSpeed        */ new int[] { 0, 0, 0, 0, 255, 0 },
            /* letUnitWobble        */ false,
            /* isValidForStructure  */ false,
            /* isSand               */ false,
            /* isValidForStructure2 */ false,
            /* canBecomeSpice       */ false,
            /* craterType           */ 0,
            /* radarColour          */ 65535,
            /* spriteID             */ 31
        ),

        new LandscapeInfo( /* 12 / LST_STRUCTURE */
            /* movementSpeed        */ new int[] { 0, 0, 0, 0, 255, 0 },
            /* letUnitWobble        */ false,
            /* isValidForStructure  */ false,
            /* isSand               */ false,
            /* isValidForStructure2 */ false,
            /* canBecomeSpice       */ false,
            /* craterType           */ 0,
            /* radarColour          */ 65535,
            /* spriteID             */ 31
        ),

        new LandscapeInfo( /* 13 / LST_DESTROYED_WALL */
            /* movementSpeed        */ new int[] { 160, 160, 160, 160, 255, 0 },
            /* letUnitWobble        */ true,
            /* isValidForStructure  */ true,
            /* isSand               */ false,
            /* isValidForStructure2 */ true,
            /* canBecomeSpice       */ false,
            /* craterType           */ 2,
            /* radarColour          */ 29,
            /* spriteID             */ 47
        ),

        new LandscapeInfo( /* 14 / LST_BLOOM_FIELD */
            /* movementSpeed        */ new int[] { 112, 112, 112, 160, 255, 192 },
            /* letUnitWobble        */ false,
            /* isValidForStructure  */ false,
            /* isSand               */ true,
            /* isValidForStructure2 */ false,
            /* canBecomeSpice       */ true,
            /* craterType           */ 1,
            /* radarColour          */ 50,
            /* spriteID             */ 57
        )
    };
}
