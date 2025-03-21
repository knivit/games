package com.tsoft.dune2.structure;

import com.tsoft.dune2.gobject.GObject;
import com.tsoft.dune2.gobject.GObjectInfo;
import com.tsoft.dune2.gui.widget.Widget;
import com.tsoft.dune2.house.House;
import com.tsoft.dune2.house.HouseInfo;
import com.tsoft.dune2.map.Tile;
import com.tsoft.dune2.pool.PoolFindStruct;
import com.tsoft.dune2.team.Team;
import com.tsoft.dune2.tile.Tile32;
import com.tsoft.dune2.unit.Unit;
import com.tsoft.dune2.unit.UnitInfo;

import static com.tsoft.dune2.animation.AnimationService.Animation_Start;
import static com.tsoft.dune2.animation.AnimationService.Animation_Stop_ByTile;
import static com.tsoft.dune2.audio.SoundService.Sound_Output_Feedback;
import static com.tsoft.dune2.explosion.ExplosionType.EXPLOSION_IMPACT_LARGE;
import static com.tsoft.dune2.gfx.GfxService.*;
import static com.tsoft.dune2.gobject.GObjectService.Object_GetByPackedTile;
import static com.tsoft.dune2.gobject.GObjectService.Object_Script_Variable4_Clear;
import static com.tsoft.dune2.gui.FactoryResult.*;
import static com.tsoft.dune2.gui.GuiService.*;
import static com.tsoft.dune2.gui.SelectionType.*;
import static com.tsoft.dune2.gui.widget.WidgetDrawService.GUI_Widget_ActionPanel_Draw;
import static com.tsoft.dune2.gui.widget.WidgetService.GUI_Widget_MakeNormal;
import static com.tsoft.dune2.gui.widget.WidgetService.GUI_Widget_MakeSelected;
import static com.tsoft.dune2.house.HouseService.*;
import static com.tsoft.dune2.house.HouseType.*;
import static com.tsoft.dune2.house.HouseWeapon.*;
import static com.tsoft.dune2.map.LandscapeType.*;
import static com.tsoft.dune2.map.MapService.*;
import static com.tsoft.dune2.opendune.OpenDuneService.*;
import static com.tsoft.dune2.pool.PoolHouseService.House_Get_ByIndex;
import static com.tsoft.dune2.pool.PoolStructureService.*;
import static com.tsoft.dune2.pool.PoolTeamService.Team_Find;
import static com.tsoft.dune2.pool.PoolUnitService.*;
import static com.tsoft.dune2.scenario.ScenarioService.g_scenario;
import static com.tsoft.dune2.script.ScriptService.*;
import static com.tsoft.dune2.sprites.IconMapEntries.ICM_ICONGROUP_BASE_DEFENSE_TURRET;
import static com.tsoft.dune2.sprites.IconMapEntries.ICM_ICONGROUP_BASE_ROCKET_TURRET;
import static com.tsoft.dune2.sprites.SpritesService.*;
import static com.tsoft.dune2.strings.StringService.String_Get_ByIndex;
import static com.tsoft.dune2.strings.Strings.*;
import static com.tsoft.dune2.table.TableAnimation.g_table_animation_structure;
import static com.tsoft.dune2.table.TableHouseInfo.g_table_houseInfo;
import static com.tsoft.dune2.table.TableLandscapeInfo.g_table_landscapeInfo;
import static com.tsoft.dune2.table.TableStructureInfo.*;
import static com.tsoft.dune2.structure.StructureState.*;
import static com.tsoft.dune2.structure.StructureType.*;
import static com.tsoft.dune2.table.TableUnitInfo.g_table_unitInfo;
import static com.tsoft.dune2.table.TileDiff.g_table_mapDiff;
import static com.tsoft.dune2.tile.TileService.*;
import static com.tsoft.dune2.timer.TimerService.Timer_SetTimer;
import static com.tsoft.dune2.timer.TimerService.g_timerGame;
import static com.tsoft.dune2.timer.TimerType.TIMER_GAME;
import static com.tsoft.dune2.tools.IndexType.IT_STRUCTURE;
import static com.tsoft.dune2.tools.ToolsService.*;
import static com.tsoft.dune2.unit.ActionType.*;
import static com.tsoft.dune2.unit.UnitFlag.*;
import static com.tsoft.dune2.unit.UnitService.*;
import static com.tsoft.dune2.unit.UnitType.*;
import static org.lwjgl.system.libc.LibCString.memmove;

public class StructureService {

    public static Structure g_structureActive = null;
    public static int g_structureActivePosition = 0;
    public static int g_structureActiveType = 0;

    static boolean s_debugInstantBuild = false;   /* When non-zero, constructions are almost instant. */
    static long s_tickStructureDegrade   = 0;     /* Indicates next time Degrade function is executed. */
    static long s_tickStructureStructure = 0;     /* Indicates next time Structures function is executed. */
    static long s_tickStructureScript    = 0;     /* Indicates next time Script function is executed. */
    static long s_tickStructurePalace    = 0;     /* Indicates next time Palace function is executed. */

    public static int g_structureIndex;

