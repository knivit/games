package com.tsoft.dune2.table;

import com.tsoft.dune2.gobject.GObjectInfo;
import com.tsoft.dune2.gobject.GObjectInfoFlags;
import com.tsoft.dune2.structure.StructureInfo;
import com.tsoft.dune2.structure.XYSize;
import com.tsoft.dune2.tile.Tile32;

import static com.tsoft.dune2.house.HouseFlag.*;
import static com.tsoft.dune2.sprites.IconMapEntries.*;
import static com.tsoft.dune2.strings.Strings.*;
import static com.tsoft.dune2.structure.StructureFlag.*;
import static com.tsoft.dune2.structure.StructureLayout.*;
import static com.tsoft.dune2.unit.ActionType.*;
import static com.tsoft.dune2.unit.UnitFlag.*;
import static com.tsoft.dune2.unit.UnitType.*;

public class TableStructureInfo {

    public static StructureInfo[] g_table_structureInfo = new StructureInfo[] {
        SI_0(), SI_1(), SI_2(), SI_3(), SI_4(), SI_5(), SI_6(), SI_7(),
        SI_8(), SI_9(), SI_10(), SI_11(), SI_12(), SI_13(), SI_14(), SI_15(),
        SI_16(), SI_17(), SI_18()
    };

    /** Array with position offset per tile in a structure layout. */
    public static int[][] g_table_structure_layoutTiles = new int[][] {
        {0,    0,    0,    0,     0,     0,     0,     0,     0}, /* STRUCTURE_LAYOUT_1x1 */
        {0,    1,    0,    0,     0,     0,     0,     0,     0}, /* STRUCTURE_LAYOUT_2x1 */
        {0, 64+0,    0,    0,     0,     0,     0,     0,     0}, /* STRUCTURE_LAYOUT_1x2 */
        {0,    1, 64+0, 64+1,     0,     0,     0,     0,     0}, /* STRUCTURE_LAYOUT_2x2 */
        {0,    1, 64+0, 64+1, 128+0, 128+1,     0,     0,     0}, /* STRUCTURE_LAYOUT_2x3 */
        {0,    1,    2, 64+0,  64+1,  64+2,     0,     0,     0}, /* STRUCTURE_LAYOUT_3x2 */
        {0,    1,    2, 64+0,  64+1,  64+2, 128+0, 128+1, 128+2}, /* STRUCTURE_LAYOUT_3x3 */
    };

    /** Array with position offset of edge tiles in a structure layout. */
    public static int[][] g_table_structure_layoutEdgeTiles = new int[][] {
        {0, 0,    0,     0,     0,     0,     0, 0}, /* STRUCTURE_LAYOUT_1x1 */
        {0, 1,    1,     1,     1,     0,     0, 0}, /* STRUCTURE_LAYOUT_2x1 */
        {0, 0,    0,  64+0,  64+0,  64+0,     0, 0}, /* STRUCTURE_LAYOUT_1x2 */
        {0, 1,    1,  64+1,  64+1,  64+0,  64+0, 0}, /* STRUCTURE_LAYOUT_2x2 */
        {0, 1, 64+1, 128+1, 128+1, 128+0,  64+0, 0}, /* STRUCTURE_LAYOUT_2x3 */
        {1, 2,    2,  64+2,  64+1,  64+0,     0, 0}, /* STRUCTURE_LAYOUT_3x2 */
        {1, 2, 64+2, 128+2, 128+1, 128+0,  64+0, 0}, /* STRUCTURE_LAYOUT_3x3 */
    };

    /** Array with number of tiles in a layout. */
    public static int[] g_table_structure_layoutTileCount = new int[] {
        1, /* STRUCTURE_LAYOUT_1x1 */
        2, /* STRUCTURE_LAYOUT_2x1 */
        2, /* STRUCTURE_LAYOUT_1x2 */
        4, /* STRUCTURE_LAYOUT_2x2 */
        6, /* STRUCTURE_LAYOUT_2x3 */
        6, /* STRUCTURE_LAYOUT_3x2 */
        9, /* STRUCTURE_LAYOUT_3x3 */
    };

    /** Array with TileDiff of a layout. */
    public static Tile32[] g_table_structure_layoutTileDiff = new Tile32[] {
        new Tile32(0x0080, 0x0080), /* STRUCTURE_LAYOUT_1x1 */
        new Tile32(0x0100, 0x0080), /* STRUCTURE_LAYOUT_2x1 */
        new Tile32(0x0080, 0x0100), /* STRUCTURE_LAYOUT_1x2 */
        new Tile32(0x0100, 0x0100), /* STRUCTURE_LAYOUT_2x2 */
        new Tile32(0x0100, 0x0180), /* STRUCTURE_LAYOUT_2x3 */
        new Tile32(0x0280, 0x0100), /* STRUCTURE_LAYOUT_3x2 */
        new Tile32(0x0180, 0x0180), /* STRUCTURE_LAYOUT_3x3 */
    };

    /** Array with size of a layout. */
    public static XYSize[] g_table_structure_layoutSize = new XYSize[] {
        new XYSize(1, 1), /* STRUCTURE_LAYOUT_1x1 */
        new XYSize(2, 1), /* STRUCTURE_LAYOUT_2x1 */
        new XYSize(1, 2), /* STRUCTURE_LAYOUT_1x2 */
        new XYSize(2, 2), /* STRUCTURE_LAYOUT_2x2 */
        new XYSize(2, 3), /* STRUCTURE_LAYOUT_2x3 */
        new XYSize(3, 2), /* STRUCTURE_LAYOUT_3x2 */
        new XYSize(3, 3), /* STRUCTURE_LAYOUT_3x3 */
    };

    /** Array with position offset per tile around a structure layout. */
    public static int[][] g_table_structure_layoutTilesAround = new int[][] {
        {-64, -64+1,     1,  64+1,  64+0,  64-1,    -1, -64-1,     0,     0,     0,     0,     0,     0,  0,     0}, /* STRUCTURE_LAYOUT_1x1 */
        {-64, -64+1, -64+2,     2,  64+2,  64+1,  64+0,  64-1,    -1, -64-1,     0,     0,     0,     0,  0,     0}, /* STRUCTURE_LAYOUT_2x1 */
        {-64, -64+1,     1,  64+1, 128+1, 128+0, 128-1,  64-1,    -1, -64-1,     0,     0,     0,     0,  0,     0}, /* STRUCTURE_LAYOUT_1x2 */
        {-64, -64+1, -64+2,     2,  64+2, 128+2, 128+1, 128+0, 128-1,  64-1,    -1, -64-1,     0,     0,  0,     0}, /* STRUCTURE_LAYOUT_2x2 */
        {-64, -64+1, -64+2,     2,  64+2, 128+2, 192+2, 192+1, 192+0, 192-1, 128-1,  64-1,    -1, -64-1,  0,     0}, /* STRUCTURE_LAYOUT_2x3 */
        {-64, -64+1, -64+2, -64+3,     3,  64+3, 128+3, 128+2, 128+1, 128+0, 128-1,  64-1,    -1, -64-1,  0,     0}, /* STRUCTURE_LAYOUT_3x2 */
        {-64, -64+1, -64+2, -64+3,     3,  64+3, 128+3, 192+3, 192+2, 192+1, 192+0, 192-1, 128-1,  64-1, -1, -64-1}, /* STRUCTURE_LAYOUT_3x3 */
    };

