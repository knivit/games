package com.tsoft.dune2.structure;

import static com.tsoft.dune2.structure.StructureType.*;

public class StructureFlag {

    public static int FLAG_STRUCTURE_SLAB_1x1          = 1 << STRUCTURE_SLAB_1x1;          /* 0x____01 */
    public static int FLAG_STRUCTURE_SLAB_2x2          = 1 << STRUCTURE_SLAB_2x2;          /* 0x____02 */
    public static int FLAG_STRUCTURE_PALACE            = 1 << STRUCTURE_PALACE;            /* 0x____04 */
    public static int FLAG_STRUCTURE_LIGHT_VEHICLE     = 1 << STRUCTURE_LIGHT_VEHICLE;     /* 0x____08 */
    public static int FLAG_STRUCTURE_HEAVY_VEHICLE     = 1 << STRUCTURE_HEAVY_VEHICLE;     /* 0x____10 */
    public static int FLAG_STRUCTURE_HIGH_TECH         = 1 << STRUCTURE_HIGH_TECH;         /* 0x____20 */
    public static int FLAG_STRUCTURE_HOUSE_OF_IX       = 1 << STRUCTURE_HOUSE_OF_IX;       /* 0x____40 */
    public static int FLAG_STRUCTURE_WOR_TROOPER       = 1 << STRUCTURE_WOR_TROOPER;       /* 0x____80 */
    public static int FLAG_STRUCTURE_CONSTRUCTION_YARD = 1 << STRUCTURE_CONSTRUCTION_YARD; /* 0x__01__ */
    public static int FLAG_STRUCTURE_WINDTRAP          = 1 << STRUCTURE_WINDTRAP;          /* 0x__02__ */
    public static int FLAG_STRUCTURE_BARRACKS          = 1 << STRUCTURE_BARRACKS;          /* 0x__04__ */
    public static int FLAG_STRUCTURE_STARPORT          = 1 << STRUCTURE_STARPORT;          /* 0x__08__ */
    public static int FLAG_STRUCTURE_REFINERY          = 1 << STRUCTURE_REFINERY;          /* 0x__10__ */
    public static int FLAG_STRUCTURE_REPAIR            = 1 << STRUCTURE_REPAIR;            /* 0x__20__ */
    public static int FLAG_STRUCTURE_WALL              = 1 << STRUCTURE_WALL;              /* 0x__40__ */
    public static int FLAG_STRUCTURE_TURRET            = 1 << STRUCTURE_TURRET;            /* 0x__80__ */
    public static int FLAG_STRUCTURE_ROCKET_TURRET     = 1 << STRUCTURE_ROCKET_TURRET;     /* 0x01____ */
    public static int FLAG_STRUCTURE_SILO              = 1 << STRUCTURE_SILO;              /* 0x02____ */
    public static int FLAG_STRUCTURE_OUTPOST           = 1 << STRUCTURE_OUTPOST;           /* 0x04____ */

    public static int FLAG_STRUCTURE_NONE              = 0;
    public static int FLAG_STRUCTURE_NEVER             = -1;   /*!< Special flag to mark that certain buildings can never be built on a Construction Yard. */
}