    /**
     * Loop over all structures, preforming various of tasks.
     */
    public static void GameLoop_Structure() {
        boolean tickDegrade = false;
        boolean tickStructure = false;
        boolean tickScript = false;
        boolean tickPalace = false;

        if (s_tickStructureDegrade <= g_timerGame && g_campaignID > 1) {
            tickDegrade = true;
            s_tickStructureDegrade = g_timerGame + Tools_AdjustToGameSpeed(10800, 5400, 21600, true);
        }

        if (s_tickStructureStructure <= g_timerGame || s_debugInstantBuild) {
            tickStructure = true;
            s_tickStructureStructure = g_timerGame + Tools_AdjustToGameSpeed(30, 15, 60, true);
        }

        if (s_tickStructureScript <= g_timerGame) {
            tickScript = true;
            s_tickStructureScript = g_timerGame + 5;
        }

        if (s_tickStructurePalace <= g_timerGame) {
            tickPalace = true;
            s_tickStructurePalace = g_timerGame + 60;
        }

        PoolFindStruct find = new PoolFindStruct();
        find.houseID = HOUSE_INVALID;
        find.index = 0xFFFF;
        find.type = 0xFFFF;

        if (g_debugScenario) return;

        while (true) {
            Structure s = Structure_Find(find);
            if (s == null) break;

            if (s.o.type == STRUCTURE_SLAB_1x1 || s.o.type == STRUCTURE_SLAB_2x2 || s.o.type == STRUCTURE_WALL) {
                continue;
            }

            StructureInfo si = g_table_structureInfo[s.o.type];
            House h = House_Get_ByIndex(s.o.houseID);
            HouseInfo hi = g_table_houseInfo[h.index];

            g_scriptCurrentObject    = s.o;
            g_scriptCurrentStructure = s;
            g_scriptCurrentUnit      = null;
            g_scriptCurrentTeam      = null;

            if (tickPalace && s.o.type == STRUCTURE_PALACE) {
                if (s.countDown != 0) {
                    s.countDown--;

                    if (s.o.houseID == g_playerHouseID) {
                        GUI_Widget_ActionPanel_Draw(true);
                    }
                }

                /* Check if we have to fire the weapon for the AI immediately */
                if (s.countDown == 0 && !h.flags.human && h.flags.isAIActive) {
                    Structure_ActivateSpecial(s);
                }
            }

            if (tickDegrade && s.o.flags.degrades && s.o.hitpoints > si.o.hitpoints / 2) {
                Structure_Damage(s, hi.degradingAmount, 0);
            }

            if (tickStructure) {
                if (s.o.flags.upgrading) {
                    int upgradeCost = si.o.buildCredits / 40;

                    if (upgradeCost <= h.credits) {
                        h.credits -= upgradeCost;

                        if (s.upgradeTimeLeft > 5) {
                            s.upgradeTimeLeft -= 5;
                        } else {
                            s.upgradeLevel++;
                            s.o.flags.upgrading = false;

                            /* Ordos Heavy Vehicle gets the last upgrade for free */
                            if (s.o.houseID == HOUSE_ORDOS && s.o.type == STRUCTURE_HEAVY_VEHICLE && s.upgradeLevel == 2) s.upgradeLevel = 3;

                            s.upgradeTimeLeft = Structure_IsUpgradable(s) ? 100 : 0;
                        }
                    } else {
                        s.o.flags.upgrading = false;
                    }
                } else if (s.o.flags.repairing) {
                    int repairCost;

                    /* ENHANCEMENT -- The calculation of the repair cost is a bit unfair in Dune2, because of rounding errors (they use a 256 float-resolution, which is not sufficient) */
                    if (g_dune2_enhanced) {
                        repairCost = si.o.buildCredits * 2 / si.o.hitpoints;
                    } else {
                        repairCost = ((2 * 256 / si.o.hitpoints) * si.o.buildCredits + 128) / 256;
                    }

                    if (repairCost <= h.credits) {
                        h.credits -= repairCost;

                        /* AIs repair in early games slower than in later games */
                        if (s.o.houseID == g_playerHouseID || g_campaignID >= 3) {
                            s.o.hitpoints += 5;
                        } else {
                            s.o.hitpoints += 3;
                        }

                        if (s.o.hitpoints > si.o.hitpoints) {
                            s.o.hitpoints = si.o.hitpoints;
                            s.o.flags.repairing = false;
                            s.o.flags.onHold = false;
                        }
                    } else {
                        s.o.flags.repairing = false;
                    }
                } else {
                    if (!s.o.flags.onHold && s.countDown != 0 && s.o.linkedID != 0xFF && s.state == STRUCTURE_STATE_BUSY && si.o.flags.factory) {
                        GObjectInfo oi;
                        int buildSpeed;
                        int buildCost;

                        if (s.o.type == STRUCTURE_CONSTRUCTION_YARD) {
                            oi = g_table_structureInfo[s.objectType].o;
                        } else if (s.o.type == STRUCTURE_REPAIR) {
                            oi = g_table_unitInfo[Unit_Get_ByIndex(s.o.linkedID).o.type].o;
                        } else {
                            oi = g_table_unitInfo[s.objectType].o;
                        }

                        buildSpeed = 256;
                        if (s.o.hitpoints < si.o.hitpoints) {
                            buildSpeed = s.o.hitpoints * 256 / si.o.hitpoints;
                        }

                        /* For AIs, we slow down building speed in all but the last campaign */
                        if (g_playerHouseID != s.o.houseID) {
                            if (buildSpeed > g_campaignID * 20 + 95) {
                                buildSpeed = g_campaignID * 20 + 95;
                            }
                        }

                        buildCost = oi.buildCredits * 256 / oi.buildTime;

                        if (buildSpeed < 256) {
                            buildCost = buildSpeed * buildCost / 256;
                        }

                        if (s.o.type == STRUCTURE_REPAIR && buildCost > 4) {
                            buildCost /= 4;
                        }

                        buildCost += s.buildCostRemainder;

                        if (buildCost / 256 <= h.credits) {
                            s.buildCostRemainder = buildCost & 0xFF;
                            h.credits -= buildCost / 256;

                            if (buildSpeed < s.countDown) {
                                s.countDown -= buildSpeed;
                            } else {
                                s.countDown = 0;
                                s.buildCostRemainder = 0;

                                Structure_SetState(s, STRUCTURE_STATE_READY);

                                if (s.o.houseID == g_playerHouseID) {
                                    if (s.o.type != STRUCTURE_BARRACKS && s.o.type != STRUCTURE_WOR_TROOPER) {
                                        int stringID = STR_IS_COMPLETED_AND_AWAITING_ORDERS;
                                        if (s.o.type == STRUCTURE_HIGH_TECH) stringID = STR_IS_COMPLETE;
                                        if (s.o.type == STRUCTURE_CONSTRUCTION_YARD) stringID = STR_IS_COMPLETED_AND_READY_TO_PLACE;

                                        GUI_DisplayText("%s %s", 0, String_Get_ByIndex(oi.stringID_full), String_Get_ByIndex(stringID));

                                        Sound_Output_Feedback(0);
                                    }
                                } else if (s.o.type == STRUCTURE_CONSTRUCTION_YARD) {
                                    /* An AI immediately places the structure when it is done building */
                                    Structure ns;
                                    int i;

                                    ns = Structure_Get_ByIndex(s.o.linkedID);
                                    s.o.linkedID = 0xFF;

                                    /* The AI places structures which are operational immediately */
                                    Structure_SetState(s, STRUCTURE_STATE_IDLE);

                                    /* Find the position to place the structure */
                                    for (i = 0; i < 5; i++) {
                                        if (ns.o.type != h.ai_structureRebuild[i][0]) continue;

                                        if (!Structure_Place(ns, h.ai_structureRebuild[i][1])) continue;

                                        h.ai_structureRebuild[i][0] = 0;
                                        h.ai_structureRebuild[i][1] = 0;
                                        break;
                                    }

                                    /* If the AI no longer had in memory where to store the structure, free it and forget about it */
                                    if (i == 5) {
									    StructureInfo nsi = g_table_structureInfo[ns.o.type];

                                        h.credits += nsi.o.buildCredits;

                                        Structure_Free(ns);
                                    }
                                }
                            }
                        } else {
                            /* Out of money means the building gets put on hold */
                            if (s.o.houseID == g_playerHouseID) {
                                s.o.flags.onHold = true;
                                GUI_DisplayText(String_Get_ByIndex(STR_INSUFFICIENT_FUNDS_CONSTRUCTION_IS_HALTED), 0);
                            }
                        }
                    }

                    if (s.o.type == STRUCTURE_REPAIR) {
                        if (!s.o.flags.onHold && s.countDown != 0 && s.o.linkedID != 0xFF) {
						    UnitInfo ui;
                            int repairSpeed;
                            int repairCost;

                            ui = g_table_unitInfo[Unit_Get_ByIndex(s.o.linkedID).o.type];

                            repairSpeed = 256;
                            if (s.o.hitpoints < si.o.hitpoints) {
                                repairSpeed = s.o.hitpoints * 256 / si.o.hitpoints;
                            }

                            /* XXX -- This is highly unfair. Repairing becomes more expensive if your structure is more damaged */
                            repairCost = 2 * ui.o.buildCredits / 256;

                            if (repairCost < h.credits) {
                                h.credits -= repairCost;

                                if (repairSpeed < s.countDown) {
                                    s.countDown -= repairSpeed;
                                } else {
                                    s.countDown = 0;

                                    Structure_SetState(s, STRUCTURE_STATE_READY);

                                    if (s.o.houseID == g_playerHouseID) {
                                        Sound_Output_Feedback(g_playerHouseID + 55);
                                    }
                                }
                            }
                        } else if (h.credits != 0) {
                            /* Automatically resume repairing when there is money again */
                            s.o.flags.onHold = false;
                        }
                    }

                    /* AI maintenance on structures */
                    if (h.flags.isAIActive && s.o.flags.allocated && s.o.houseID != g_playerHouseID && h.credits != 0) {
                        /* When structure is below 50% hitpoints, start repairing */
                        if (s.o.hitpoints < si.o.hitpoints / 2) {
                            Structure_SetRepairingState(s, 1, null);
                        }

                        /* If the structure is not doing something, but can build stuff, see if there is stuff to build */
                        if (si.o.flags.factory && s.countDown == 0 && s.o.linkedID == 0xFF) {
                            int type = Structure_AI_PickNextToBuild(s);

                            if (type != 0xFFFF) Structure_BuildObject(s, type);
                        }
                    }
                }
            }

            if (tickScript) {
                if (s.o.script.delay != 0) {
                    s.o.script.delay--;
                } else {
                    if (Script_IsLoaded(s.o.script)) {
                        int i;

                        /* Run the script 3 times in a row */
                        for (i = 0; i < 3; i++) {
                            if (!Script_Run(s.o.script)) break;
                        }

                        /* ENHANCEMENT -- Dune2 aborts all other structures if one gives a script error. This doesn't seem correct */
                        if (!g_dune2_enhanced && i != 3) return;
                    } else {
                        Script_Reset(s.o.script, s.o.script.scriptInfo);
                        Script_Load(s.o.script, s.o.type);
                    }
                }
            }
        }
    }

    /**
     * Convert the name of a structure to the type value of that structure, or
     *  STRUCTURE_INVALID if not found.
     */
    public static int Structure_StringToType(String name) {
        if (name == null) {
            return STRUCTURE_INVALID;
        }

        for (int type = 0; type < STRUCTURE_MAX; type++) {
            if (g_table_structureInfo[type].o.name.equalsIgnoreCase(name)) {
                return type;
            }
        }

        return STRUCTURE_INVALID;
    }