    private static StructureInfo SI_0() {
        StructureInfo si = new StructureInfo();
        si.o                            = new GObjectInfo();
        si.o.stringID_abbrev            = STR_CONCRETE;
        si.o.name                       = "Concrete";
        si.o.stringID_full              = STR_SMALL_CONCRETE_SLAB;
        si.o.wsa                        = "slab.wsa";
        si.o.flags                      = new GObjectInfoFlags();
        si.o.flags.hasShadow            = false;
        si.o.flags.factory              = false;
        si.o.flags.notOnConcrete        = true;
        si.o.flags.busyStateIsIncoming  = false;
        si.o.flags.blurTile             = false;
        si.o.flags.hasTurret            = false;
        si.o.flags.conquerable          = false;
        si.o.flags.canBePickedUp        = false;
        si.o.flags.noMessageOnDeath     = false;
        si.o.flags.tabSelectable        = false;
        si.o.flags.scriptNoSlowdown     = false;
        si.o.flags.targetAir            = false;
        si.o.flags.priority             = false;
        si.o.spawnChance                = 0;
        si.o.hitpoints                  = 20;
        si.o.fogUncoverRadius           = 1;
        si.o.spriteID                   = 65;
        si.o.buildCredits               = 5;
        si.o.buildTime                  = 16;
        si.o.availableCampaign          = 1;
        si.o.structuresRequired         = FLAG_STRUCTURE_NONE;
        si.o.sortPriority               = 2;
        si.o.upgradeLevelRequired       = 0;
        si.o.actionsPlayer              = new int[] { ACTION_ATTACK, ACTION_ATTACK, ACTION_ATTACK, ACTION_ATTACK };
        si.o.available                  = 0;
        si.o.hintStringID               = STR_CONCRETE_USE_CONCRETE_TO_MAKE_A_STURDY_FOUNDATION_FOR_YOUR_STRUCTURES;
        si.o.priorityBuild              = 0;
        si.o.priorityTarget             = 5;
        si.o.availableHouse             = FLAG_HOUSE_ALL;
        si.enterFilter                  = FLAG_UNIT_NONE;
        si.creditsStorage               = 0;
        si.powerUsage                   = 0;
        si.layout                       = STRUCTURE_LAYOUT_1x1;
        si.iconGroup                    = ICM_ICONGROUP_CONCRETE_SLAB;
        si.animationIndex               = new int[] { 2, 2, 2 };
        si.buildableUnits               = new int[] { UNIT_INVALID, UNIT_INVALID, UNIT_INVALID, UNIT_INVALID, UNIT_INVALID, UNIT_INVALID, UNIT_INVALID, UNIT_INVALID };
        si.upgradeCampaign              = new int[] { 0, 0, 0 };

        return si;
    }

    private static StructureInfo SI_1() {
        StructureInfo si = new StructureInfo();
        si.o                            = new GObjectInfo();
        si.o.stringID_abbrev            = STR_CONCRETE_4;
        si.o.name                       = "Concrete4";
        si.o.stringID_full              = STR_LARGE_CONCRETE_SLAB;
        si.o.wsa                        = "4slab.wsa";
        si.o.flags                      = new GObjectInfoFlags();
        si.o.flags.hasShadow            = false;
        si.o.flags.factory              = false;
        si.o.flags.notOnConcrete        = true;
        si.o.flags.busyStateIsIncoming  = false;
        si.o.flags.blurTile             = false;
        si.o.flags.hasTurret            = false;
        si.o.flags.conquerable          = false;
        si.o.flags.canBePickedUp        = false;
        si.o.flags.noMessageOnDeath     = false;
        si.o.flags.tabSelectable        = false;
        si.o.flags.scriptNoSlowdown     = false;
        si.o.flags.targetAir            = false;
        si.o.flags.priority             = false;
        si.o.spawnChance                = 0;
        si.o.hitpoints                  = 20;
        si.o.fogUncoverRadius           = 1;
        si.o.spriteID                   = 83;
        si.o.buildCredits               = 20;
        si.o.buildTime                  = 16;
        si.o.availableCampaign          = 4;
        si.o.structuresRequired         = FLAG_STRUCTURE_NONE;
        si.o.sortPriority               = 4;
        si.o.upgradeLevelRequired       = 1;
        si.o.actionsPlayer              = new int[] { ACTION_ATTACK, ACTION_ATTACK, ACTION_ATTACK, ACTION_ATTACK };
        si.o.available                  = 0;
        si.o.hintStringID               = STR_CONCRETE_USE_CONCRETE_TO_MAKE_A_STURDY_FOUNDATION_FOR_YOUR_STRUCTURES;
        si.o.priorityBuild              = 0;
        si.o.priorityTarget             = 10;
        si.o.availableHouse             = FLAG_HOUSE_ALL;
        si.enterFilter                  = FLAG_UNIT_NONE;
        si.creditsStorage               = 0;
        si.powerUsage                   = 0;
        si.layout                       = STRUCTURE_LAYOUT_2x2;
        si.iconGroup                    = ICM_ICONGROUP_CONCRETE_SLAB;
        si.animationIndex               = new int[] { 2, 2, 2 };
        si.buildableUnits               = new int[] { UNIT_INVALID, UNIT_INVALID, UNIT_INVALID, UNIT_INVALID, UNIT_INVALID, UNIT_INVALID, UNIT_INVALID, UNIT_INVALID };
        si.upgradeCampaign              = new int[] { 0, 0, 0 };

        return si;
    }

    private static StructureInfo SI_2() {
        StructureInfo si = new StructureInfo();
        si.o                            = new GObjectInfo();
        si.o.stringID_abbrev            = STR_PALACE;
        si.o.name                       = "Palace";
        si.o.stringID_full              = STR_HOUSE_PALACE;
        si.o.wsa                        = "palace.wsa";
        si.o.flags                      = new GObjectInfoFlags();
        si.o.flags.hasShadow            = false;
        si.o.flags.factory              = false;
        si.o.flags.notOnConcrete        = false;
        si.o.flags.busyStateIsIncoming  = false;
        si.o.flags.blurTile             = false;
        si.o.flags.hasTurret            = false;
        si.o.flags.conquerable          = false;
        si.o.flags.canBePickedUp        = false;
        si.o.flags.noMessageOnDeath     = false;
        si.o.flags.tabSelectable        = false;
        si.o.flags.scriptNoSlowdown     = false;
        si.o.flags.targetAir            = false;
        si.o.flags.priority             = false;
        si.o.spawnChance                = 128;
        si.o.hitpoints                  = 1000;
        si.o.fogUncoverRadius           = 5;
        si.o.spriteID                   = 66;
        si.o.buildCredits               = 999;
        si.o.buildTime                  = 130;
        si.o.availableCampaign          = 8;
        si.o.structuresRequired         = FLAG_STRUCTURE_STARPORT;
        si.o.sortPriority               = 5;
        si.o.upgradeLevelRequired       = 0;
        si.o.actionsPlayer              = new int[] { ACTION_ATTACK, ACTION_ATTACK, ACTION_ATTACK, ACTION_ATTACK };
        si.o.available                  = 0;
        si.o.hintStringID               = STR_PALACE_THIS_IS_YOUR_PALACE;
        si.o.priorityBuild              = 0;
        si.o.priorityTarget             = 400;
        si.o.availableHouse             = FLAG_HOUSE_ALL;
        si.enterFilter                  = FLAG_UNIT_NONE;
        si.creditsStorage               = 0;
        si.powerUsage                   = 80;
        si.layout                       = STRUCTURE_LAYOUT_3x3;
        si.iconGroup                    = ICM_ICONGROUP_HOUSE_PALACE;
        si.animationIndex               = new int[] { 4, 4, 4 };
        si.buildableUnits               = new int[] { UNIT_INVALID, UNIT_INVALID, UNIT_INVALID, UNIT_INVALID, UNIT_INVALID, UNIT_INVALID, UNIT_INVALID, UNIT_INVALID };
        si.upgradeCampaign              = new int[] { 0, 0, 0 };

        return si;
    }

    private static StructureInfo SI_3() {
        StructureInfo si = new StructureInfo();
        si.o                            = new GObjectInfo();
        si.o.stringID_abbrev            = STR_LIGHT_FCTRY;
        si.o.name                       = "Light Fctry";
        si.o.stringID_full              = STR_LIGHT_VEHICLE_FACTORY;
        si.o.wsa                        = "liteftry.wsa";
        si.o.flags                      = new GObjectInfoFlags();
        si.o.flags.hasShadow            = false;
        si.o.flags.factory              = true;
        si.o.flags.notOnConcrete        = false;
        si.o.flags.busyStateIsIncoming  = false;
        si.o.flags.blurTile             = false;
        si.o.flags.hasTurret            = false;
        si.o.flags.conquerable          = true;
        si.o.flags.canBePickedUp        = false;
        si.o.flags.noMessageOnDeath     = false;
        si.o.flags.tabSelectable        = false;
        si.o.flags.scriptNoSlowdown     = false;
        si.o.flags.targetAir            = false;
        si.o.flags.priority             = false;
        si.o.spawnChance                = 64;
        si.o.hitpoints                  = 350;
        si.o.fogUncoverRadius           = 3;
        si.o.spriteID                   = 67;
        si.o.buildCredits               = 400;
        si.o.buildTime                  = 96;
        si.o.availableCampaign          = 3;
        si.o.structuresRequired         = FLAG_STRUCTURE_REFINERY | FLAG_STRUCTURE_WINDTRAP;
        si.o.sortPriority               = 14;
        si.o.upgradeLevelRequired       = 0;
        si.o.actionsPlayer              = new int[] { ACTION_ATTACK, ACTION_ATTACK, ACTION_ATTACK, ACTION_ATTACK };
        si.o.available                  = 0;
        si.o.hintStringID               = STR_LIGHT_FACTORY_THE_LIGHT_FACTORY_PRODUCES_LIGHT_ATTACK_VEHICLES;
        si.o.priorityBuild              = 0;
        si.o.priorityTarget             = 200;
        si.o.availableHouse             = FLAG_HOUSE_ALL;
        si.enterFilter                  = FLAG_UNIT_NONE;
        si.creditsStorage               = 0;
        si.powerUsage                   = 20;
        si.layout                       = STRUCTURE_LAYOUT_2x2;
        si.iconGroup                    = ICM_ICONGROUP_LIGHT_VEHICLE_FACTORY;
        si.animationIndex               = new int[] { 14, 15, 16 };
        si.buildableUnits               = new int[] { UNIT_TRIKE, UNIT_QUAD, UNIT_INVALID, UNIT_INVALID, UNIT_INVALID, UNIT_INVALID, UNIT_INVALID, UNIT_INVALID };
        si.upgradeCampaign              = new int[] { 3, 0, 0 };

        return si;
    }

    private static StructureInfo SI_4() {
        StructureInfo si = new StructureInfo();
        si.o                            = new GObjectInfo();
        si.o.stringID_abbrev            = STR_HEAVY_FCTRY;
        si.o.name                       = "Heavy Fctry";
        si.o.stringID_full              = STR_HEAVY_VEHICLE_FACTORY;
        si.o.wsa                        = "hvyftry.wsa";
        si.o.flags                      = new GObjectInfoFlags();
        si.o.flags.hasShadow            = false;
        si.o.flags.factory              = true;
        si.o.flags.notOnConcrete        = false;
        si.o.flags.busyStateIsIncoming  = false;
        si.o.flags.blurTile             = false;
        si.o.flags.hasTurret            = false;
        si.o.flags.conquerable          = true;
        si.o.flags.canBePickedUp        = false;
        si.o.flags.noMessageOnDeath     = false;
        si.o.flags.tabSelectable        = false;
        si.o.flags.scriptNoSlowdown     = false;
        si.o.flags.targetAir            = false;
        si.o.flags.priority             = false;
        si.o.spawnChance                = 64;
        si.o.hitpoints                  = 200;
        si.o.fogUncoverRadius           = 3;
        si.o.spriteID                   = 68;
        si.o.buildCredits               = 600;
        si.o.buildTime                  = 144;
        si.o.availableCampaign          = 4;
        si.o.structuresRequired         = FLAG_STRUCTURE_OUTPOST | FLAG_STRUCTURE_WINDTRAP | FLAG_STRUCTURE_LIGHT_VEHICLE;
        si.o.sortPriority               = 28;
        si.o.upgradeLevelRequired       = 0;
        si.o.actionsPlayer              = new int[] { ACTION_ATTACK, ACTION_ATTACK, ACTION_ATTACK, ACTION_ATTACK };
        si.o.available                  = 0;
        si.o.hintStringID               = STR_HEAVY_FACTORY_THE_HEAVY_FACTORY_PRODUCES_TRACKED_VEHICLES;
        si.o.priorityBuild              = 0;
        si.o.priorityTarget             = 600;
        si.o.availableHouse             = FLAG_HOUSE_ALL;
        si.enterFilter                  = FLAG_UNIT_NONE;
        si.creditsStorage               = 0;
        si.powerUsage                   = 35;
        si.layout                       = STRUCTURE_LAYOUT_3x2;
        si.iconGroup                    = ICM_ICONGROUP_HEAVY_VEHICLE_FACTORY;
        si.animationIndex               = new int[] { 11, 12, 13 };
        si.buildableUnits               = new int[] { UNIT_SIEGE_TANK, UNIT_LAUNCHER, UNIT_HARVESTER, UNIT_TANK, UNIT_DEVASTATOR, UNIT_DEVIATOR, UNIT_MCV, UNIT_SONIC_TANK };
        si.upgradeCampaign              = new int[] { 4, 5, 6 };

        return si;
    }

    private static StructureInfo SI_5() {
        StructureInfo si = new StructureInfo();
        si.o                            = new GObjectInfo();
        si.o.stringID_abbrev            = STR_HITECH;
        si.o.name                       = "Hi-Tech";
        si.o.stringID_full              = STR_HITECH_FACTORY;
        si.o.wsa                        = "hitcftry.wsa";
        si.o.flags                      = new GObjectInfoFlags();
        si.o.flags.hasShadow            = false;
        si.o.flags.factory              = true;
        si.o.flags.notOnConcrete        = false;
        si.o.flags.busyStateIsIncoming  = false;
        si.o.flags.blurTile             = false;
        si.o.flags.hasTurret            = false;
        si.o.flags.conquerable          = true;
        si.o.flags.canBePickedUp        = false;
        si.o.flags.noMessageOnDeath     = false;
        si.o.flags.tabSelectable        = false;
        si.o.flags.scriptNoSlowdown     = false;
        si.o.flags.targetAir            = false;
        si.o.flags.priority             = false;
        si.o.spawnChance                = 64;
        si.o.hitpoints                  = 400;
        si.o.fogUncoverRadius           = 3;
        si.o.spriteID                   = 69;
        si.o.buildCredits               = 500;
        si.o.buildTime                  = 120;
        si.o.availableCampaign          = 5;
        si.o.structuresRequired         = FLAG_STRUCTURE_OUTPOST | FLAG_STRUCTURE_WINDTRAP | FLAG_STRUCTURE_LIGHT_VEHICLE;
        si.o.sortPriority               = 30;
        si.o.upgradeLevelRequired       = 0;
        si.o.actionsPlayer              = new int[] { ACTION_ATTACK, ACTION_ATTACK, ACTION_ATTACK, ACTION_ATTACK };
        si.o.available                  = 0;
        si.o.hintStringID               = STR_HITECH_FACTORY_THE_HITECH_FACTORY_PRODUCES_FLYING_VEHICLES;
        si.o.priorityBuild              = 0;
        si.o.priorityTarget             = 200;
        si.o.availableHouse             = FLAG_HOUSE_ALL;
        si.enterFilter                  = FLAG_UNIT_NONE;
        si.creditsStorage               = 0;
        si.powerUsage                   = 35;
        si.layout                       = STRUCTURE_LAYOUT_3x2;
        si.iconGroup                    = ICM_ICONGROUP_HI_TECH_FACTORY;
        si.animationIndex               = new int[] { 8, 9, 10 };
        si.buildableUnits               = new int[] { UNIT_CARRYALL, UNIT_ORNITHOPTER, UNIT_INVALID, UNIT_INVALID, UNIT_INVALID, UNIT_INVALID, UNIT_INVALID, UNIT_INVALID };
        si.upgradeCampaign              = new int[] { 7, 0, 0 };

        return si;
    }