    /**
     * Create a new Structure.
     *
     * @param index The new index of the Structure, or STRUCTURE_INDEX_INVALID to assign one.
     * @param typeID The type of the new Structure.
     * @param houseID The House of the new Structure.
     * @param position The packed position where to place the Structure. If 0xFFFF, the Structure is not placed.
     * @return The new created Structure, or NULL if something failed.
     */
    public static Structure Structure_Create(int index, int typeID, int houseID, int position) {
	    StructureInfo si;
        Structure s;

        if (houseID >= HOUSE_MAX) return null;
        if (typeID >= STRUCTURE_MAX) return null;

        si = g_table_structureInfo[typeID];
        s = Structure_Allocate(index, typeID);
        if (s == null) return null;

        s.o.houseID = houseID;
        s.creatorHouseID = houseID;
        s.o.flags.isNotOnMap = true;
        s.o.position.x = 0;
        s.o.position.y = 0;
        s.o.linkedID = 0xFF;
        s.state = (g_debugScenario) ? STRUCTURE_STATE_IDLE : STRUCTURE_STATE_JUSTBUILT;

        if (typeID == STRUCTURE_TURRET) {
            s.rotationSpriteDiff = g_iconMap[g_iconMap[ICM_ICONGROUP_BASE_DEFENSE_TURRET] + 1];
        }
        if (typeID == STRUCTURE_ROCKET_TURRET) {
            s.rotationSpriteDiff = g_iconMap[g_iconMap[ICM_ICONGROUP_BASE_ROCKET_TURRET] + 1];
        }

        s.o.hitpoints  = si.o.hitpoints;
        s.hitpointsMax = si.o.hitpoints;

        if (houseID == HOUSE_HARKONNEN && typeID == STRUCTURE_LIGHT_VEHICLE) {
            s.upgradeLevel = 1;
        }

        /* Check if there is an upgrade available */
        if (si.o.flags.factory) {
            s.upgradeTimeLeft = Structure_IsUpgradable(s) ? 100 : 0;
        }

        s.objectType = 0xFFFF;

        Structure_BuildObject(s, 0xFFFE);

        s.countDown = 0;

        /* AIs get the full upgrade immediately */
        if (houseID != g_playerHouseID) {
            while (true) {
                if (!Structure_IsUpgradable(s)) break;
                s.upgradeLevel++;
            }
            s.upgradeTimeLeft = 0;
        }

        if (position != 0xFFFF && !Structure_Place(s, position)) {
            Structure_Free(s);
            return null;
        }

        return s;
    }

    /**
     * Place a structure on the map.
     *
     * @param s The structure to place on the map.
     * @param position The (packed) tile to place the construction on.
     * @return True if and only if the structure is placed on the map.
     */
    public static boolean Structure_Place(Structure s, int position) {
	    StructureInfo si;
        int validBuildLocation;

        if (s == null) return false;
        if (position == 0xFFFF) return false;

        si = g_table_structureInfo[s.o.type];

        switch (s.o.type) {
            case STRUCTURE_WALL: {
                Tile t;

                if (Structure_IsValidBuildLocation(position, STRUCTURE_WALL) == 0) return false;

                t = g_map[position];
                t.groundTileID = g_wallTileID + 1;
                /* ENHANCEMENT -- Dune2 wrongfully only removes the lower 2 bits, where the lower 3 bits are the owner. This is no longer visible. */
                t.houseID  = s.o.houseID;

                g_mapTileID[position] |= 0x8000;

                if (s.o.houseID == g_playerHouseID) Tile_RemoveFogInRadius(Tile_UnpackTile(position), 1);

                if (Map_IsPositionUnveiled(position)) t.overlayTileID = 0;

                Structure_ConnectWall(position, true);
                Structure_Free(s);

            } return true;

            case STRUCTURE_SLAB_1x1:
            case STRUCTURE_SLAB_2x2: {
                int i, result;

                result = 0;

                for (i = 0; i < g_table_structure_layoutTileCount[si.layout]; i++) {
                    int curPos = position + g_table_structure_layoutTiles[si.layout][i];
                    Tile t = g_map[curPos];

                    if (Structure_IsValidBuildLocation(curPos, STRUCTURE_SLAB_1x1) == 0) continue;

                    t.groundTileID = g_builtSlabTileID;
                    t.houseID = s.o.houseID;

                    g_mapTileID[curPos] |= 0x8000;

                    if (s.o.houseID == g_playerHouseID) {
                        Tile_RemoveFogInRadius(Tile_UnpackTile(curPos), 1);
                    }

                    if (Map_IsPositionUnveiled(curPos)) t.overlayTileID = 0;

                    Map_Update(curPos, 0, false);

                    result = 1;
                }

                /* XXX -- Dirt hack -- Parts of the 2x2 slab can be outside the building area, so by doing the same loop twice it will build for sure */
                if (s.o.type == STRUCTURE_SLAB_2x2) {
                    for (i = 0; i < g_table_structure_layoutTileCount[si.layout]; i++) {
                        int curPos = position + g_table_structure_layoutTiles[si.layout][i];
                        Tile t = g_map[curPos];

                        if (Structure_IsValidBuildLocation(curPos, STRUCTURE_SLAB_1x1) == 0) continue;

                        t.groundTileID = g_builtSlabTileID;
                        t.houseID = s.o.houseID;

                        g_mapTileID[curPos] |= 0x8000;

                        if (s.o.houseID == g_playerHouseID) {
                            Tile_RemoveFogInRadius(Tile_UnpackTile(curPos), 1);
                            t.overlayTileID = 0;
                        }

                        Map_Update(curPos, 0, false);

                        result = 1;
                    }
                }

                if (result == 0) return false;

                Structure_Free(s);
            }

            return true;
        }

        validBuildLocation = Structure_IsValidBuildLocation(position, s.o.type);
        if (validBuildLocation == 0 && s.o.houseID == g_playerHouseID && !g_debugScenario && g_validateStrictIfZero == 0) {
            return false;
        }

        /* ENHANCEMENT -- In Dune2, it only removes the fog around the top-left tile of a structure, leaving for big structures the right in the fog. */
        if (!g_dune2_enhanced && s.o.houseID == g_playerHouseID) {
            Tile_RemoveFogInRadius(Tile_UnpackTile(position), 2);
        }

        s.o.seenByHouses |= 1 << s.o.houseID;
        if (s.o.houseID == g_playerHouseID) s.o.seenByHouses |= 0xFF;

        s.o.flags.isNotOnMap = false;

        s.o.position = Tile_UnpackTile(position);
        s.o.position.x &= 0xFF00;
        s.o.position.y &= 0xFF00;

        s.rotationSpriteDiff = 0;
        s.o.hitpoints  = si.o.hitpoints;
        s.hitpointsMax = si.o.hitpoints;

        /* If the return value is negative, there are tiles without slab. This gives a penalty to the hitpoints. */
        if (validBuildLocation < 0) {
            int tilesWithoutSlab = -validBuildLocation;
            int structureTileCount = g_table_structure_layoutTileCount[si.layout];

            s.o.hitpoints -= (si.o.hitpoints / 2) * tilesWithoutSlab / structureTileCount;

            s.o.flags.degrades = true;
        } else {
            /* ENHANCEMENT -- When you build a structure completely on slabs, it should not degrade */
            if (!g_dune2_enhanced) {
                s.o.flags.degrades = true;
            }
        }

        Script_Reset(s.o.script, g_scriptStructure);

        s.o.script.variables[0] = 0;
        s.o.script.variables[4] = 0;

        /* XXX -- Weird .. if 'position' enters with 0xFFFF it is returned immediately .. how can this ever NOT happen? */
        if (position != 0xFFFF) {
            s.o.script.delay = 0;
            Script_Reset(s.o.script, s.o.script.scriptInfo);
            Script_Load(s.o.script, s.o.type);
        }

        for (int i = 0; i < g_table_structure_layoutTileCount[si.layout]; i++) {
            int curPos = position + g_table_structure_layoutTiles[si.layout][i];
            Unit u;

            u = Unit_Get_ByPackedTile(curPos);

            Unit_Remove(u);

            /* ENHANCEMENT -- In Dune2, it only removes the fog around the top-left tile of a structure, leaving for big structures the right in the fog. */
            if (g_dune2_enhanced && s.o.houseID == g_playerHouseID) {
                Tile_RemoveFogInRadius(Tile_UnpackTile(curPos), 2);
            }

        }

        if (s.o.type == STRUCTURE_WINDTRAP) {
            House h = House_Get_ByIndex(s.o.houseID);
            h.windtrapCount += 1;
        }

        if (g_validateStrictIfZero == 0) {
            House h = House_Get_ByIndex(s.o.houseID);
            House_CalculatePowerAndCredit(h);
        }

        Structure_UpdateMap(s);

        House h = House_Get_ByIndex(s.o.houseID);
        h.structuresBuilt = Structure_GetStructuresBuilt(h);

        return true;
    }

    /**
     * Calculate the power usage and production, and the credits storage.
     *
     * @param h The house to calculate the numbers for.
     */
    public static void Structure_CalculateHitpointsMax(House h) {
        PoolFindStruct find = new PoolFindStruct();
        int power = 0;

        if (h == null) return;

        if (h.index == g_playerHouseID) House_UpdateRadarState(h);

        if (h.powerUsage == 0) {
            power = 256;
        } else {
            power = Math.min(h.powerProduction * 256 / h.powerUsage, 256);
        }

        find.houseID = h.index;
        find.index   = 0xFFFF;
        find.type    = 0xFFFF;

        while (true) {
		    StructureInfo si;
            Structure s;

            s = Structure_Find(find);
            if (s == null) return;
            if (s.o.type == STRUCTURE_SLAB_1x1 || s.o.type == STRUCTURE_SLAB_2x2 || s.o.type == STRUCTURE_WALL) continue;

            si = g_table_structureInfo[s.o.type];

            s.hitpointsMax = si.o.hitpoints * power / 256;
            s.hitpointsMax = Math.max(s.hitpointsMax, si.o.hitpoints / 2);

            if (s.hitpointsMax >= s.o.hitpoints) continue;
            Structure_Damage(s, 1, 0);
        }
    }