    private static StructureInfo SI_6() {
        StructureInfo si = new StructureInfo();
        si.o                            = new GObjectInfo();
        si.o.stringID_abbrev            = STR_IX;
        si.o.name                       = "IX";
        si.o.stringID_full              = STR_HOUSE_OF_IX;
        si.o.wsa                        = "ix.wsa";
        si.o.flags                      = new GObjectInfoFlags();
        si.o.flags.hasShadow            = false;
        si.o.flags.factory              = false;
        si.o.flags.notOnConcrete        = false;
        si.o.flags.busyStateIsIncoming  = false;
        si.o.flags.blurTile             = false;
        si.o.flags.hasTurret            = false;
        si.o.flags.conquerable          = false;
        si.o.flags.canBePickedUp        = false;
        si.o.flags.noMessageOnDeath     = false;
        si.o.flags.tabSelectable        = false;
        si.o.flags.scriptNoSlowdown     = false;
        si.o.flags.targetAir            = false;
        si.o.flags.priority             = false;
        si.o.spawnChance                = 192;
        si.o.hitpoints                  = 400;
        si.o.fogUncoverRadius           = 3;
        si.o.spriteID                   = 70;
        si.o.buildCredits               = 500;
        si.o.buildTime                  = 120;
        si.o.availableCampaign          = 7;
        si.o.structuresRequired         = FLAG_STRUCTURE_REFINERY | FLAG_STRUCTURE_STARPORT | FLAG_STRUCTURE_WINDTRAP;
        si.o.sortPriority               = 34;
        si.o.upgradeLevelRequired       = 0;
        si.o.actionsPlayer              = new int[] { ACTION_ATTACK, ACTION_ATTACK, ACTION_ATTACK, ACTION_ATTACK };
        si.o.available                  = 0;
        si.o.hintStringID               = STR_HOUSE_IX_THE_IX_RESEARCH_FACILITY_ADVANCES_YOUR_HOUSES_TECHNOLOGY;
        si.o.priorityBuild              = 0;
        si.o.priorityTarget             = 100;
        si.o.availableHouse             = FLAG_HOUSE_ALL;
        si.enterFilter                  = FLAG_UNIT_NONE;
        si.creditsStorage               = 0;
        si.powerUsage                   = 40;
        si.layout                       = STRUCTURE_LAYOUT_2x2;
        si.iconGroup                    = ICM_ICONGROUP_IX_RESEARCH;
        si.animationIndex               = new int[] { 20, 20, 20 };
        si.buildableUnits               = new int[] { UNIT_INVALID, UNIT_INVALID, UNIT_INVALID, UNIT_INVALID, UNIT_INVALID, UNIT_INVALID, UNIT_INVALID, UNIT_INVALID };
        si.upgradeCampaign              = new int[] { 0, 0, 0 };

        return si;
    }

    private static StructureInfo SI_7() {
        StructureInfo si = new StructureInfo();
        si.o                            = new GObjectInfo();
        si.o.stringID_abbrev            = STR_WOR;
        si.o.name                       = "WOR";
        si.o.stringID_full              = STR_WOR_TROOPER_FACILITY;
        si.o.wsa                        = "wor.wsa";
        si.o.flags                      = new GObjectInfoFlags();
        si.o.flags.hasShadow            = false;
        si.o.flags.factory              = true;
        si.o.flags.notOnConcrete        = false;
        si.o.flags.busyStateIsIncoming  = false;
        si.o.flags.blurTile             = false;
        si.o.flags.hasTurret            = false;
        si.o.flags.conquerable          = false;
        si.o.flags.canBePickedUp        = false;
        si.o.flags.noMessageOnDeath     = false;
        si.o.flags.tabSelectable        = false;
        si.o.flags.scriptNoSlowdown     = false;
        si.o.flags.targetAir            = false;
        si.o.flags.priority             = false;
        si.o.spawnChance                = 128;
        si.o.hitpoints                  = 400;
        si.o.fogUncoverRadius           = 3;
        si.o.spriteID                   = 71;
        si.o.buildCredits               = 400;
        si.o.buildTime                  = 104;
        si.o.availableCampaign          = 5;
        si.o.structuresRequired         = FLAG_STRUCTURE_OUTPOST | FLAG_STRUCTURE_BARRACKS | FLAG_STRUCTURE_WINDTRAP;
        si.o.sortPriority               = 20;
        si.o.upgradeLevelRequired       = 0;
        si.o.actionsPlayer              = new int[] { ACTION_ATTACK, ACTION_ATTACK, ACTION_ATTACK, ACTION_ATTACK };
        si.o.available                  = 0;
        si.o.hintStringID               = STR_WOR_WOR_IS_USED_TO_TRAIN_YOUR_HEAVY_INFANTRY;
        si.o.priorityBuild              = 0;
        si.o.priorityTarget             = 175;
        si.o.availableHouse             = FLAG_HOUSE_MERCENARY | FLAG_HOUSE_SARDAUKAR | FLAG_HOUSE_FREMEN | FLAG_HOUSE_ORDOS | FLAG_HOUSE_HARKONNEN;
        si.enterFilter                  = FLAG_UNIT_NONE;
        si.creditsStorage               = 0;
        si.powerUsage                   = 20;
        si.layout                       = STRUCTURE_LAYOUT_2x2;
        si.iconGroup                    = ICM_ICONGROUP_WOR_TROOPER_FACILITY;
        si.animationIndex               = new int[] { 21, 21, 21 };
        si.buildableUnits               = new int[] { UNIT_TROOPER, UNIT_TROOPERS, UNIT_INVALID, UNIT_INVALID, UNIT_INVALID, UNIT_INVALID, UNIT_INVALID, UNIT_INVALID };
        si.upgradeCampaign              = new int[] { 6, 0, 0 };

        return si;
    }