    /**
     * Set the state for the given structure.
     *
     * @param s The structure to set the state of.
     * @param state The new sate value.
     */
    public static void Structure_SetState(Structure s, int state) {
        if (s == null) return;
        s.state = state;

        Structure_UpdateMap(s);
    }

    /**
     * Get the structure on the given packed tile.
     *
     * @param packed The packed tile to get the structure from.
     * @return The structure.
     */
    public static Structure Structure_Get_ByPackedTile(int packed) {
        Tile tile;

        if (Tile_IsOutOfMap(packed)) return null;

        tile = g_map[packed];
        if (!tile.hasStructure) return null;
        return Structure_Get_ByIndex(tile.index - 1);
    }

    /**
     * Get a bitmask of all built structure types for the given House.
     *
     * @param h The house to get built structures for.
     * @return The bitmask.
     */
    public static long Structure_GetStructuresBuilt(House h) {
        PoolFindStruct find = new PoolFindStruct();
        long result;

        if (h == null) return 0;

        result = 0;
        find.houseID = h.index;
        find.index   = 0xFFFF;
        find.type    = 0xFFFF;

        /* Recount windtraps after capture or loading old saved games. */
        h.windtrapCount = 0;

        while (true) {
            Structure s;

            s = Structure_Find(find);
            if (s == null) break;
            if (s.o.flags.isNotOnMap) continue;
            if (s.o.type == STRUCTURE_SLAB_1x1 || s.o.type == STRUCTURE_SLAB_2x2 || s.o.type == STRUCTURE_WALL) continue;
            result |= 1 << s.o.type;

            if (s.o.type == STRUCTURE_WINDTRAP) h.windtrapCount++;
        }

        return result;
    }

    /**
     * Checks if the given position is a valid location for the given structure type.
     *
     * @param position The (packed) tile to check.
     * @param type The structure type to check the position for.
     * @return 0 if the position is not valid, 1 if the position is valid and have enough slabs, <0 if the position is valid but miss some slabs.
     */
    public static int Structure_IsValidBuildLocation(int position, int type) {
	    StructureInfo si;
        int[] layoutTile;
        int i;
        int neededSlabs;
        boolean isValid;
        int curPos;

        si = g_table_structureInfo[type];
        layoutTile = g_table_structure_layoutTiles[si.layout];

        isValid = true;
        neededSlabs = 0;
        for (i = 0; i < g_table_structure_layoutTileCount[si.layout]; i++) {
            int lst;

            curPos = position + layoutTile[i];

            lst = Map_GetLandscapeType(curPos);

            if (g_debugScenario) {
                if (!g_table_landscapeInfo[lst].isValidForStructure2) {
                    isValid = false;
                    break;
                }
            } else {
                if (!Map_IsValidPosition(curPos)) {
                    isValid = false;
                    break;
                }

                if (si.o.flags.notOnConcrete) {
                    if (!g_table_landscapeInfo[lst].isValidForStructure2 && g_validateStrictIfZero == 0) {
                        isValid = false;
                        break;
                    }
                } else {
                    if (!g_table_landscapeInfo[lst].isValidForStructure && g_validateStrictIfZero == 0) {
                        isValid = false;
                        break;
                    }
                    if (lst != LST_CONCRETE_SLAB) neededSlabs++;
                }
            }

            if (Object_GetByPackedTile(curPos) != null) {
                isValid = false;
                break;
            }
        }

        if (g_validateStrictIfZero == 0 && isValid && type != STRUCTURE_CONSTRUCTION_YARD && !g_debugScenario) {
            isValid = false;
            for (i = 0; i < 16; i++) {
                int offset, lst;
                Structure s;

                offset = g_table_structure_layoutTilesAround[si.layout][i];
                if (offset == 0) break;

                curPos = position + offset;
                s = Structure_Get_ByPackedTile(curPos);
                if (s != null) {
                    if (s.o.houseID != g_playerHouseID) continue;
                    isValid = true;
                    break;
                }

                lst = Map_GetLandscapeType(curPos);
                if (lst != LST_CONCRETE_SLAB && lst != LST_WALL) continue;
                if (g_map[curPos].houseID != g_playerHouseID) continue;

                isValid = true;
                break;
            }
        }

        if (!isValid) return 0;
        if (neededSlabs == 0) return 1;
        return -neededSlabs;
    }

    /**
     * Activate the special weapon of a house.
     *
     * @param s The structure which launches the weapon. Has to be the Palace.
     */
    public static void Structure_ActivateSpecial(Structure s) {
        if (s == null) return;
        if (s.o.type != STRUCTURE_PALACE) return;

        House h = House_Get_ByIndex(s.o.houseID);
        if (!h.flags.used) return;

        switch (g_table_houseInfo[s.o.houseID].specialWeapon) {
            case HOUSE_WEAPON_MISSILE: {
                Tile32 position = new Tile32();

                position.x = 0xFFFF;
                position.y = 0xFFFF;

                g_validateStrictIfZero++;
                Unit u = Unit_Create(UNIT_INDEX_INVALID, UNIT_MISSILE_HOUSE, s.o.houseID, position, Tools_Random_256());
                g_validateStrictIfZero--;

                g_unitHouseMissile = u;
                if (u == null) break;

                s.countDown = g_table_houseInfo[s.o.houseID].specialCountDown;

                if (!h.flags.human) {
                    PoolFindStruct find = new PoolFindStruct();
                    find.houseID = HOUSE_INVALID;
                    find.type    = 0xFFFF;
                    find.index   = 0xFFFF;

                    /* For the AI, try to find the first structure which is not ours, and launch missile to there */
                    while (true) {
                        Structure sf = Structure_Find(find);
                        if (sf == null) break;
                        if (sf.o.type == STRUCTURE_SLAB_1x1 || sf.o.type == STRUCTURE_SLAB_2x2 || sf.o.type == STRUCTURE_WALL) continue;

                        if (House_AreAllied(s.o.houseID, sf.o.houseID)) continue;

                        Unit_LaunchHouseMissile(Tile_PackTile(sf.o.position));

                        return;
                    }

                    /* We failed to find a target, so remove the missile */
                    Unit_Free(u);
                    g_unitHouseMissile = null;

                    return;
                }

                /* Give the user 7 seconds to select their target */
                g_houseMissileCountdown = 7;

                GUI_ChangeSelectionType(SELECTIONTYPE_TARGET);
            } break;

            case HOUSE_WEAPON_FREMEN: {
                /* Find a random location to appear */
                int location = Map_FindLocationTile(4, HOUSE_INVALID);

                for (int i = 0; i < 5; i++) {
                    Tools_Random_256();

                    Tile32 position = Tile_UnpackTile(location);
                    position = Tile_MoveByRandom(position, 32, true);

                    int orientation = Tools_RandomLCG_Range(0, 3);
                    int unitType = (orientation == 1) ? UNIT_TROOPER : UNIT_TROOPERS;

                    g_validateStrictIfZero++;
                    Unit u = Unit_Create(UNIT_INDEX_INVALID, (int)unitType, HOUSE_FREMEN, position, (int)orientation);
                    g_validateStrictIfZero--;

                    if (u == null) continue;

                    Unit_SetAction(u, ACTION_HUNT);
                }

                s.countDown = g_table_houseInfo[s.o.houseID].specialCountDown;
            } break;

            case HOUSE_WEAPON_SABOTEUR: {
                /* Find a spot next to the structure */
                int position = Structure_FindFreePosition(s, false);

                /* If there is no spot, reset countdown */
                if (position == 0) {
                    s.countDown = 1;
                    return;
                }

                g_validateStrictIfZero++;
                Unit u = Unit_Create(UNIT_INDEX_INVALID, UNIT_SABOTEUR, s.o.houseID, Tile_UnpackTile(position), Tools_Random_256());
                g_validateStrictIfZero--;

                if (u == null) return;

                Unit_SetAction(u, ACTION_SABOTAGE);

                s.countDown = g_table_houseInfo[s.o.houseID].specialCountDown;
            } break;

            default: break;
        }

        if (s.o.houseID == g_playerHouseID) {
            GUI_Widget_ActionPanel_Draw(true);
        }
    }