    private static StructureInfo SI_8() {
        StructureInfo si = new StructureInfo();
        si.o                            = new GObjectInfo();
        si.o.stringID_abbrev            = STR_CONST_YARD;
        si.o.name                       = "Const Yard";
        si.o.stringID_full              = STR_CONSTRUCTION_YARD;
        si.o.wsa                        = "construc.wsa";
        si.o.flags                      = new GObjectInfoFlags();
        si.o.flags.hasShadow            = false;
        si.o.flags.factory              = true;
        si.o.flags.notOnConcrete        = true;
        si.o.flags.busyStateIsIncoming  = false;
        si.o.flags.blurTile             = false;
        si.o.flags.hasTurret            = false;
        si.o.flags.conquerable          = true;
        si.o.flags.canBePickedUp        = false;
        si.o.flags.noMessageOnDeath     = false;
        si.o.flags.tabSelectable        = false;
        si.o.flags.scriptNoSlowdown     = false;
        si.o.flags.targetAir            = false;
        si.o.flags.priority             = false;
        si.o.spawnChance                = 64;
        si.o.hitpoints                  = 400;
        si.o.fogUncoverRadius           = 3;
        si.o.spriteID                   = 72;
        si.o.buildCredits               = 400;
        si.o.buildTime                  = 80;
        si.o.availableCampaign          = 99;
        si.o.structuresRequired         = FLAG_STRUCTURE_NEVER;
        si.o.sortPriority               = 0;
        si.o.upgradeLevelRequired       = 0;
        si.o.actionsPlayer              = new int[] { ACTION_ATTACK, ACTION_ATTACK, ACTION_ATTACK, ACTION_ATTACK };
        si.o.available                  = 0;
        si.o.hintStringID               = STR_CONSTRUCTION_FACILITY_ALL_STRUCTURES_ARE_BUILT_BY_THE_CONSTRUCTION_FACILITY;
        si.o.priorityBuild              = 0;
        si.o.priorityTarget             = 300;
        si.o.availableHouse             = FLAG_HOUSE_ALL;
        si.enterFilter                  = FLAG_UNIT_NONE;
        si.creditsStorage               = 0;
        si.powerUsage                   = 0;
        si.layout                       = STRUCTURE_LAYOUT_2x2;
        si.iconGroup                    = ICM_ICONGROUP_CONSTRUCTION_YARD;
        si.animationIndex               = new int[] { 22, 22, 22 };
        si.buildableUnits               = new int[] { UNIT_INVALID, UNIT_INVALID, UNIT_INVALID, UNIT_INVALID, UNIT_INVALID, UNIT_INVALID, UNIT_INVALID, UNIT_INVALID };
        si.upgradeCampaign              = new int[] { 4, 6, 0 };

        return si;
    }

    private static StructureInfo SI_9() {
        StructureInfo si = new StructureInfo();
        si.o                            = new GObjectInfo();
        si.o.stringID_abbrev            = STR_WINDTRAP;
        si.o.name                       = "Windtrap";
        si.o.stringID_full              = STR_WINDTRAP_POWER_CENTER;
        si.o.wsa                        = "windtrap.wsa";
        si.o.flags                      = new GObjectInfoFlags();
        si.o.flags.hasShadow            = false;
        si.o.flags.factory              = false;
        si.o.flags.notOnConcrete        = false;
        si.o.flags.busyStateIsIncoming  = false;
        si.o.flags.blurTile             = false;
        si.o.flags.hasTurret            = false;
        si.o.flags.conquerable          = true;
        si.o.flags.canBePickedUp        = false;
        si.o.flags.noMessageOnDeath     = false;
        si.o.flags.tabSelectable        = false;
        si.o.flags.scriptNoSlowdown     = false;
        si.o.flags.targetAir            = false;
        si.o.flags.priority             = false;
        si.o.spawnChance                = 64;
        si.o.hitpoints                  = 200;
        si.o.fogUncoverRadius           = 2;
        si.o.spriteID                   = 73;
        si.o.buildCredits               = 300;
        si.o.buildTime                  = 48;
        si.o.availableCampaign          = 1;
        si.o.structuresRequired         = FLAG_STRUCTURE_NONE;
        si.o.sortPriority               = 6;
        si.o.upgradeLevelRequired       = 0;
        si.o.actionsPlayer              = new int[] { ACTION_ATTACK, ACTION_ATTACK, ACTION_ATTACK, ACTION_ATTACK };
        si.o.available                  = 0;
        si.o.hintStringID               = STR_WINDTRAP_THE_WINDTRAP_SUPPLIES_POWER_TO_YOUR_BASE_WITHOUT_POWER_YOUR_STRUCTURES_WILL_DECAY;
        si.o.priorityBuild              = 0;
        si.o.priorityTarget             = 300;
        si.o.availableHouse             = FLAG_HOUSE_ALL;
        si.enterFilter                  = FLAG_UNIT_NONE;
        si.creditsStorage               = 0;
        si.powerUsage                   = -100;
        si.layout                       = STRUCTURE_LAYOUT_2x2;
        si.iconGroup                    = ICM_ICONGROUP_WINDTRAP_POWER;
        si.animationIndex               = new int[] { 26, 26, 26 };
        si.buildableUnits               = new int[] { UNIT_INVALID, UNIT_INVALID, UNIT_INVALID, UNIT_INVALID, UNIT_INVALID, UNIT_INVALID, UNIT_INVALID, UNIT_INVALID };
        si.upgradeCampaign              = new int[] { 0, 0, 0 };

        return si;
    }

    private static StructureInfo SI_10() {
        StructureInfo si = new StructureInfo();
        si.o                            = new GObjectInfo();
        si.o.stringID_abbrev            = STR_BARRACKS;
        si.o.name                       = "Barracks";
        si.o.stringID_full              = STR_INFANTRY_BARRACKS;
        si.o.wsa                        = "barrac.wsa";
        si.o.flags                      = new GObjectInfoFlags();
        si.o.flags.hasShadow            = false;
        si.o.flags.factory              = true;
        si.o.flags.notOnConcrete        = false;
        si.o.flags.busyStateIsIncoming  = false;
        si.o.flags.blurTile             = false;
        si.o.flags.hasTurret            = false;
        si.o.flags.conquerable          = false;
        si.o.flags.canBePickedUp        = false;
        si.o.flags.noMessageOnDeath     = false;
        si.o.flags.tabSelectable        = false;
        si.o.flags.scriptNoSlowdown     = false;
        si.o.flags.targetAir            = false;
        si.o.flags.priority             = false;
        si.o.spawnChance                = 128;
        si.o.hitpoints                  = 300;
        si.o.fogUncoverRadius           = 2;
        si.o.spriteID                   = 74;
        si.o.buildCredits               = 300;
        si.o.buildTime                  = 72;
        si.o.availableCampaign          = 2;
        si.o.structuresRequired         = FLAG_STRUCTURE_OUTPOST | FLAG_STRUCTURE_WINDTRAP;
        si.o.sortPriority               = 18;
        si.o.upgradeLevelRequired       = 0;
        si.o.actionsPlayer              = new int[] { ACTION_ATTACK, ACTION_ATTACK, ACTION_ATTACK, ACTION_ATTACK };
        si.o.available                  = 0;
        si.o.hintStringID               = STR_BARRACKS_THE_BARRACKS_IS_USED_TO_TRAIN_YOUR_LIGHT_INFANTRY;
        si.o.priorityBuild              = 0;
        si.o.priorityTarget             = 100;
        si.o.availableHouse             = FLAG_HOUSE_MERCENARY | FLAG_HOUSE_SARDAUKAR | FLAG_HOUSE_FREMEN | FLAG_HOUSE_ORDOS | FLAG_HOUSE_ATREIDES;
        si.enterFilter                  = FLAG_UNIT_NONE;
        si.creditsStorage               = 0;
        si.powerUsage                   = 10;
        si.layout                       = STRUCTURE_LAYOUT_2x2;
        si.iconGroup                    = ICM_ICONGROUP_INFANTRY_BARRACKS;
        si.animationIndex               = new int[] { 28, 28, 28 };
        si.buildableUnits               = new int[] { UNIT_SOLDIER, UNIT_INFANTRY, UNIT_INVALID, UNIT_INVALID, UNIT_INVALID, UNIT_INVALID, UNIT_INVALID, UNIT_INVALID };
        si.upgradeCampaign              = new int[] { 2, 0, 0 };

        return si;
    }