    /**
     * Remove the fog around a structure.
     *
     * @param s The Structure.
     */
    public static void Structure_RemoveFog(Structure s) {
        if (s == null || s.o.houseID != g_playerHouseID) return;

        StructureInfo si = g_table_structureInfo[s.o.type];

        Tile32 position = s.o.position;

        /* ENHANCEMENT -- Fog is removed around the top left corner instead of the center of a structure. */
        if (g_dune2_enhanced) {
            position.x += 256 * (g_table_structure_layoutSize[si.layout].width  - 1) / 2;
            position.y += 256 * (g_table_structure_layoutSize[si.layout].height - 1) / 2;
        }

        Tile_RemoveFogInRadius(position, si.o.fogUncoverRadius);
    }

    /**
     * Handles destroying of a structure.
     *
     * @param s The Structure.
     */
    static void Structure_Destroy(Structure s) {
        if (s == null) return;

        if (g_debugScenario) {
            Structure_Remove(s);
            return;
        }

        s.o.script.variables[0] = 1;
        s.o.flags.allocated = false;
        s.o.flags.repairing = false;
        s.o.script.delay = 0;

        Script_Reset(s.o.script, g_scriptStructure);
        Script_Load(s.o.script, s.o.type);

        Voice_PlayAtTile(44, s.o.position);

        int linkedID = s.o.linkedID;

        if (linkedID != 0xFF) {
            if (s.o.type == STRUCTURE_CONSTRUCTION_YARD) {
                Structure_Destroy(Structure_Get_ByIndex(linkedID));
                s.o.linkedID = 0xFF;
            } else {
                while (linkedID != 0xFF) {
                    Unit u = Unit_Get_ByIndex(linkedID);

                    linkedID = u.o.linkedID;

                    Unit_Remove(u);
                }
            }
        }

        House h = House_Get_ByIndex(s.o.houseID);
        StructureInfo si = g_table_structureInfo[s.o.type];

        h.credits -= (h.creditsStorage == 0) ? h.credits : Math.min(h.credits, (h.credits * 256 / h.creditsStorage) * si.creditsStorage / 256);

        if (s.o.houseID != g_playerHouseID) h.credits += si.o.buildCredits + (g_campaignID > 7 ? si.o.buildCredits / 2 : 0);

        if (s.o.type != STRUCTURE_WINDTRAP) return;

        h.windtrapCount--;
    }

    /**
     * Damage the structure, and bring the surrounding to an explosion if needed.
     *
     * @param s The structure to damage.
     * @param damage The damage to deal to the structure.
     * @param range The range in which an explosion should be possible.
     * @return True if and only if the structure is now destroyed.
     */
    public static boolean Structure_Damage(Structure s, int damage, int range) {
        if (s == null) return false;
        if (damage == 0) return false;
        if (s.o.script.variables[0] == 1) return false;

        StructureInfo si = g_table_structureInfo[s.o.type];

        if (s.o.hitpoints >= damage) {
            s.o.hitpoints -= damage;
        } else {
            s.o.hitpoints = 0;
        }

        if (s.o.hitpoints == 0) {
            int score = si.o.buildCredits / 100;
            if (score < 1) score = 1;

            if (House_AreAllied(g_playerHouseID, s.o.houseID)) {
                g_scenario.destroyedAllied++;
                g_scenario.score -= score;
            } else {
                g_scenario.destroyedEnemy++;
                g_scenario.score += score;
            }

            Structure_Destroy(s);

            if (g_playerHouseID == s.o.houseID) {
                int index;

                switch (s.o.houseID) {
                    case HOUSE_HARKONNEN: index = 22; break;
                    case HOUSE_ATREIDES:  index = 23; break;
                    case HOUSE_ORDOS:     index = 24; break;
                    default: index = 0xFFFF; break;
                }

                Sound_Output_Feedback(index);
            } else {
                Sound_Output_Feedback(21);
            }

            Structure_UntargetMe(s);
            return true;
        }

        if (range == 0) return false;

        Map_MakeExplosion(EXPLOSION_IMPACT_LARGE, Tile_AddTileDiff(s.o.position, g_table_structure_layoutTileDiff[si.layout]), 0, 0);
        return false;
    }

    /**
     * Check wether the given structure is upgradable.
     *
     * @param s The Structure to check.
     * @return True if and only if the structure is upgradable.
     */
    public static boolean Structure_IsUpgradable(Structure s) {
        if (s == null) return false;

        StructureInfo si = g_table_structureInfo[s.o.type];

        if (s.o.houseID == HOUSE_HARKONNEN && s.o.type == STRUCTURE_HIGH_TECH) return false;
        if (s.o.houseID == HOUSE_ORDOS && s.o.type == STRUCTURE_HEAVY_VEHICLE && s.upgradeLevel == 1 && si.upgradeCampaign[2] > g_campaignID) return false;

        if (si.upgradeCampaign[s.upgradeLevel] != 0 && si.upgradeCampaign[s.upgradeLevel] <= g_campaignID + 1) {
            if (s.o.type != STRUCTURE_CONSTRUCTION_YARD) return true;
            if (s.upgradeLevel != 1) return true;

            House h = House_Get_ByIndex(s.o.houseID);
            if ((h.structuresBuilt & g_table_structureInfo[STRUCTURE_ROCKET_TURRET].o.structuresRequired) == g_table_structureInfo[STRUCTURE_ROCKET_TURRET].o.structuresRequired) return true;

            return false;
        }

        if (s.o.houseID == HOUSE_HARKONNEN && s.o.type == STRUCTURE_WOR_TROOPER && s.upgradeLevel == 0 && g_campaignID > 3) {
            return true;
        }

        return false;
    }

    private static final int[] wall = new int[] {
        0,  3,  1,  2,  3,  3,  4,  5,  1,  6,  1,  7,  8,  9, 10, 11,
        1, 12,  1, 19,  1, 16,  1, 31,  1, 28,  1, 52,  1, 45,  1, 59,
        3,  3, 13, 20,  3,  3, 22, 32,  3,  3, 13, 53,  3,  3, 38, 60,
        5,  6,  7, 21,  5,  6,  7, 33,  5,  6,  7, 54,  5,  6,  7, 61,
        9,  9,  9,  9, 17, 17, 23, 34,  9,  9,  9,  9, 25, 46, 39, 62,
        11, 12, 11, 12, 13, 18, 13, 35, 11, 12, 11, 12, 13, 47, 13, 63,
        15, 15, 16, 16, 17, 17, 24, 36, 15, 15, 16, 16, 17, 17, 40, 64,
        19, 20, 21, 22, 23, 24, 25, 37, 19, 20, 21, 22, 23, 24, 25, 65,
        27, 27, 27, 27, 27, 27, 27, 27, 14, 29, 14, 55, 26, 48, 41, 66,
        29, 30, 29, 30, 29, 30, 29, 30, 31, 30, 31, 56, 31, 49, 31, 67,
        33, 33, 34, 34, 33, 33, 34, 34, 35, 35, 15, 57, 35, 35, 42, 68,
        37, 38, 39, 40, 37, 38, 39, 40, 41, 42, 43, 58, 41, 42, 43, 69,
        45, 45, 45, 45, 46, 46, 46, 46, 47, 47, 47, 47, 27, 50, 43, 70,
        49, 50, 49, 50, 51, 52, 51, 52, 53, 54, 53, 54, 55, 51, 55, 71,
        57, 57, 58, 58, 59, 59, 60, 60, 61, 61, 62, 62, 63, 63, 44, 72,
        65, 66, 67, 68, 69, 70, 71, 72, 73, 74, 75, 76, 77, 78, 79, 73
    };

    /**
     * Connect walls around the given position.
     *
     * @param position The packed position.
     * @param recurse Whether to recurse.
     * @return True if and only if a change happened.
     */
    public static boolean Structure_ConnectWall(int position, boolean recurse) {
        int bits = 0;
        boolean isDestroyedWall = Map_GetLandscapeType(position) == LST_DESTROYED_WALL;

        for (int i = 0; i < 4; i++) {
		    int curPos = position + g_table_mapDiff[i];

            if (recurse && Map_GetLandscapeType(curPos) == LST_WALL) {
                Structure_ConnectWall(curPos, false);
            }

            if (isDestroyedWall) continue;

            switch (Map_GetLandscapeType(curPos)) {
                case LST_DESTROYED_WALL: bits |= (1 << (i + 4));
                    /* FALL-THROUGH */
                case LST_WALL: bits |= (1 << i);
                    /* FALL-THROUGH */
                default:  break;
            }
        }

        if (isDestroyedWall) return false;

        int tileID = g_wallTileID + wall[bits] + 1;

        Tile tile = g_map[position];
        if (tile.groundTileID == tileID) return false;

        tile.groundTileID = tileID;
        g_mapTileID[position] |= 0x8000;
        Map_Update(position, 0, false);

        return true;
    }