    private static StructureInfo SI_11() {
        StructureInfo si = new StructureInfo();
        si.o                            = new GObjectInfo();
        si.o.stringID_abbrev            = STR_STARPORT;
        si.o.name                       = "Starport";
        si.o.stringID_full              = STR_STARPORT_FACILITY;
        si.o.wsa                        = "starport.wsa";
        si.o.flags                      = new GObjectInfoFlags();
        si.o.flags.hasShadow            = false;
        si.o.flags.factory              = true;
        si.o.flags.notOnConcrete        = false;
        si.o.flags.busyStateIsIncoming  = true;
        si.o.flags.blurTile             = false;
        si.o.flags.hasTurret            = false;
        si.o.flags.conquerable          = true;
        si.o.flags.canBePickedUp        = false;
        si.o.flags.noMessageOnDeath     = false;
        si.o.flags.tabSelectable        = false;
        si.o.flags.scriptNoSlowdown     = false;
        si.o.flags.targetAir            = false;
        si.o.flags.priority             = false;
        si.o.spawnChance                = 128;
        si.o.hitpoints                  = 500;
        si.o.fogUncoverRadius           = 6;
        si.o.spriteID                   = 75;
        si.o.buildCredits               = 500;
        si.o.buildTime                  = 120;
        si.o.availableCampaign          = 6;
        si.o.structuresRequired         = FLAG_STRUCTURE_REFINERY | FLAG_STRUCTURE_WINDTRAP;
        si.o.sortPriority               = 32;
        si.o.upgradeLevelRequired       = 0;
        si.o.actionsPlayer              = new int[] { ACTION_ATTACK, ACTION_ATTACK, ACTION_ATTACK, ACTION_ATTACK };
        si.o.available                  = 0;
        si.o.hintStringID               = STR_STARTPORT_THE_STARPORT_IS_USED_TO_ORDER_AND_RECEIVE_SHIPMENTS_FROM_CHOAM;
        si.o.priorityBuild              = 0;
        si.o.priorityTarget             = 250;
        si.o.availableHouse             = FLAG_HOUSE_ALL;
        si.enterFilter                  = FLAG_UNIT_NONE;
        si.creditsStorage               = 0;
        si.powerUsage                   = 50;
        si.layout                       = STRUCTURE_LAYOUT_3x3;
        si.iconGroup                    = ICM_ICONGROUP_STARPORT_FACILITY;
        si.animationIndex               = new int[] { 5, 6, 7 };
        si.buildableUnits               = new int[] { UNIT_INVALID, UNIT_INVALID, UNIT_INVALID, UNIT_INVALID, UNIT_INVALID, UNIT_INVALID, UNIT_INVALID, UNIT_INVALID };
        si.upgradeCampaign              = new int[] { 0, 0, 0 };

        return si;
    }

    private static StructureInfo SI_12() {
        StructureInfo si = new StructureInfo();
        si.o                            = new GObjectInfo();
        si.o.stringID_abbrev            = STR_REFINERY;
        si.o.name                       = "Refinery";
        si.o.stringID_full              = STR_SPICE_REFINERY;
        si.o.wsa                        = "refinery.wsa";
        si.o.flags                      = new GObjectInfoFlags();
        si.o.flags.hasShadow            = false;
        si.o.flags.factory              = false;
        si.o.flags.notOnConcrete        = false;
        si.o.flags.busyStateIsIncoming  = true;
        si.o.flags.blurTile             = false;
        si.o.flags.hasTurret            = false;
        si.o.flags.conquerable          = true;
        si.o.flags.canBePickedUp        = false;
        si.o.flags.noMessageOnDeath     = false;
        si.o.flags.tabSelectable        = false;
        si.o.flags.scriptNoSlowdown     = false;
        si.o.flags.targetAir            = false;
        si.o.flags.priority             = false;
        si.o.spawnChance                = 128;
        si.o.hitpoints                  = 450;
        si.o.fogUncoverRadius           = 4;
        si.o.spriteID                   = 76;
        si.o.buildCredits               = 400;
        si.o.buildTime                  = 80;
        si.o.availableCampaign          = 1;
        si.o.structuresRequired         = FLAG_STRUCTURE_WINDTRAP;
        si.o.sortPriority               = 8;
        si.o.upgradeLevelRequired       = 0;
        si.o.actionsPlayer              = new int[] { ACTION_ATTACK, ACTION_ATTACK, ACTION_ATTACK, ACTION_ATTACK };
        si.o.available                  = 0;
        si.o.hintStringID               = STR_SPICE_REFINERY_THE_REFINERY_CONVERTS_SPICE_INTO_CREDITS;
        si.o.priorityBuild              = 0;
        si.o.priorityTarget             = 300;
        si.o.availableHouse             = FLAG_HOUSE_ALL;
        si.enterFilter                  = FLAG_UNIT_HARVESTER;
        si.creditsStorage               = 1005;
        si.powerUsage                   = 30;
        si.layout                       = STRUCTURE_LAYOUT_3x2;
        si.iconGroup                    = ICM_ICONGROUP_SPICE_REFINERY;
        si.animationIndex               = new int[] { 17, 18, 19 };
        si.buildableUnits               = new int[] { UNIT_INVALID, UNIT_INVALID, UNIT_INVALID, UNIT_INVALID, UNIT_INVALID, UNIT_INVALID, UNIT_INVALID, UNIT_INVALID };
        si.upgradeCampaign              = new int[] { 0, 0, 0 };

        return si;
    }

    private static StructureInfo SI_13() {
        StructureInfo si = new StructureInfo();
        si.o                            = new GObjectInfo();
        si.o.stringID_abbrev            = STR_REPAIR2;
        si.o.name                       = "Repair";
        si.o.stringID_full              = STR_REPAIR_FACILITY;
        si.o.wsa                        = "repair.wsa";
        si.o.flags                      = new GObjectInfoFlags();
        si.o.flags.hasShadow            = false;
        si.o.flags.factory              = false;
        si.o.flags.notOnConcrete        = false;
        si.o.flags.busyStateIsIncoming  = false;
        si.o.flags.blurTile             = false;
        si.o.flags.hasTurret            = false;
        si.o.flags.conquerable          = true;
        si.o.flags.canBePickedUp        = false;
        si.o.flags.noMessageOnDeath     = false;
        si.o.flags.tabSelectable        = false;
        si.o.flags.scriptNoSlowdown     = false;
        si.o.flags.targetAir            = false;
        si.o.flags.priority             = false;
        si.o.spawnChance                = 128;
        si.o.hitpoints                  = 200;
        si.o.fogUncoverRadius           = 3;
        si.o.spriteID                   = 77;
        si.o.buildCredits               = 700;
        si.o.buildTime                  = 80;
        si.o.availableCampaign          = 5;
        si.o.structuresRequired         = FLAG_STRUCTURE_OUTPOST | FLAG_STRUCTURE_WINDTRAP | FLAG_STRUCTURE_LIGHT_VEHICLE;
        si.o.sortPriority               = 24;
        si.o.upgradeLevelRequired       = 0;
        si.o.actionsPlayer              = new int[] { ACTION_ATTACK, ACTION_ATTACK, ACTION_ATTACK, ACTION_ATTACK };
        si.o.available                  = 0;
        si.o.hintStringID               = STR_REPAIR_FACILITY_THE_REPAIR_FACILITY_IS_USED_TO_REPAIR_YOUR_VEHICLES;
        si.o.priorityBuild              = 0;
        si.o.priorityTarget             = 600;
        si.o.availableHouse             = FLAG_HOUSE_ALL;
        si.enterFilter                  = FLAG_UNIT_HARVESTER | FLAG_UNIT_QUAD | FLAG_UNIT_RAIDER_TRIKE | FLAG_UNIT_TRIKE | FLAG_UNIT_SONIC_TANK | FLAG_UNIT_DEVASTATOR | FLAG_UNIT_SIEGE_TANK | FLAG_UNIT_TANK | FLAG_UNIT_DEVIATOR | FLAG_UNIT_LAUNCHER;
        si.creditsStorage               = 0;
        si.powerUsage                   = 20;
        si.layout                       = STRUCTURE_LAYOUT_3x2;
        si.iconGroup                    = ICM_ICONGROUP_VEHICLE_REPAIR_CENTRE;
        si.animationIndex               = new int[] { 23, 24, 25 };
        si.buildableUnits               = new int[] { UNIT_INVALID, UNIT_INVALID, UNIT_INVALID, UNIT_INVALID, UNIT_INVALID, UNIT_INVALID, UNIT_INVALID, UNIT_INVALID };
        si.upgradeCampaign              = new int[] { 0, 0, 0 };

        return si;
    }