    /**
     * Get the unit linked to this structure, or NULL if there is no.
     * @param s The structure to get the linked unit from.
     * @return The linked unit, or NULL if there was none.
     */
    public static Unit Structure_GetLinkedUnit(Structure s) {
        return Structure_GetLinkedUnit(s.o);
    }

    public static Unit Structure_GetLinkedUnit(GObject o) {
        if (o.linkedID == 0xFF) return null;
        return Unit_Get_ByIndex(o.linkedID);
    }

    /**
     * Untarget the given Structure.
     *
     * @param s The Structure to untarget.
     */
    public static void Structure_UntargetMe(Structure s) {
        int encoded = Tools_Index_Encode(s.o.index, IT_STRUCTURE);

        Object_Script_Variable4_Clear(s.o);

        PoolFindStruct find = new PoolFindStruct();
        find.houseID = HOUSE_INVALID;
        find.index = 0xFFFF;
        find.type = 0xFFFF;

        while (true) {
            Unit u = Unit_Find(find);
            if (u == null) break;

            if (u.targetMove == encoded) u.targetMove = 0;
            if (u.targetAttack == encoded) u.targetAttack = 0;
            if (u.o.script.variables[4] == encoded) Object_Script_Variable4_Clear(u.o);
        }

        find.houseID = HOUSE_INVALID;
        find.index = 0xFFFF;
        find.type = 0xFFFF;

        while (true) {
            Team t = Team_Find(find);
            if (t == null) break;

            if (t.target == encoded) t.target = 0;
        }
    }

    /**
     * Find a free spot for units next to a structure.
     * @param s Structure that needs a free spot.
     * @param checkForSpice Spot should be as close to spice as possible.
     * @return Position of the free spot, or 0 if no free spot available.
     */
    public static int Structure_FindFreePosition(Structure s, boolean checkForSpice) {
        if (s == null) return 0;

        StructureInfo si = g_table_structureInfo[s.o.type];
        int packed = Tile_PackTile(Tile_Center(s.o.position));

        /* Position of the spice, or 0 if not used or if no spice. */
        int spicePacked = (checkForSpice) ? Map_SearchSpice(packed, 10) : 0;
        int bestPacked = 0;

        /* If > 0, distance to the spice from bestPacked. */
        int bestDistance = 0;

        int i = Tools_Random_256() & 0xF;
        for (int j = 0; j < 16; j++, i = (i + 1) & 0xF) {
            int offset = g_table_structure_layoutTilesAround[si.layout][i];
            if (offset == 0) continue;

            int curPacked = packed + offset;
            if (!Map_IsValidPosition(curPacked)) continue;

            int type = Map_GetLandscapeType(curPacked);
            if (type == LST_WALL || type == LST_ENTIRELY_MOUNTAIN || type == LST_PARTIAL_MOUNTAIN) continue;

            Tile t = g_map[curPacked];
            if (t.hasUnit || t.hasStructure) continue;

            if (!checkForSpice) return curPacked;

            if (bestDistance == 0 || Tile_GetDistancePacked(curPacked, spicePacked) < bestDistance) {
                bestPacked = curPacked;
                bestDistance = Tile_GetDistancePacked(curPacked, spicePacked);
            }
        }

        return bestPacked;
    }

    /**
     * Remove the structure from the map, free it, and clean up after it.
     * @param s The structure to remove.
     */
    public static void Structure_Remove(Structure s) {
        if (s == null) return;

        StructureInfo si = g_table_structureInfo[s.o.type];
        int packed = Tile_PackTile(s.o.position);

        for (int i = 0; i < g_table_structure_layoutTileCount[si.layout]; i++) {
            int curPacked = packed + g_table_structure_layoutTiles[si.layout][i];

            Animation_Stop_ByTile(curPacked);

            Tile t = g_map[curPacked];
            t.hasStructure = false;

            if (g_debugScenario) {
                t.groundTileID = g_mapTileID[curPacked] & 0x1FF;
                t.overlayTileID = 0;
            }
        }

        if (!g_debugScenario) {
            Animation_Start(g_table_animation_structure[0], s.o.position, si.layout, s.o.houseID, (int)si.iconGroup);
        }

        House h = House_Get_ByIndex(s.o.houseID);

        for (int i = 0; i < 5; i++) {
            if (h.ai_structureRebuild[i][0] != 0) continue;
            h.ai_structureRebuild[i][0] = s.o.type;
            h.ai_structureRebuild[i][1] = packed;
            break;
        }

        Structure_Free(s);
        Structure_UntargetMe(s);

        h.structuresBuilt = Structure_GetStructuresBuilt(h);

        House_UpdateCreditsStorage(s.o.houseID);

        if (g_debugScenario) return;

        switch (s.o.type) {
            case STRUCTURE_WINDTRAP:
                House_CalculatePowerAndCredit(h);
                break;

            case STRUCTURE_OUTPOST:
                House_UpdateRadarState(h);
                break;

            default: break;
        }
    }

    /**
     * Check if requested structureType can be built on the map with concrete below.
     *
     * @param structureType The type of structure to check for.
     * @param houseID The house to check for.
     * @return True if and only if there are enough slabs available on the map to
     *  build requested structure.
     */
    static boolean Structure_CheckAvailableConcrete(int structureType, int houseID) {
        StructureInfo si = g_table_structureInfo[structureType];

        int tileCount = g_table_structure_layoutTileCount[si.layout];

        if (structureType == STRUCTURE_SLAB_1x1 || structureType == STRUCTURE_SLAB_2x2) {
            return true;
        }

        for (int i = 0; i < 4096; i++) {
            boolean stop = true;

            for (int j = 0; j < tileCount; j++) {
                int packed = i + g_table_structure_layoutTiles[si.layout][j];
                /* XXX -- This can overflow, and we should check for that */

                if (Map_GetLandscapeType(packed) == LST_CONCRETE_SLAB && g_map[packed].houseID == houseID) {
                    continue;
                }

                stop = false;
                break;
            }

            if (stop) {
                return true;
            }
        }

        return false;
    }

    /**
     * Cancel the building of object for given structure.
     *
     * @param s The Structure.
     */
    static void Structure_CancelBuild(Structure s) {
        if (s == null || s.o.linkedID == 0xFF) return;

        GObjectInfo oi;
        if (s.o.type == STRUCTURE_CONSTRUCTION_YARD) {
            Structure s2 = Structure_Get_ByIndex(s.o.linkedID);
            oi = g_table_structureInfo[s2.o.type].o;
            Structure_Free(s2);
        } else {
            Unit u = Unit_Get_ByIndex(s.o.linkedID);
            oi = g_table_unitInfo[u.o.type].o;
            Unit_Free(u);
        }

        House_Get_ByIndex(s.o.houseID).credits += ((oi.buildTime - (s.countDown >> 8)) * 256 / oi.buildTime) * oi.buildCredits / 256;

        s.o.flags.onHold = false;
        s.countDown = 0;
        s.o.linkedID = 0xFF;
    }

    /**
     * Make the given Structure build an object.
     *
     * @param s The Structure.
     * @param objectType The type of the object to build or a special value (0xFFFD, 0xFFFE, 0xFFFF).
     * @return ??.
     */
    public static boolean Structure_BuildObject(Structure s, int objectType) {
	    String str;
        GObject o;

        if (s == null) return false;

        StructureInfo si = g_table_structureInfo[s.o.type];

        if (!si.o.flags.factory) return false;

        Structure_SetRepairingState(s, 0, null);

        if (objectType == 0xFFFD) {
            Structure_SetUpgradingState(s, 1, null);
            return false;
        }

        if (objectType == 0xFFFF || objectType == 0xFFFE) {
            int upgradeCost = 0;

            if (Structure_IsUpgradable(s) && si.o.hitpoints == s.o.hitpoints) {
                upgradeCost = (si.o.buildCredits + (si.o.buildCredits >> 15)) / 2;
            }

            if (upgradeCost != 0 && s.o.type == STRUCTURE_HIGH_TECH && s.o.houseID == HOUSE_HARKONNEN) upgradeCost = 0;
            if (s.o.type == STRUCTURE_STARPORT) upgradeCost = 0;

            long buildable = Structure_GetBuildable(s);

            if (buildable == 0) {
                s.objectType = 0;
                return false;
            }

            if (s.o.type == STRUCTURE_CONSTRUCTION_YARD) {
                int i;

                g_factoryWindowConstructionYard = true;

                for (i = 0; i < STRUCTURE_MAX; i++) {
                    if ((buildable & (1 << i)) == 0) continue;
                    g_table_structureInfo[i].o.available = 1;
                    if (objectType != 0xFFFE) continue;
                    s.objectType = i;
                    return false;
                }
            } else {
                g_factoryWindowConstructionYard = false;

                if (s.o.type == STRUCTURE_STARPORT) {
                    int linkedID = 0xFF;
                    int[] availableUnits = new int[UNIT_MAX];
                    boolean loop;

                    do {
                        loop = false;

                        for (int i = 0; i < UNIT_MAX; i++) {
                            int unitsAtStarport = g_starportAvailable[i];

                            if (unitsAtStarport == 0) {
                                g_table_unitInfo[i].o.available = 0;
                            } else if (unitsAtStarport < 0) {
                                g_table_unitInfo[i].o.available = -1;
                            } else if (unitsAtStarport > availableUnits[i]) {
                                g_validateStrictIfZero++;
                                Unit u = Unit_Allocate(UNIT_INDEX_INVALID, i, s.o.houseID);
                                g_validateStrictIfZero--;

                                if (u != null) {
                                    loop = true;
                                    u.o.linkedID = linkedID;
                                    linkedID = u.o.index & 0xFF;
                                    availableUnits[i]++;
                                    g_table_unitInfo[i].o.available = (int)availableUnits[i];
                                } else if (availableUnits[i] == 0) g_table_unitInfo[i].o.available = -1;
                            }
                        }
                    } while (loop);

                    while (linkedID != 0xFF) {
                        Unit u = Unit_Get_ByIndex(linkedID);
                        linkedID = u.o.linkedID;
                        Unit_Free(u);
                    }
                } else {
                    for (int i = 0; i < UNIT_MAX; i++) {
                        if ((buildable & (1 << i)) == 0) continue;
                        g_table_unitInfo[i].o.available = 1;
                        if (objectType != 0xFFFE) continue;
                        s.objectType = i;
                        return false;
                    }
                }
            }

            if (objectType == 0xFFFF) {
                Sprites_UnloadTiles();

                memmove(g_palette1, g_paletteActive, 256 * 3);

                GUI_ChangeSelectionType(SELECTIONTYPE_MENTAT);

                Timer_SetTimer(TIMER_GAME, false);

                int res = GUI_DisplayFactoryWindow(g_factoryWindowConstructionYard, s.o.type == STRUCTURE_STARPORT ? 1 : 0, upgradeCost);

                Timer_SetTimer(TIMER_GAME, true);

                Sprites_LoadTiles();

                GFX_SetPalette(g_palette1);

                GUI_ChangeSelectionType(SELECTIONTYPE_STRUCTURE);

                if (res == FACTORY_RESUME) return false;

                if (res == FACTORY_UPGRADE) {
                    Structure_SetUpgradingState(s, 1, null);
                    return false;
                }

                if (res == FACTORY_BUY) {
                    House h;
                    int i;

                    h = House_Get_ByIndex(s.o.houseID);

                    for (i = 0; i < 25; i++) {
                        Unit u;

                        if (g_factoryWindowItems[i].amount == 0) continue;
                        objectType = g_factoryWindowItems[i].objectType;

                        if (s.o.type != STRUCTURE_STARPORT) {
                            Structure_CancelBuild(s);

                            s.objectType = objectType;

                            if (!g_factoryWindowConstructionYard) continue;

                            if (Structure_CheckAvailableConcrete(objectType, s.o.houseID)) continue;

                            if (GUI_DisplayHint(STR_THERE_ISNT_ENOUGH_OPEN_CONCRETE_TO_PLACE_THIS_STRUCTURE_YOU_MAY_PROCEED_BUT_WITHOUT_ENOUGH_CONCRETE_THE_BUILDING_WILL_NEED_REPAIRS, g_table_structureInfo[objectType].o.spriteID) == 0) continue;

                            s.objectType = objectType;

                            return false;
                        }

                        g_validateStrictIfZero++;
                        {
                            Tile32 tile = new Tile32();
                            tile.x = 0xFFFF;
                            tile.y = 0xFFFF;
                            u = Unit_Create(UNIT_INDEX_INVALID, (int)objectType, s.o.houseID, tile, 0);
                        }
                        g_validateStrictIfZero--;

                        if (u == null) {
                            h.credits += g_table_unitInfo[UNIT_CARRYALL].o.buildCredits;
                            if (s.o.houseID != g_playerHouseID) continue;
                            GUI_DisplayText(String_Get_ByIndex(STR_UNABLE_TO_CREATE_MORE), 2);
                            continue;
                        }

                        g_structureIndex = s.o.index;

                        if (h.starportTimeLeft == 0) h.starportTimeLeft = g_table_houseInfo[h.index].starportDeliveryTime;

                        u.o.linkedID = h.starportLinkedID & 0xFF;
                        h.starportLinkedID = u.o.index;

                        g_starportAvailable[objectType]--;
                        if (g_starportAvailable[objectType] <= 0) g_starportAvailable[objectType] = -1;

                        g_factoryWindowItems[i].amount--;
                        if (g_factoryWindowItems[i].amount != 0) i--;
                    }
                }
            } else {
                s.objectType = objectType;
            }
        }

        if (s.o.type == STRUCTURE_STARPORT) return true;

        if (s.objectType != objectType) Structure_CancelBuild(s);

        if (s.o.linkedID != 0xFF || objectType == 0xFFFF) return false;

        GObjectInfo oi;
        if (s.o.type != STRUCTURE_CONSTRUCTION_YARD) {
            Tile32 tile = new Tile32();
            tile.x = 0xFFFF;
            tile.y = 0xFFFF;

            oi = g_table_unitInfo[objectType].o;
            o = Unit_Create(UNIT_INDEX_INVALID, objectType, s.o.houseID, tile, 0).o;
            str = String_Get_ByIndex(g_table_unitInfo[objectType].o.stringID_full);
        } else {
            oi = g_table_structureInfo[objectType].o;
            o = Structure_Create(STRUCTURE_INDEX_INVALID, objectType, s.o.houseID, 0xFFFF).o;
            str = String_Get_ByIndex(g_table_structureInfo[objectType].o.stringID_full);
        }

        s.o.flags.onHold = false;

        if (o != null) {
            s.o.linkedID = o.index & 0xFF;
            s.objectType = objectType;
            s.countDown = oi.buildTime << 8;

            Structure_SetState(s, STRUCTURE_STATE_BUSY);

            if (s.o.houseID != g_playerHouseID) return true;

            GUI_DisplayText(String_Get_ByIndex(STR_PRODUCTION_OF_S_HAS_STARTED), 2, str);

            return true;
        }

        if (s.o.houseID != g_playerHouseID) return false;

        GUI_DisplayText(String_Get_ByIndex(STR_UNABLE_TO_CREATE_MORE), 2);

        return false;
    }

    /**
     * Sets or toggle the upgrading state of the given Structure.
     *
     * @param s The Structure.
     * @param state The upgrading state, -1 to toggle.
     * @param w The widget.
     * @return True if and only if the state changed.
     */
    public static boolean Structure_SetUpgradingState(Structure s, int state, Widget w) {
        boolean ret = false;

        if (s == null) return false;

        if (state == -1) state = s.o.flags.upgrading ? 0 : 1;

        if (state == 0 && s.o.flags.upgrading) {
            if (s.o.houseID == g_playerHouseID) {
                GUI_DisplayText(String_Get_ByIndex(STR_UPGRADING_STOPS), 2);
            }

            s.o.flags.upgrading = false;
            s.o.flags.onHold = false;

            GUI_Widget_MakeNormal(w, false);

            ret = true;
        }

        if (state == 0 || s.o.flags.upgrading || s.upgradeTimeLeft == 0) return ret;

        if (s.o.houseID == g_playerHouseID) {
            GUI_DisplayText(String_Get_ByIndex(STR_UPGRADING_STARTS), 2);
        }

        s.o.flags.onHold = true;
        s.o.flags.repairing = false;
        s.o.flags.upgrading = true;

        GUI_Widget_MakeSelected(w, false);

        return true;
    }

    /**
     * Sets or toggle the repairing state of the given Structure.
     *
     * @param s The Structure.
     * @param state The repairing state, -1 to toggle.
     * @param w The widget.
     * @return True if and only if the state changed.
     */
    static boolean Structure_SetRepairingState(Structure s, int state, Widget w) {
        boolean ret = false;

        if (s == null) return false;

        /* ENHANCEMENT -- If a structure gets damaged during upgrading, pressing the "Upgrading" button silently starts the repair of the structure, and doesn't cancel upgrading. */
        if (g_dune2_enhanced && s.o.flags.upgrading) return false;

        if (!s.o.flags.allocated) state = 0;

        if (state == -1) state = s.o.flags.repairing ? 0 : 1;

        if (state == 0 && s.o.flags.repairing) {
            if (s.o.houseID == g_playerHouseID) {
                GUI_DisplayText(String_Get_ByIndex(STR_REPAIRING_STOPS), 2);
            }

            s.o.flags.repairing = false;
            s.o.flags.onHold = false;

            GUI_Widget_MakeNormal(w, false);

            ret = true;
        }

        if (state == 0 || s.o.flags.repairing || s.o.hitpoints == g_table_structureInfo[s.o.type].o.hitpoints) {
            return ret;
        }

        if (s.o.houseID == g_playerHouseID) {
            GUI_DisplayText(String_Get_ByIndex(STR_REPAIRING_STARTS), 2);
        }

        s.o.flags.onHold = true;
        s.o.flags.repairing = true;

        GUI_Widget_MakeSelected(w, false);

        return true;
    }