    private static StructureInfo SI_14() {
        StructureInfo si = new StructureInfo();
        si.o                            = new GObjectInfo();
        si.o.stringID_abbrev            = STR_WALL;
        si.o.name                       = "Wall";
        si.o.stringID_full              = STR_BASE_DEFENSE_WALL;
        si.o.wsa                        = "wall.wsa";
        si.o.flags                      = new GObjectInfoFlags();
        si.o.flags.hasShadow            = false;
        si.o.flags.factory              = false;
        si.o.flags.notOnConcrete        = false;
        si.o.flags.busyStateIsIncoming  = false;
        si.o.flags.blurTile             = false;
        si.o.flags.hasTurret            = false;
        si.o.flags.conquerable          = false;
        si.o.flags.canBePickedUp        = false;
        si.o.flags.noMessageOnDeath     = false;
        si.o.flags.tabSelectable        = false;
        si.o.flags.scriptNoSlowdown     = false;
        si.o.flags.targetAir            = false;
        si.o.flags.priority             = false;
        si.o.spawnChance                = 0;
        si.o.hitpoints                  = 50;
        si.o.fogUncoverRadius           = 1;
        si.o.spriteID                   = 78;
        si.o.buildCredits               = 50;
        si.o.buildTime                  = 40;
        si.o.availableCampaign          = 4;
        si.o.structuresRequired         = FLAG_STRUCTURE_OUTPOST | FLAG_STRUCTURE_WINDTRAP;
        si.o.sortPriority               = 16;
        si.o.upgradeLevelRequired       = 0;
        si.o.actionsPlayer              = new int[] { ACTION_ATTACK, ACTION_ATTACK, ACTION_ATTACK, ACTION_ATTACK };
        si.o.available                  = 0;
        si.o.hintStringID               = STR_WALL_THE_WALL_IS_USED_FOR_PASSIVE_DEFENSE;
        si.o.priorityBuild              = 0;
        si.o.priorityTarget             = 30;
        si.o.availableHouse             = FLAG_HOUSE_ALL;
        si.enterFilter                  = FLAG_UNIT_NONE;
        si.creditsStorage               = 0;
        si.powerUsage                   = 0;
        si.layout                       = STRUCTURE_LAYOUT_1x1;
        si.iconGroup                    = ICM_ICONGROUP_WALLS;
        si.animationIndex               = new int[] { 0xFF, 0xFF, 0xFF };
        si.buildableUnits               = new int[] { UNIT_INVALID, UNIT_INVALID, UNIT_INVALID, UNIT_INVALID, UNIT_INVALID, UNIT_INVALID, UNIT_INVALID, UNIT_INVALID };
        si.upgradeCampaign              = new int[] { 0, 0, 0 };

        return si;
    }

    private static StructureInfo SI_15() {
        StructureInfo si = new StructureInfo();
        si.o                            = new GObjectInfo();
        si.o.stringID_abbrev            = STR_TURRET;
        si.o.name                       = "Turret";
        si.o.stringID_full              = STR_CANNON_TURRET;
        si.o.wsa                        = "turret.wsa";
        si.o.flags                      = new GObjectInfoFlags();
        si.o.flags.hasShadow            = false;
        si.o.flags.factory              = false;
        si.o.flags.notOnConcrete        = false;
        si.o.flags.busyStateIsIncoming  = false;
        si.o.flags.blurTile             = false;
        si.o.flags.hasTurret            = false;
        si.o.flags.conquerable          = true;
        si.o.flags.canBePickedUp        = false;
        si.o.flags.noMessageOnDeath     = false;
        si.o.flags.tabSelectable        = false;
        si.o.flags.scriptNoSlowdown     = false;
        si.o.flags.targetAir            = false;
        si.o.flags.priority             = false;
        si.o.spawnChance                = 64;
        si.o.hitpoints                  = 200;
        si.o.fogUncoverRadius           = 2;
        si.o.spriteID                   = 79;
        si.o.buildCredits               = 125;
        si.o.buildTime                  = 64;
        si.o.availableCampaign          = 5;
        si.o.structuresRequired         = FLAG_STRUCTURE_OUTPOST | FLAG_STRUCTURE_WINDTRAP;
        si.o.sortPriority               = 22;
        si.o.upgradeLevelRequired       = 0;
        si.o.actionsPlayer              = new int[] { ACTION_ATTACK, ACTION_ATTACK, ACTION_ATTACK, ACTION_ATTACK };
        si.o.available                  = 0;
        si.o.hintStringID               = STR_GUN_TURRET_THE_CANNON_TURRET_IS_USED_FOR_SHORT_RANGE_ACTIVE_DEFENSE;
        si.o.priorityBuild              = 75;
        si.o.priorityTarget             = 150;
        si.o.availableHouse             = FLAG_HOUSE_ALL;
        si.enterFilter                  = FLAG_UNIT_NONE;
        si.creditsStorage               = 0;
        si.powerUsage                   = 10;
        si.layout                       = STRUCTURE_LAYOUT_1x1;
        si.iconGroup                    = ICM_ICONGROUP_BASE_DEFENSE_TURRET;
        si.animationIndex               = new int[] { 0xFF, 0xFF, 0xFF };
        si.buildableUnits               = new int[] { UNIT_INVALID, UNIT_INVALID, UNIT_INVALID, UNIT_INVALID, UNIT_INVALID, UNIT_INVALID, UNIT_INVALID, UNIT_INVALID };
        si.upgradeCampaign              = new int[] { 0, 0, 0 };

        return si;
    }