    /**
     * Update the map with the right data for this structure.
     * @param s The structure to update on the map.
     */
    public static void Structure_UpdateMap(Structure s) {
	    StructureInfo si;
        int layoutSize;
        int[] layout;
        int iconMap;
        int i;

        if (s == null) return;
        if (!s.o.flags.used) return;
        if (s.o.flags.isNotOnMap) return;

        si = g_table_structureInfo[s.o.type];

        layout = g_table_structure_layoutTiles[si.layout];
        layoutSize = g_table_structure_layoutTileCount[si.layout];

        iconMap = g_iconMap[g_iconMap[si.iconGroup] + layoutSize + layoutSize];

        for (i = 0; i < layoutSize; i++) {
            int position;
            Tile t;

            position = Tile_PackTile(s.o.position) + layout[i];

            t = g_map[position];
            t.houseID = s.o.houseID;
            t.hasStructure = true;
            t.index = s.o.index + 1;

            t.groundTileID = iconMap[i] + s.rotationSpriteDiff;

            if (Tile_IsUnveiled(t.overlayTileID)) t.overlayTileID = 0;

            Map_Update(position, 0, false);
        }

        s.o.flags.isDirty = true;

        if (s.state >= STRUCTURE_STATE_IDLE) {
            int animationIndex = (s.state > STRUCTURE_STATE_READY) ? STRUCTURE_STATE_READY : s.state;

            if (si.animationIndex[animationIndex] == 0xFF) {
                Animation_Start(null, s.o.position, si.layout, s.o.houseID, (int)si.iconGroup);
            } else {
                int animationID = si.animationIndex[animationIndex];

                assert(animationID < 29);
                Animation_Start(g_table_animation_structure[animationID], s.o.position, si.layout, s.o.houseID, (int)si.iconGroup);
            }
        } else {
            Animation_Start(g_table_animation_structure[1], s.o.position, si.layout, s.o.houseID, (int)si.iconGroup);
        }
    }

    public static long Structure_GetBuildable(Structure s) {
        if (s == null) return 0;

        StructureInfo si = g_table_structureInfo[s.o.type];

        long structuresBuilt = House_Get_ByIndex(s.o.houseID).structuresBuilt;

        long ret = 0;
        switch (s.o.type) {
            case STRUCTURE_LIGHT_VEHICLE:
            case STRUCTURE_HEAVY_VEHICLE:
            case STRUCTURE_HIGH_TECH:
            case STRUCTURE_WOR_TROOPER:
            case STRUCTURE_BARRACKS:
                for (int i = 0; i < UNIT_MAX; i++) {
                    g_table_unitInfo[i].o.available = 0;
                }

                for (int i = 0; i < 8; i++) {
                    int upgradeLevelRequired;
                    int unitType = si.buildableUnits[i];

                    if (unitType == UNIT_INVALID) continue;

                    if (unitType == UNIT_TRIKE && s.creatorHouseID == HOUSE_ORDOS) unitType = UNIT_RAIDER_TRIKE;

                    UnitInfo ui = g_table_unitInfo[unitType];
                    upgradeLevelRequired = ui.o.upgradeLevelRequired;

                    if (unitType == UNIT_SIEGE_TANK && s.creatorHouseID == HOUSE_ORDOS) upgradeLevelRequired--;

                    if ((structuresBuilt & ui.o.structuresRequired) != ui.o.structuresRequired) continue;
                    if ((ui.o.availableHouse & (1 << s.creatorHouseID)) == 0) continue;

                    if (s.upgradeLevel >= upgradeLevelRequired) {
                        ui.o.available = 1;

                        ret |= (1 << unitType);
                        continue;
                    }

                    if (s.upgradeTimeLeft != 0 && s.upgradeLevel + 1 >= upgradeLevelRequired) {
                        ui.o.available = -1;
                    }
                }
                return ret;

            case STRUCTURE_CONSTRUCTION_YARD:
                for (int i = 0; i < STRUCTURE_MAX; i++) {
                    StructureInfo localsi = g_table_structureInfo[i];

                    localsi.o.available = 0;

                    int availableCampaign = localsi.o.availableCampaign;
                    long structuresRequired = localsi.o.structuresRequired;

                    if (i == STRUCTURE_WOR_TROOPER && s.o.houseID == HOUSE_HARKONNEN && g_campaignID >= 1) {
                        structuresRequired &= ~(1 << STRUCTURE_BARRACKS);
                        availableCampaign = 2;
                    }

                    if ((structuresBuilt & structuresRequired) == structuresRequired || s.o.houseID != g_playerHouseID) {
                        if (s.o.houseID != HOUSE_HARKONNEN && i == STRUCTURE_LIGHT_VEHICLE) {
                            availableCampaign = 2;
                        }

                        if (g_campaignID >= availableCampaign - 1 && (localsi.o.availableHouse & (1 << s.o.houseID)) != 0) {
                            if (s.upgradeLevel >= localsi.o.upgradeLevelRequired || s.o.houseID != g_playerHouseID) {
                                localsi.o.available = 1;

                                ret |= (1 << i);
                            } else if (s.upgradeTimeLeft != 0 && s.upgradeLevel + 1 >= localsi.o.upgradeLevelRequired) {
                                localsi.o.available = -1;
                            }
                        }
                    }
                }
                return ret;

            case STRUCTURE_STARPORT:
                return -1;

            default:
                return 0;
        }
    }

    /**
     * The house is under attack in the form of a structure being hit.
     * @param houseID The house who is being attacked.
     */
    public static void Structure_HouseUnderAttack(int houseID) {
        PoolFindStruct find = new PoolFindStruct();
        House h = House_Get_ByIndex(houseID);

        if (houseID != g_playerHouseID && h.flags.doneFullScaleAttack) return;
        h.flags.doneFullScaleAttack = true;

        if (h.flags.human) {
            if (h.timerStructureAttack != 0) return;

            Sound_Output_Feedback(48);

            h.timerStructureAttack = 8;
            return;
        }

        /* ENHANCEMENT -- Dune2 originally only searches for units with type 0 (Carry-all). In result, the rest of this function does nothing. */
        if (!g_dune2_enhanced) return;

        find.houseID = houseID;
        find.index = 0xFFFF;
        find.type = 0xFFFF;

        while (true) {
            Unit u = Unit_Find(find);
            if (u == null) break;

            UnitInfo ui = g_table_unitInfo[u.o.type];

            if (ui.bulletType == UNIT_INVALID) continue;

            /* XXX -- Dune2 does something odd here. What was their intention? */
            if ((u.actionID == ACTION_GUARD && u.actionID == ACTION_AMBUSH) || u.actionID == ACTION_AREA_GUARD) {
                Unit_SetAction(u, ACTION_HUNT);
            }
        }
    }

    /**
     * Find the next object to build.
     * @param s The structure in which we can build something.
     * @return The type (either UnitType or StructureType) of what we should build next.
     */
    static int Structure_AI_PickNextToBuild(Structure s) {
        PoolFindStruct find = new PoolFindStruct();

        if (s == null) return 0xFFFF;

        House h = House_Get_ByIndex(s.o.houseID);
        long buildable = Structure_GetBuildable(s);

        if (s.o.type == STRUCTURE_CONSTRUCTION_YARD) {
            for (int i = 0; i < 5; i++) {
                int type = h.ai_structureRebuild[i][0];

                if (type == 0) continue;
                if ((buildable & (1 << type)) == 0) continue;

                return type;
            }

            return 0xFFFF;
        }

        if (s.o.type == STRUCTURE_HIGH_TECH) {
            find.houseID = s.o.houseID;
            find.index = 0xFFFF;
            find.type = UNIT_CARRYALL;

            while (true) {
                Unit u = Unit_Find(find);
                if (u == null) break;

                buildable &= ~FLAG_UNIT_CARRYALL;
            }
        }

        if (s.o.type == STRUCTURE_HEAVY_VEHICLE) {
            buildable &= ~FLAG_UNIT_HARVESTER;
            buildable &= ~FLAG_UNIT_MCV;
        }

        int type = 0xFFFF;
        for (int i = 0; i < UNIT_MAX; i++) {
            if ((buildable & (1 << i)) == 0) continue;

            if ((Tools_Random_256() % 4) == 0) type = i;

            if (type != 0xFFFF) {
                if (g_table_unitInfo[i].o.priorityBuild <= g_table_unitInfo[type].o.priorityBuild) continue;
            }

            type = i;
        }

        return type;
    }
}