    private static StructureInfo SI_16() {
        StructureInfo si = new StructureInfo();
        si.o                            = new GObjectInfo();
        si.o.stringID_abbrev            = STR_RTURRET;
        si.o.name                       = "R-Turret";
        si.o.stringID_full              = STR_ROCKET_TURRET;
        si.o.wsa                        = "rturret.wsa";
        si.o.flags                      = new GObjectInfoFlags();
        si.o.flags.hasShadow            = false;
        si.o.flags.factory              = false;
        si.o.flags.notOnConcrete        = false;
        si.o.flags.busyStateIsIncoming  = false;
        si.o.flags.blurTile             = false;
        si.o.flags.hasTurret            = false;
        si.o.flags.conquerable          = true;
        si.o.flags.canBePickedUp        = false;
        si.o.flags.noMessageOnDeath     = false;
        si.o.flags.tabSelectable        = false;
        si.o.flags.scriptNoSlowdown     = false;
        si.o.flags.targetAir            = false;
        si.o.flags.priority             = false;
        si.o.spawnChance                = 64;
        si.o.hitpoints                  = 200;
        si.o.fogUncoverRadius           = 5;
        si.o.spriteID                   = 80;
        si.o.buildCredits               = 250;
        si.o.buildTime                  = 96;
        si.o.availableCampaign          = 0;
        si.o.structuresRequired         = FLAG_STRUCTURE_OUTPOST | FLAG_STRUCTURE_WINDTRAP;
        si.o.sortPriority               = 26;
        si.o.upgradeLevelRequired       = 2;
        si.o.actionsPlayer              = new int[] { ACTION_ATTACK, ACTION_ATTACK, ACTION_ATTACK, ACTION_ATTACK };
        si.o.available                  = 0;
        si.o.hintStringID               = STR_ROCKET_TURRET_THE_ROCKETCANNON_TURRET_IS_USED_FOR_BOTH_SHORT_AND_MEDIUM_RANGE_ACTIVE_DEFENSE;
        si.o.priorityBuild              = 100;
        si.o.priorityTarget             = 75;
        si.o.availableHouse             = FLAG_HOUSE_ALL;
        si.enterFilter                  = FLAG_UNIT_NONE;
        si.creditsStorage               = 0;
        si.powerUsage                   = 25;
        si.layout                       = STRUCTURE_LAYOUT_1x1;
        si.iconGroup                    = ICM_ICONGROUP_BASE_ROCKET_TURRET;
        si.animationIndex               = new int[] { 0xFF, 0xFF, 0xFF };
        si.buildableUnits               = new int[] { UNIT_INVALID, UNIT_INVALID, UNIT_INVALID, UNIT_INVALID, UNIT_INVALID, UNIT_INVALID, UNIT_INVALID, UNIT_INVALID };
        si.upgradeCampaign              = new int[] { 0, 0, 0 };

        return si;
    }

    private static StructureInfo SI_17() {
        StructureInfo si = new StructureInfo();
        si.o                            = new GObjectInfo();
        si.o.stringID_abbrev            = STR_SPICE_SILO;
        si.o.name                       = "Spice Silo";
        si.o.stringID_full              = STR_SPICE_STORAGE_SILO;
        si.o.wsa                        = "storage.wsa";
        si.o.flags                      = new GObjectInfoFlags();
        si.o.flags.hasShadow            = false;
        si.o.flags.factory              = false;
        si.o.flags.notOnConcrete        = false;
        si.o.flags.busyStateIsIncoming  = false;
        si.o.flags.blurTile             = false;
        si.o.flags.hasTurret            = false;
        si.o.flags.conquerable          = true;
        si.o.flags.canBePickedUp        = false;
        si.o.flags.noMessageOnDeath     = false;
        si.o.flags.tabSelectable        = false;
        si.o.flags.scriptNoSlowdown     = false;
        si.o.flags.targetAir            = false;
        si.o.flags.priority             = false;
        si.o.spawnChance                = 0;
        si.o.hitpoints                  = 150;
        si.o.fogUncoverRadius           = 2;
        si.o.spriteID                   = 81;
        si.o.buildCredits               = 150;
        si.o.buildTime                  = 48;
        si.o.availableCampaign          = 2;
        si.o.structuresRequired         = FLAG_STRUCTURE_REFINERY | FLAG_STRUCTURE_WINDTRAP;
        si.o.sortPriority               = 12;
        si.o.upgradeLevelRequired       = 0;
        si.o.actionsPlayer              = new int[] { ACTION_ATTACK, ACTION_ATTACK, ACTION_ATTACK, ACTION_ATTACK };
        si.o.available                  = 0;
        si.o.hintStringID               = STR_SPICE_SILO_THE_SPICE_SILO_IS_USED_TO_STORE_REFINED_SPICE;
        si.o.priorityBuild              = 0;
        si.o.priorityTarget             = 150;
        si.o.availableHouse             = FLAG_HOUSE_ALL;
        si.enterFilter                  = FLAG_UNIT_NONE;
        si.creditsStorage               = 1000;
        si.powerUsage                   = 5;
        si.layout                       = STRUCTURE_LAYOUT_2x2;
        si.iconGroup                    = ICM_ICONGROUP_SPICE_STORAGE_SILO;
        si.animationIndex               = new int[] { 27, 27, 27 };
        si.buildableUnits               = new int[] { UNIT_INVALID, UNIT_INVALID, UNIT_INVALID, UNIT_INVALID, UNIT_INVALID, UNIT_INVALID, UNIT_INVALID, UNIT_INVALID };
        si.upgradeCampaign              = new int[] { 0, 0, 0 };

        return si;
    }

    private static StructureInfo SI_18() {
        StructureInfo si = new StructureInfo();
        si.o                            = new GObjectInfo();
        si.o.stringID_abbrev            = STR_OUTPOST;
        si.o.name                       = "Outpost";
        si.o.stringID_full              = STR_RADAR_OUTPOST;
        si.o.wsa                        = "headqrts.wsa";
        si.o.flags                      = new GObjectInfoFlags();
        si.o.flags.hasShadow            = false;
        si.o.flags.factory              = false;
        si.o.flags.notOnConcrete        = false;
        si.o.flags.busyStateIsIncoming  = false;
        si.o.flags.blurTile             = false;
        si.o.flags.hasTurret            = false;
        si.o.flags.conquerable          = false;
        si.o.flags.canBePickedUp        = false;
        si.o.flags.noMessageOnDeath     = false;
        si.o.flags.tabSelectable        = false;
        si.o.flags.scriptNoSlowdown     = false;
        si.o.flags.targetAir            = false;
        si.o.flags.priority             = false;
        si.o.spawnChance                = 128;
        si.o.hitpoints                  = 500;
        si.o.fogUncoverRadius           = 10;
        si.o.spriteID                   = 82;
        si.o.buildCredits               = 400;
        si.o.buildTime                  = 80;
        si.o.availableCampaign          = 2;
        si.o.structuresRequired         = FLAG_STRUCTURE_WINDTRAP;
        si.o.sortPriority               = 10;
        si.o.upgradeLevelRequired       = 0;
        si.o.actionsPlayer              = new int[] { ACTION_ATTACK, ACTION_ATTACK, ACTION_ATTACK, ACTION_ATTACK };
        si.o.available                  = 0;
        si.o.hintStringID               = STR_OUTPOST_THE_OUTPOST_PROVIDES_RADAR_AND_AIDS_CONTROL_OF_DISTANT_VEHICLES;
        si.o.priorityBuild              = 0;
        si.o.priorityTarget             = 275;
        si.o.availableHouse             = FLAG_HOUSE_ALL;
        si.enterFilter                  = FLAG_UNIT_NONE;
        si.creditsStorage               = 0;
        si.powerUsage                   = 3;
        si.layout                       = STRUCTURE_LAYOUT_2x2;
        si.iconGroup                    = ICM_ICONGROUP_RADAR_OUTPOST;
        si.animationIndex               = new int[] { 3, 3, 3 };
        si.buildableUnits               = new int[] { UNIT_INVALID, UNIT_INVALID, UNIT_INVALID, UNIT_INVALID, UNIT_INVALID, UNIT_INVALID, UNIT_INVALID, UNIT_INVALID };
        si.upgradeCampaign              = new int[] { 0, 0, 0 };

        return si;
    }
}
