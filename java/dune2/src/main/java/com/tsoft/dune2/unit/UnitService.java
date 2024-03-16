package com.tsoft.dune2.unit;

import com.tsoft.dune2.house.House;
import com.tsoft.dune2.map.Tile;
import com.tsoft.dune2.pool.PoolFindStruct;
import com.tsoft.dune2.structure.Structure;
import com.tsoft.dune2.structure.StructureInfo;
import com.tsoft.dune2.team.Team;
import com.tsoft.dune2.tile.Tile32;

import static com.tsoft.dune2.animation.AnimationService.Animation_Start;
import static com.tsoft.dune2.explosion.ExplosionType.EXPLOSION_SAND_BURST;
import static com.tsoft.dune2.gobject.GObjectService.*;
import static com.tsoft.dune2.gui.GuiService.GUI_ChangeSelectionType;
import static com.tsoft.dune2.gui.GuiService.GUI_DisplayText;
import static com.tsoft.dune2.gui.SelectionType.*;
import static com.tsoft.dune2.house.HouseService.*;
import static com.tsoft.dune2.house.HouseType.*;
import static com.tsoft.dune2.map.LandscapeType.*;
import static com.tsoft.dune2.map.MapService.*;
import static com.tsoft.dune2.opendune.OpenDuneService.g_debugScenario;
import static com.tsoft.dune2.opendune.OpenDuneService.g_dune2_enhanced;
import static com.tsoft.dune2.pool.PoolHouseService.House_Find;
import static com.tsoft.dune2.pool.PoolHouseService.House_Get_ByIndex;
import static com.tsoft.dune2.pool.PoolStructureService.Structure_Find;
import static com.tsoft.dune2.pool.PoolTeamService.Team_Get_ByIndex;
import static com.tsoft.dune2.pool.PoolUnitService.UNIT_INDEX_INVALID;
import static com.tsoft.dune2.script.ScriptService.*;
import static com.tsoft.dune2.strings.Strings.*;
import static com.tsoft.dune2.structure.StructureService.*;
import static com.tsoft.dune2.table.TableStructureInfo.g_table_structureInfo;
import static com.tsoft.dune2.structure.StructureState.STRUCTURE_STATE_BUSY;
import static com.tsoft.dune2.structure.StructureState.STRUCTURE_STATE_READY;
import static com.tsoft.dune2.structure.StructureType.*;
import static com.tsoft.dune2.table.TableStructureInfo.g_table_structure_layoutTileDiff;
import static com.tsoft.dune2.table.TableUnitInfo.g_table_unitInfo;
import static com.tsoft.dune2.tile.TileService.*;
import static com.tsoft.dune2.tools.IndexType.*;
import static com.tsoft.dune2.tools.ToolsService.*;
import static com.tsoft.dune2.unit.ActionType.*;
import static com.tsoft.dune2.unit.MovementType.*;
import static com.tsoft.dune2.unit.UnitType.*;

public class UnitService {

    static long s_tickUnitMovement = 0;  /*!< Indicates next time the Movement function is executed. */
    static long s_tickUnitRotation = 0;  /*!< Indicates next time the Rotation function is executed. */
    static long s_tickUnitBlinking = 0;  /*!< Indicates next time the Blinking function is executed. */
    static long s_tickUnitUnknown4 = 0;  /*!< Indicates next time the Unknown4 function is executed. */
    static long s_tickUnitScript = 0;    /*!< Indicates next time the Script function is executed. */
    static long s_tickUnitUnknown5 = 0;  /*!< Indicates next time the Unknown5 function is executed. */
    static long s_tickUnitDeviation = 0; /*!< Indicates next time the Deviation function is executed. */

    private static Unit g_unitActive = null;
    public static Unit g_unitHouseMissile = null;
    public static Unit g_unitSelected = null;

    public static int g_dirtyUnitCount = 0;
    public static int g_dirtyAirUnitCount = 0;

    /**
     * Number of units of each type available at the starport.
     * \c 0 means not available, \c -1 means \c 0 units, \c >0 means that number of units available.
     */
    public static int[] g_starportAvailable = new int[UNIT_MAX];

    /**
     * Rotate a unit (or his top).
     *
     * @param unit  The Unit to operate on.
     * @param level 0 = base, 1 = top (turret etc).
     */
    static void Unit_Rotate(Unit unit, int level) {
        int target;
        int current;
        int newCurrent;
        int diff;

        assert (level == 0 || level == 1);

        if (unit.orientation[level].speed == 0) return;

        target = unit.orientation[level].target;
        current = unit.orientation[level].current;
        diff = target - current;

        if (diff > 128) diff -= 256;
        if (diff < -128) diff += 256;
        diff = Math.abs(diff);

        newCurrent = current + unit.orientation[level].speed;

        if (Math.abs(unit.orientation[level].speed) >= diff) {
            unit.orientation[level].speed = 0;
            newCurrent = target;
        }

        unit.orientation[level].current = newCurrent;

        if (Orientation_Orientation256ToOrientation16(newCurrent) == Orientation_Orientation256ToOrientation16(current) && Orientation_Orientation256ToOrientation8(newCurrent) == Orientation_Orientation256ToOrientation8(current))
            return;

        Unit_UpdateMap(2, unit);
    }

    static void Unit_MovementTick(Unit unit) {
        int speed;

        if (unit.speed == 0) return;

        speed = unit.speedRemainder;

        /* Units in the air don't feel the effect of gameSpeed */
        if (g_table_unitInfo[unit.o.type].movementType != MOVEMENT_WINGER) {
            speed += Tools_AdjustToGameSpeed(unit.speedPerTick, 1, 255, false);
        } else {
            speed += unit.speedPerTick;
        }

        if ((speed & 0xFF00) != 0) {
            Unit_Move(unit, Math.min(unit.speed * 16, Tile_GetDistance(unit.o.position, unit.currentDestination) + 16));
        }

        unit.speedRemainder = speed & 0xFF;
    }

    /**
     * Loop over all units, performing various of tasks.
     */
    void GameLoop_Unit() {
        PoolFindStruct find = new PoolFindStruct();
        boolean tickMovement = false;
        boolean tickRotation = false;
        boolean tickBlinking = false;
        boolean tickUnknown4 = false;
        boolean tickScript = false;
        boolean tickUnknown5 = false;
        boolean tickDeviation = false;

        if (g_debugScenario) return;

        if (s_tickUnitMovement <= g_timerGame) {
            tickMovement = true;
            s_tickUnitMovement = g_timerGame + 3;
        }

        if (s_tickUnitRotation <= g_timerGame) {
            tickRotation = true;
            s_tickUnitRotation = g_timerGame + Tools_AdjustToGameSpeed(4, 2, 8, true);
        }

        if (s_tickUnitBlinking <= g_timerGame) {
            tickBlinking = true;
            s_tickUnitBlinking = g_timerGame + 3;
        }

        if (s_tickUnitUnknown4 <= g_timerGame) {
            tickUnknown4 = true;
            s_tickUnitUnknown4 = g_timerGame + 20;
        }

        if (s_tickUnitScript <= g_timerGame) {
            tickScript = true;
            s_tickUnitScript = g_timerGame + 5;
        }

        if (s_tickUnitUnknown5 <= g_timerGame) {
            tickUnknown5 = true;
            s_tickUnitUnknown5 = g_timerGame + 5;
        }

        if (s_tickUnitDeviation <= g_timerGame) {
            tickDeviation = true;
            s_tickUnitDeviation = g_timerGame + 60;
        }

        find.houseID = HOUSE_INVALID;
        find.index = 0xFFFF;
        find.type = 0xFFFF;

        while (true) {
		    UnitInfo ui;
            Unit u;

            u = Unit_Find(find);
            if (u == null) break;

            ui = g_table_unitInfo[u.o.type];

            g_scriptCurrentObject = u.o;
            g_scriptCurrentStructure = null;
            g_scriptCurrentUnit = u;
            g_scriptCurrentTeam = null;

            if (u.o.flags.isNotOnMap) continue;

            if (tickUnknown4 && u.targetAttack != 0 && ui.o.flags.hasTurret) {
                Tile32 tile;

                tile = Tools_Index_GetTile(u.targetAttack);

                Unit_SetOrientation(u, Tile_GetDirection(u.o.position, tile), false, 1);
            }

            if (tickMovement) {
                Unit_MovementTick(u);

                if (u.fireDelay != 0) {
                    if (ui.movementType == MOVEMENT_WINGER && !ui.flags.isNormalUnit) {
                        Tile32 tile;

                        tile = u.currentDestination;

                        if (Tools_Index_GetType(u.targetAttack) == IT_UNIT && g_table_unitInfo[Tools_Index_GetUnit(u.targetAttack).o.type].movementType == MOVEMENT_WINGER) {
                            tile = Tools_Index_GetTile(u.targetAttack);
                        }

                        Unit_SetOrientation(u, Tile_GetDirection(u.o.position, tile), false, 0);
                    }

                    u.fireDelay--;
                }
            }

            if (tickRotation) {
                Unit_Rotate(u, 0);
                if (ui.o.flags.hasTurret) Unit_Rotate(u, 1);
            }

            if (tickBlinking && u.blinkCounter != 0) {
                u.blinkCounter--;
                if ((u.blinkCounter % 2) != 0) {
                    u.o.flags.isHighlighted = true;
                } else {
                    u.o.flags.isHighlighted = false;
                }

                Unit_UpdateMap(2, u);
            }

            if (tickDeviation) Unit_Deviation_Decrease(u, 1);

            if (ui.movementType != MOVEMENT_WINGER && Object_GetByPackedTile(Tile_PackTile(u.o.position)) == null) {
                Unit_UpdateMap(1, u);
            }

            if (tickUnknown5) {
                if (u.timer == 0) {
                    if ((ui.movementType == MOVEMENT_FOOT && u.speed != 0) || u.o.flags.isSmoking) {
                        if (u.spriteOffset >= 0) {
                            u.spriteOffset &= 0x3F;
                            u.spriteOffset++;

                            Unit_UpdateMap(2, u);

                            u.timer = ui.animationSpeed / 5;
                            if (u.o.flags.isSmoking) {
                                u.timer = 3;
                                if (u.spriteOffset > 32) {
                                    u.o.flags.isSmoking = false;
                                    u.spriteOffset = 0;
                                }
                            }
                        }
                    }

                    if (u.o.type == UNIT_ORNITHOPTER && u.o.flags.allocated && u.spriteOffset >= 0) {
                        u.spriteOffset &= 0x3F;
                        u.spriteOffset++;

                        Unit_UpdateMap(2, u);

                        u.timer = 1;
                    }

                    if (u.o.type == UNIT_HARVESTER) {
                        if (u.actionID == ACTION_HARVEST || u.o.flags.isSmoking) {
                            u.spriteOffset &= 0x3F;
                            u.spriteOffset++;

                            Unit_UpdateMap(2, u);

                            u.timer = 4;
                        } else {
                            if (u.spriteOffset != 0) {
                                Unit_UpdateMap(2, u);

                                u.spriteOffset = 0;
                            }
                        }
                    }
                } else {
                    u.timer--;
                }
            }

            if (tickScript) {
                if (u.o.script.delay == 0) {
                    if (Script_IsLoaded(u.o.script)) {
                        int opcodesLeft = SCRIPT_UNIT_OPCODES_PER_TICK + 2;
                        if (!ui.o.flags.scriptNoSlowdown && !Map_IsPositionInViewport(u.o.position, null, null)) {
                            opcodesLeft = 3;
                        }

                        u.o.script.variables[3] = g_playerHouseID;

                        for (; opcodesLeft > 0 && u.o.script.delay == 0; opcodesLeft--) {
                            if (!Script_Run(u.o.script))break;
                        }
                    }
                } else {
                    u.o.script.delay--;
                }
            }

            if (u.nextActionID == ACTION_INVALID) continue;
            if (u.currentDestination.x != 0 || u.currentDestination.y != 0) continue;

            Unit_SetAction(u, u.nextActionID);
            u.nextActionID = ACTION_INVALID;
        }
    }

    /**
     * Get the HouseID of a unit. This is not always u.o.houseID, as a unit can be
     * deviated by the Ordos.
     *
     * @param u Unit to get the HouseID of.
     * @return The HouseID of the unit, which might be deviated.
     */
    public static int Unit_GetHouseID(Unit u) {
        if (u.deviated != 0) {
            /* ENHANCEMENT -- Deviated units always belong to Ordos, no matter who did the deviating. */
            if (g_dune2_enhanced) return u.deviatedHouse;
            return HOUSE_ORDOS;
        }
        return u.o.houseID;
    }

    /**
     * Convert the name of a unit to the type value of that unit, or
     * UNIT_INVALID if not found.
     */
    public static int Unit_StringToType(String name) {
        if (name == null) return UNIT_INVALID;

        for (int type = 0; type < UNIT_MAX; type++) {
            if (g_table_unitInfo[type].o.name.equalsIgnoreCase(name)) return type;
        }

        return UNIT_INVALID;
    }

    /**
     * Convert the name of an action to the type value of that action, or
     * ACTION_INVALID if not found.
     */
    static int Unit_ActionStringToType(String name) {
        if (name == null) return ACTION_INVALID;

        for (int type = 0; type < ACTION_MAX; type++) {
            if (g_table_actionInfo[type].name.equalsIgnoreCase(name)) return type;
        }

        return ACTION_INVALID;
    }

    /**
     * Convert the name of a movement to the type value of that movement, or
     * MOVEMENT_INVALID if not found.
     */
    public static int Unit_MovementStringToType(String name) {
        int type;
        if (name == null) return MOVEMENT_INVALID;

        for (type = 0; type < MOVEMENT_MAX; type++) {
            if (g_table_movementTypeName[type].equalsIgnoreCase(name)) return type;
        }

        return MOVEMENT_INVALID;
    }

    /**
     * Create a new Unit.
     *
     * @param index The new index of the Unit, or UNIT_INDEX_INVALID to assign one.
     * @param typeID The type of the new Unit.
     * @param houseID The House of the new Unit.
     * @param position To where on the map this Unit should be transported, or TILE_INVALID for not on the map yet.
     * @param orientation Orientation of the Unit.
     * @return The new created Unit, or NULL if something failed.
     */
    public static Unit Unit_Create(int index, int typeID, int houseID, Tile32 position, int orientation) {
	    UnitInfo ui;
        Unit u;

        if (houseID >= HOUSE_MAX) return null;
        if (typeID >= UNIT_MAX) return null;

        ui = g_table_unitInfo[typeID];
        u = Unit_Allocate(index, typeID, houseID);
        if (u == null) return null;

        u.o.houseID = houseID;

        Unit_SetOrientation(u, orientation, true, 0);
        Unit_SetOrientation(u, orientation, true, 1);

        Unit_SetSpeed(u, 0);

        u.o.position = position;
        u.o.hitpoints = ui.o.hitpoints;
        u.currentDestination.x = 0;
        u.currentDestination.y = 0;
        u.originEncoded = 0x0000;
        u.route[0] = 0xFF;

        if (position.x != 0xFFFF || position.y != 0xFFFF) {
            u.originEncoded = Unit_FindClosestRefinery(u);
            u.targetLast = position;
            u.targetPreLast = position;
        }

        u.o.linkedID = 0xFF;
        u.o.script.delay = 0;
        u.actionID = ACTION_GUARD;
        u.nextActionID = ACTION_INVALID;
        u.fireDelay = 0;
        u.distanceToDestination = 0x7FFF;
        u.targetMove = 0x0000;
        u.amount = 0;
        u.wobbleIndex = 0;
        u.spriteOffset = 0;
        u.blinkCounter = 0;
        u.timer = 0;

        Script_Reset(u.o.script, g_scriptUnit);

        u.o.flags.allocated = true;

        if (ui.movementType == MOVEMENT_TRACKED) {
            if (Tools_Random_256() < g_table_houseInfo[houseID].degradingChance) {
                u.o.flags.degrades = true;
            }
        }

        if (ui.movementType == MOVEMENT_WINGER) {
            Unit_SetSpeed(u, 255);
        } else {
            if ((position.x != 0xFFFF || position.y != 0xFFFF) && Unit_IsTileOccupied(u)) {
                Unit_Free(u);
                return null;
            }
        }

        if ((position.x == 0xFFFF) && (position.y == 0xFFFF)) {
            u.o.flags.isNotOnMap = true;
            return u;
        }

        Unit_UpdateMap(1, u);

        Unit_SetAction(u, (houseID == g_playerHouseID) ? ui.o.actionsPlayer[3] : ui.actionAI);

        return u;
    }

    /**
     * Checks if a Unit is on the map.
     *
     * @param houseID The House of the Unit.
     * @param typeID  The type of the Unit.
     * @return Returns true if and only if a Unit with the given attributes is on the map.
     */
    static boolean Unit_IsTypeOnMap(int houseID, int typeID) {
        int i;

        for (i = 0; i < g_unitFindCount; i++) {
            Unit u;

            u = g_unitFindArray[i];
            if (houseID != HOUSE_INVALID && Unit_GetHouseID(u) != houseID) continue;
            if (typeID != UNIT_INVALID && u.o.type != typeID) continue;
            if (g_validateStrictIfZero == 0 && u.o.flags.isNotOnMap) continue;

            return true;
        }
        return false;
    }

    /**
     * Sets the action the given unit will execute.
     *
     * @param u      The Unit to set the action for.
     * @param action The action.
     */
    public static void Unit_SetAction(Unit u, int action) {
	    ActionInfo ai;

        if (u == null) return;
        if (u.actionID == ACTION_DESTRUCT || u.actionID == ACTION_DIE || action == ACTION_INVALID) return;

        ai = g_table_actionInfo[action];

        switch (ai.switchType) {
            case 0:
                if (u.currentDestination.x != 0 || u.currentDestination.y != 0) {
                    u.nextActionID = action;
                    return;
                }
                /* FALL-THROUGH */
            case 1:
                u.actionID = action;
                u.nextActionID = ACTION_INVALID;
                u.currentDestination.x = 0;
                u.currentDestination.y = 0;
                u.o.script.delay = 0;
                Script_Reset(u.o.script, g_scriptUnit);
                u.o.script.variables[0] = action;
                Script_Load(u.o.script, u.o.type);
                return;

            case 2:
                u.o.script.variables[0] = action;
                Script_LoadAsSubroutine(u.o.script, u.o.type);
                return;

            default:
                return;
        }
    }

    /**
     * Adds the specified unit to the specified team.
     *
     * @param u The unit to add to the team.
     * @param t The team to add the unit to.
     * @return Amount of space left in the team.
     */
    static int Unit_AddToTeam(Unit u, Team t) {
        if (t == null || u == null) return 0;

        u.team = t.index + 1;
        t.members++;

        return t.maxMembers - t.members;
    }

    /**
     * Removes the specified unit from its team.
     *
     * @param u The unit to remove from the team it is in.
     * @return Amount of space left in the team.
     */
    static int Unit_RemoveFromTeam(Unit u) {
        Team t;

        if (u == null) return 0;
        if (u.team == 0) return 0;

        t = Team_Get_ByIndex(u.team - 1);

        t.members--;
        u.team = 0;

        return t.maxMembers - t.members;
    }

    /**
     * Gets the team of the given unit.
     *
     * @param u The unit to get the team of.
     * @return The team.
     */
    static Team Unit_GetTeam(Unit u) {
        if (u == null) return null;
        if (u.team == 0) return null;
        return Team_Get_ByIndex(u.team - 1);
    }

    /**
     * ?? Sorts unit array and count enemy/allied units.
     */
    static void Unit_Sort() {
        House h;
        int i;

        h = g_playerHouse;
        h.unitCountEnemy = 0;
        h.unitCountAllied = 0;

        for (i = 0; i < g_unitFindCount - 1; i++) {
            Unit u1;
            Unit u2;
            int y1;
            int y2;

            u1 = g_unitFindArray[i];
            u2 = g_unitFindArray[i + 1];
            y1 = Tile_GetY(u1.o.position);
            y2 = Tile_GetY(u2.o.position);
            if (g_table_unitInfo[u1.o.type].movementType == MOVEMENT_FOOT) y1 -= 0x100;
            if (g_table_unitInfo[u2.o.type].movementType == MOVEMENT_FOOT) y2 -= 0x100;

            if ((int) y1 > (int) y2) {
                g_unitFindArray[i] = u2;
                g_unitFindArray[i + 1] = u1;
            }
        }

        for (i = 0; i < g_unitFindCount; i++) {
            Unit u;

            u = g_unitFindArray[i];
            if ((u.o.seenByHouses & (1 << g_playerHouseID)) != 0 && !u.o.flags.isNotOnMap) {
                if (House_AreAllied(u.o.houseID, g_playerHouseID)) {
                    h.unitCountAllied++;
                } else {
                    h.unitCountEnemy++;
                }
            }
        }
    }

    /**
     * Get the unit on the given packed tile.
     *
     * @param packed The packed tile to get the unit from.
     * @return The unit.
     */
    public static Unit Unit_Get_ByPackedTile(int packed) {
        Tile tile;

        if (Tile_IsOutOfMap(packed)) return null;

        tile = g_map[packed];
        if (!tile.hasUnit) return null;
        return Unit_Get_ByIndex(tile.index - 1);
    }

    /**
     * Determines whether a move order into the given structure is OK for
     * a particular unit.
     * <p>
     * It handles orders to invade enemy buildings as well as going into
     * a friendly structure (e.g. refinery, repair facility).
     *
     * @param unit The Unit to operate on.
     * @param s    The Structure to operate on.
     * @return 0 - invalid movement
     * 1 - valid movement, will try to get close to the structure
     * 2 - valid movement, will attempt to damage/conquer the structure
     */
    public static int Unit_IsValidMovementIntoStructure(Unit unit, Structure s) {
	    StructureInfo si;
	    UnitInfo ui;
        int unitEnc;
        int structEnc;

        if (unit == null || s == null) return 0;

        si = g_table_structureInfo[s.o.type];
        ui = g_table_unitInfo[unit.o.type];

        unitEnc = Tools_Index_Encode(unit.o.index, IT_UNIT);
        structEnc = Tools_Index_Encode(s.o.index, IT_STRUCTURE);

        /* Movement into structure of other owner. */
        if (Unit_GetHouseID(unit) != s.o.houseID) {
            /* Saboteur can always enter houses */
            if (unit.o.type == UNIT_SABOTEUR && unit.targetMove == structEnc) return 2;
            /* Entering houses is only possible for foot-units and if the structure is conquerable.
             * Everyone else can only move close to the building. */
            if (ui.movementType == MOVEMENT_FOOT && si.o.flags.conquerable)
                return unit.targetMove == structEnc ? 2 : 1;
            return 0;
        }

        /* Prevent movement if target structure does not accept the unit type. */
        if ((si.enterFilter & (1 << unit.o.type)) == 0) return 0;

        /* TODO -- Not sure. */
        if (s.o.script.variables[4] == unitEnc) return 2;

        /* Enter only if structure not linked to any other unit already. */
        return s.o.linkedID == 0xFF ? 1 : 0;
    }

    /**
     * Sets the destination for the given unit.
     *
     * @param u           The unit to set the destination for.
     * @param destination The destination (encoded index).
     */
    public static void Unit_SetDestination(Unit u, int destination) {
        Structure s;

        if (u == null) return;
        if (!Tools_Index_IsValid(destination)) return;
        if (u.targetMove == destination) return;

        if (Tools_Index_GetType(destination) == IT_TILE) {
            Unit u2;
            int packed;

            packed = Tools_Index_Decode(destination);

            u2 = Unit_Get_ByPackedTile(packed);
            if (u2 != null) {
                if (u != u2) destination = Tools_Index_Encode(u2.o.index, IT_UNIT);
            } else {
                s = Structure_Get_ByPackedTile(packed);
                if (s != null) destination = Tools_Index_Encode(s.o.index, IT_STRUCTURE);
            }
        }

        s = Tools_Index_GetStructure(destination);
        if (s != null && s.o.houseID == Unit_GetHouseID(u)) {
            if (Unit_IsValidMovementIntoStructure(u, s) == 1 || g_table_unitInfo[u.o.type].movementType == MOVEMENT_WINGER) {
                Object_Script_Variable4_Link(Tools_Index_Encode(u.o.index, IT_UNIT), destination);
            }
        }

        u.targetMove = destination;
        u.route[0] = 0xFF;
    }

    /**
     * Get the priority a target unit has for a given unit. The higher the value,
     * the more serious it should look at the target.
     *
     * @param unit   The unit looking at a target.
     * @param target The unit to look at.
     * @return The priority of the target.
     */
    public static int Unit_GetTargetUnitPriority(Unit unit, Unit target) {
	    UnitInfo targetInfo;
	    UnitInfo unitInfo;
        int distance;
        int priority;

        if (unit == null || target == null) return 0;
        if (unit == target) return 0;

        if (!target.o.flags.allocated) return 0;
        if ((target.o.seenByHouses & (1 << Unit_GetHouseID(unit))) == 0) return 0;

        if (House_AreAllied(Unit_GetHouseID(unit), Unit_GetHouseID(target))) return 0;

        unitInfo = g_table_unitInfo[unit.o.type];
        targetInfo = g_table_unitInfo[target.o.type];

        if (!targetInfo.o.flags.priority) return 0;

        if (targetInfo.movementType == MOVEMENT_WINGER) {
            if (!unitInfo.o.flags.targetAir) return 0;
            if (target.o.houseID == g_playerHouseID && !Map_IsPositionUnveiled(Tile_PackTile(target.o.position)))
                return 0;
        }

        if (!Map_IsValidPosition(Tile_PackTile(target.o.position))) return 0;

        distance = Tile_GetDistanceRoundedUp(unit.o.position, target.o.position);

        if (!Map_IsValidPosition(Tile_PackTile(unit.o.position))) {
            if (targetInfo.fireDistance >= distance) return 0;
        }

        priority = targetInfo.o.priorityTarget + targetInfo.o.priorityBuild;
        if (distance != 0) priority = (priority / distance) + 1;

        if (priority > 0x7D00) return 0x7D00;
        return priority;
    }

    /**
     * Finds the closest refinery a harvester can go to.
     *
     * @param unit The unit to find the closest refinery for.
     * @return 1 if unit.originEncoded was not 0, else 0.
     */
    public static int Unit_FindClosestRefinery(Unit unit) {
        int res;
        Structure s = null;
        int mind = 0;
        Structure s2;
        int d;
        PoolFindStruct find = new PoolFindStruct();

        res = (unit.originEncoded == 0) ? 0 : 1;

        if (unit.o.type != UNIT_HARVESTER) {
            unit.originEncoded = Tools_Index_Encode(Tile_PackTile(unit.o.position), IT_TILE);
            return res;
        }

        find.type = STRUCTURE_REFINERY;
        find.houseID = Unit_GetHouseID(unit);
        find.index = 0xFFFF;

        while (true) {
            s2 = Structure_Find(find);
            if (s2 == null) break;
            if (s2.state != STRUCTURE_STATE_BUSY) continue;
            d = Tile_GetDistance(unit.o.position, s2.o.position);
            if (mind != 0 && d >= mind) continue;
            mind = d;
            s = s2;
        }

        if (s == null) {
            find.type = STRUCTURE_REFINERY;
            find.houseID = Unit_GetHouseID(unit);
            find.index = 0xFFFF;

            while (true) {
                s2 = Structure_Find(find);
                if (s2 == null) break;
                d = Tile_GetDistance(unit.o.position, s2.o.position);
                if (mind != 0 && d >= mind) continue;
                mind = d;
                s = s2;
            }
        }

        if (s != null) unit.originEncoded = Tools_Index_Encode(s.o.index, IT_STRUCTURE);

        return res;
    }

    /**
     * Sets the position of the given unit.
     *
     * @param u The Unit to set the position for.
     * @return True if and only if the position changed.
     * @position The position.
     */
    static boolean Unit_SetPosition(Unit u, Tile32 position) {
	   UnitInfo ui;

        if (u == null) return false;

        ui = g_table_unitInfo[u.o.type];
        u.o.flags.isNotOnMap = false;

        u.o.position = Tile_Center(position);

        if (u.originEncoded == 0) Unit_FindClosestRefinery(u);

        u.o.script.variables[4] = 0;

        if (Unit_IsTileOccupied(u)) {
            u.o.flags.isNotOnMap = true;
            return false;
        }

        u.currentDestination.x = 0;
        u.currentDestination.y = 0;
        u.targetMove = 0;
        u.targetAttack = 0;

        if (g_map[Tile_PackTile(u.o.position)].isUnveiled) {
            /* A new unit being delivered fresh from the factory; force a seenByHouses
             *  update and add it to the statistics etc. */
            u.o.seenByHouses &= ~(1 << u.o.houseID);
            Unit_HouseUnitCount_Add(u, g_playerHouseID);
        }

        if (u.o.houseID != g_playerHouseID || u.o.type == UNIT_HARVESTER || u.o.type == UNIT_SABOTEUR) {
            Unit_SetAction(u, ui.actionAI);
        } else {
            Unit_SetAction(u, ui.o.actionsPlayer[3]);
        }

        u.spriteOffset = 0;

        Unit_UpdateMap(1, u);

        return true;
    }

    /**
     * Remove the Unit from the game, doing all required administration for it, like
     * deselecting it, remove it from the radar count, stopping scripts, ..
     *
     * @param u The Unit to remove.
     */
    public static void Unit_Remove(Unit u) {
        if (u == null) return;

        u.o.flags.allocated = true;
        Unit_UntargetMe(u);

        if (u == g_unitSelected) Unit_Select(null);

        u.o.flags.bulletIsBig = true;
        Unit_UpdateMap(0, u);

        Unit_HouseUnitCount_Remove(u);

        Script_Reset(u.o.script, g_scriptUnit);

        Unit_Free(u);
    }

    /**
     * Gets the best target unit for the given unit.
     *
     * @param u The Unit to get the best target for.
     * @param mode How to determine the best target.
     * @return The best target or NULL if none found.
     */
    static Unit Unit_FindBestTargetUnit(Unit u, int mode) {
        Tile32 position = new Tile32();
        int distance;
        PoolFindStruct find = new PoolFindStruct();
        Unit best = null;
        int bestPriority = 0;

        if (u == null) return null;

        position = u.o.position;
        if (u.originEncoded == 0) {
            u.originEncoded = Tools_Index_Encode(Tile_PackTile(position), IT_TILE);
        } else {
            position = Tools_Index_GetTile(u.originEncoded);
        }

        distance = g_table_unitInfo[u.o.type].fireDistance << 8;
        if (mode == 2) distance <<= 1;

        find.houseID = HOUSE_INVALID;
        find.type = 0xFFFF;
        find.index = 0xFFFF;

        while (true) {
            Unit target;
            int priority;

            target = Unit_Find(find);

            if (target == null) break;

            if (mode != 0 && mode != 4) {
                if (mode == 1) {
                    if (Tile_GetDistance(u.o.position, target.o.position) > distance) continue;
                }
                if (mode == 2) {
                    if (Tile_GetDistance(position, target.o.position) > distance) continue;
                }
            }

            priority = Unit_GetTargetUnitPriority(u, target);

            if ((int) priority > (int) bestPriority) {
                best = target;
                bestPriority = priority;
            }
        }

        if (bestPriority == 0) return null;

        return best;
    }

    /**
     * Get the priority for a target. Various of things have influence on this score,
     * most noticeable the movementType of the target, his distance to you, and
     * if he is moving/firing.
     *
     * @param unit   The Unit that is requesting the score.
     * @param target The Unit that is being targeted.
     * @return The priority of the target.
     * @note It only considers units on sand.
     */
    static int Unit_Sandworm_GetTargetPriority(Unit unit, Unit target) {
        int res;
        int distance;

        if (unit == null || target == null) return 0;
        if (!Map_IsPositionUnveiled(Tile_PackTile(target.o.position))) return 0;
        if (!g_table_landscapeInfo[Map_GetLandscapeType(Tile_PackTile(target.o.position))].isSand) return 0;

        switch (g_table_unitInfo[target.o.type].movementType) {
            case MOVEMENT_FOOT:
                res = 0x64;
                break;
            case MOVEMENT_TRACKED:
                res = 0x3E8;
                break;
            case MOVEMENT_HARVESTER:
                res = 0x3E8;
                break;
            case MOVEMENT_WHEELED:
                res = 0x1388;
                break;
            default:
                res = 0;
                break;
        }

        if (target.speed != 0 || target.fireDelay != 0) res *= 4;

        distance = Tile_GetDistanceRoundedUp(unit.o.position, target.o.position);

        if (distance != 0 && res != 0) res /= distance;
        if (distance < 2) res *= 2;

        return res;
    }

    /**
     * Find the best target, based on the score. Only considers units on sand.
     *
     * @param unit The unit to search a target for.
     * @return A target Unit, or NULL if none is found.
     */
    public static Unit Unit_Sandworm_FindBestTarget(Unit unit) {
        Unit best = null;
        PoolFindStruct find = new PoolFindStruct();
        int bestPriority = 0;

        if (unit == null) return null;

        find.houseID = HOUSE_INVALID;
        find.type = 0xFFFF;
        find.index = 0xFFFF;

        while (true) {
            Unit u;
            int priority;

            u = Unit_Find(find);

            if (u == null) break;

            priority = Unit_Sandworm_GetTargetPriority(unit, u);

            if (priority >= bestPriority) {
                best = u;
                bestPriority = priority;
            }
        }

        if (bestPriority == 0) return null;

        return best;
    }

    /**
     * Initiate the first movement of a Unit when the pathfinder has found a route.
     *
     * @param unit The Unit to operate on.
     * @return True if movement was initiated (not blocked etc).
     */
    public static boolean Unit_StartMovement(Unit unit) {
	    UnitInfo ui;
        int orientation;
        int packed;
        int type;
        Tile32 position;
        int speed;
        int score;

        if (unit == null) return false;

        ui = g_table_unitInfo[unit.o.type];

        orientation = (int) ((unit.orientation[0].current + 16) & 0xE0);

        Unit_SetOrientation(unit, orientation, true, 0);
        Unit_SetOrientation(unit, orientation, false, 1);

        position = Tile_MoveByOrientation(unit.o.position, orientation);

        packed = Tile_PackTile(position);

        unit.distanceToDestination = 0x7FFF;

        score = Unit_GetTileEnterScore(unit, packed, orientation / 32);

        if (score > 255 || score == -1) return false;

        type = Map_GetLandscapeType(packed);
        if (type == LST_STRUCTURE) type = LST_CONCRETE_SLAB;

        speed = g_table_landscapeInfo[type].movementSpeed[ui.movementType];

        if (unit.o.type == UNIT_SABOTEUR && type == LST_WALL) speed = 255;
        unit.o.flags.isSmoking = false;

        /* ENHANCEMENT -- the flag is never set to false in original Dune2; in result, once the wobbling starts, it never stops. */
        if (g_dune2_enhanced) {
            unit.o.flags.isWobbling = g_table_landscapeInfo[type].letUnitWobble;
        } else {
            if (g_table_landscapeInfo[type].letUnitWobble) unit.o.flags.isWobbling = true;
        }

        if ((ui.o.hitpoints / 2) > unit.o.hitpoints && ui.movementType != MOVEMENT_WINGER) speed -= speed / 4;

        Unit_SetSpeed(unit, speed);

        if (ui.movementType != MOVEMENT_SLITHER) {
            Tile32 positionOld;

            positionOld = unit.o.position;
            unit.o.position = position;

            Unit_UpdateMap(1, unit);

            unit.o.position = positionOld;
        }

        unit.currentDestination = position;

        Unit_Deviation_Decrease(unit, 10);

        return true;
    }

    /**
     * Set the target for the given unit.
     *
     * @param unit    The Unit to set the target for.
     * @param encoded The encoded index of the target.
     */
    void Unit_SetTarget(Unit unit, int encoded) {
        if (unit == null || !Tools_Index_IsValid(encoded)) return;
        if (unit.targetAttack == encoded) return;

        if (Tools_Index_GetType(encoded) == IT_TILE) {
            int packed;
            Unit u;

            packed = Tools_Index_Decode(encoded);

            u = Unit_Get_ByPackedTile(packed);
            if (u != null) {
                encoded = Tools_Index_Encode(u.o.index, IT_UNIT);
            } else {
                Structure s;

                s = Structure_Get_ByPackedTile(packed);
                if (s != null) {
                    encoded = Tools_Index_Encode(s.o.index, IT_STRUCTURE);
                }
            }
        }

        if (Tools_Index_Encode(unit.o.index, IT_UNIT) == encoded) {
            encoded = Tools_Index_Encode(Tile_PackTile(unit.o.position), IT_TILE);
        }

        unit.targetAttack = encoded;

        if (!g_table_unitInfo[unit.o.type].o.flags.hasTurret) {
            unit.targetMove = encoded;
            unit.route[0] = 0xFF;
        }
    }

    /**
     * Decrease deviation counter for the given unit.
     *
     * @param unit   The Unit to decrease counter for.
     * @param amount The amount to decrease.
     * @return True if and only if the unit lost deviation.
     */
    static boolean Unit_Deviation_Decrease(Unit unit, int amount) {
	    UnitInfo ui;

        if (unit == null || unit.deviated == 0) return false;

        ui = g_table_unitInfo[unit.o.type];

        if (!ui.flags.isNormalUnit) return false;

        if (amount == 0) {
            amount = g_table_houseInfo[unit.o.houseID].toughness;
        }

        if (unit.deviated > amount) {
            unit.deviated -= amount;
            return false;
        }

        unit.deviated = 0;

        unit.o.flags.bulletIsBig = true;
        Unit_UpdateMap(2, unit);
        unit.o.flags.bulletIsBig = false;

        if (unit.o.houseID == g_playerHouseID) {
            Unit_SetAction(unit, ui.o.actionsPlayer[3]);
        } else {
            Unit_SetAction(unit, ui.actionAI);
        }

        Unit_UntargetMe(unit);
        unit.targetAttack = 0;
        unit.targetMove = 0;

        return true;
    }

    /**
     * Remove fog arount the given unit.
     *
     * @param unit The Unit to remove fog around.
     */
    public static void Unit_RemoveFog(Unit unit) {
        int fogUncoverRadius;

        if (unit == null) return;
        if (unit.o.flags.isNotOnMap) return;
        if ((unit.o.position.x == 0xFFFF && unit.o.position.y == 0xFFFF) || (unit.o.position.x == 0 && unit.o.position.y == 0))
            return;
        if (!House_AreAllied(Unit_GetHouseID(unit), g_playerHouseID)) return;

        fogUncoverRadius = g_table_unitInfo[unit.o.type].o.fogUncoverRadius;

        if (fogUncoverRadius == 0) return;

        Tile_RemoveFogInRadius(unit.o.position, fogUncoverRadius);
    }

    /**
     * Deviate the given unit.
     *
     * @param unit        The Unit to deviate.
     * @param probability The probability for deviation to succeed.
     * @param houseID     House controlling the deviator.
     * @return True if and only if the unit beacame deviated.
     */
    boolean Unit_Deviate(Unit unit, int probability, int houseID) {
        UnitInfo ui;

        if (unit == null) return false;

        ui = g_table_unitInfo[unit.o.type];

        if (!ui.flags.isNormalUnit) return false;
        if (unit.deviated != 0) return false;
        if (ui.flags.isNotDeviatable) return false;

        if (probability == 0) probability = g_table_houseInfo[unit.o.houseID].toughness;

        if (unit.o.houseID != g_playerHouseID) {
            probability -= probability / 8;
        }

        if (Tools_Random_256() >= probability) return false;

        unit.deviated = 120;
        unit.deviatedHouse = houseID;

        Unit_UpdateMap(2, unit);

        if (g_playerHouseID == unit.deviatedHouse) {
            Unit_SetAction(unit, ui.o.actionsPlayer[3]);
        } else {
            Unit_SetAction(unit, ui.actionAI);
        }

        Unit_UntargetMe(unit);
        unit.targetAttack = 0;
        unit.targetMove = 0;

        return true;
    }

    /**
     * Moves the given unit.
     *
     * @param unit     The Unit to move.
     * @param distance The maximum distance to pass through.
     * @return ??.
     */
    public static boolean Unit_Move(Unit unit, int distance) {
	    UnitInfo ui;
        int d;
        int packed;
        Tile32 newPosition;
        boolean ret;
        Tile32 currentDestination;
        boolean isSpiceBloom = false;
        boolean isSpecialBloom = false;

        if (unit == null || !unit.o.flags.used) return false;

        ui = g_table_unitInfo[unit.o.type];

        newPosition = Tile_MoveByDirection(unit.o.position, unit.orientation[0].current, distance);

        if ((newPosition.x == unit.o.position.x) && (newPosition.y == unit.o.position.y)) return false;

        if (!Tile_IsValid(newPosition)) {
            if (!ui.flags.mustStayInMap) {
                Unit_Remove(unit);
                return true;
            }

            if (unit.o.flags.byScenario && unit.o.linkedID == 0xFF && unit.o.script.variables[4] == 0) {
                Unit_Remove(unit);
                return true;
            }

            newPosition = unit.o.position;
            Unit_SetOrientation(unit, unit.orientation[0].current + (Tools_Random_256() & 0xF), false, 0);
        }

        unit.wobbleIndex = 0;
        if (ui.flags.canWobble && unit.o.flags.isWobbling) {
            unit.wobbleIndex = Tools_Random_256() & 7;
        }

        d = Tile_GetDistance(newPosition, unit.currentDestination);
        packed = Tile_PackTile(newPosition);

        if (ui.flags.isTracked && d < 48) {
            Unit u;
            u = Unit_Get_ByPackedTile(packed);

            /* Driving over a foot unit */
            if (u != null && g_table_unitInfo[u.o.type].movementType == MOVEMENT_FOOT && u.o.flags.allocated) {
                if (u == g_unitSelected) Unit_Select(null);

                Unit_UntargetMe(u);
                u.o.script.variables[1] = 1;
                Unit_SetAction(u, ACTION_DIE);
            } else {
                int type = Map_GetLandscapeType(packed);

                /* Produce tracks in the sand */
                if ((type == LST_NORMAL_SAND || type == LST_ENTIRELY_DUNE) && g_map[packed].overlayTileID == 0) {
                    int animationID = Orientation_Orientation256ToOrientation8(unit.orientation[0].current);

                    assert (animationID < 8);
                    Animation_Start(g_table_animation_unitMove[animationID], unit.o.position, 0, unit.o.houseID, 5);
                }
            }
        }

        Unit_UpdateMap(0, unit);

        if (ui.movementType == MOVEMENT_WINGER) {
            unit.o.flags.animationFlip = !unit.o.flags.animationFlip;
        }

        currentDestination = unit.currentDestination;
        distance = Tile_GetDistance(newPosition, currentDestination);

        if (unit.o.type == UNIT_SONIC_BLAST) {
            Unit u;
            int damage;

            damage = (unit.o.hitpoints / 4) + 1;
            ret = false;

            u = Unit_Get_ByPackedTile(packed);

            if (u != null) {
                if (!g_table_unitInfo[u.o.type].flags.sonicProtection) {
                    Unit_Damage(u, damage, 0);
                }
            } else {
                Structure s;

                s = Structure_Get_ByPackedTile(packed);

                if (s != null) {
                    /* ENHANCEMENT -- make sonic blast trigger counter attack, but
                     * do not warn about base under attack (original behaviour). */
                    if (g_dune2_enhanced && s.o.houseID != g_playerHouseID && !House_AreAllied(unit.o.houseID, s.o.houseID)) {
                        Structure_HouseUnderAttack(s.o.houseID);
                    }

                    Structure_Damage(s, damage, 0);
                } else {
                    if (Map_GetLandscapeType(packed) == LST_WALL && g_table_structureInfo[STRUCTURE_WALL].o.hitpoints > damage)
                        Tools_Random_256();
                }
            }

            if (unit.o.hitpoints < (ui.damage / 2)) {
                unit.o.flags.bulletIsBig = true;
            }

            if (--unit.o.hitpoints == 0 || unit.fireDelay == 0) {
                Unit_Remove(unit);
            }
        } else {
            if (unit.o.type == UNIT_BULLET) {
                int type = Map_GetLandscapeType(Tile_PackTile(newPosition));
                if (type == LST_WALL || type == LST_STRUCTURE) {
                    if (Tools_Index_GetType(unit.originEncoded) == IT_STRUCTURE) {
                        if (g_map[Tile_PackTile(newPosition)].houseID == unit.o.houseID) {
                            type = LST_NORMAL_SAND;
                        }
                    }
                }

                if (type == LST_WALL || type == LST_STRUCTURE || type == LST_ENTIRELY_MOUNTAIN) {
                    unit.o.position = newPosition;

                    Map_MakeExplosion((ui.explosionType + unit.o.hitpoints / 10) & 3, unit.o.position, unit.o.hitpoints, unit.originEncoded);

                    Unit_Remove(unit);
                    return true;
                }
            }

            ret = (unit.distanceToDestination < distance || distance < 16) ? true : false;

            if (ret) {
                if (ui.flags.isBullet) {
                    if (unit.fireDelay == 0 || unit.o.type == UNIT_MISSILE_TURRET) {
                        if (unit.o.type == UNIT_MISSILE_HOUSE) {
                            int i;

                            for (i = 0; i < 17; i++) {
                                int[] offsetX = new int[] {
                                    0, 0, 200, 256, 200, 0, -200, -256, -200, 0, 400, 512, 400, 0, -400, -512, -400
                                };
                                int[] offsetY = new int[] {
                                    0, -256, -200, 0, 200, 256, 200, 0, -200, -512, -400, 0, 400, 512, 400, 0, -400
                                };
                                Tile32 p = newPosition;
                                p.y += offsetY[i];
                                p.x += offsetX[i];

                                if (Tile_IsValid(p)) {
                                    Map_MakeExplosion(ui.explosionType, p, 200, 0);
                                }
                            }
                        } else if (ui.explosionType != 0xFFFF) {
                            if (ui.flags.impactOnSand && g_map[Tile_PackTile(unit.o.position)].index == 0 && Map_GetLandscapeType(Tile_PackTile(unit.o.position)) == LST_NORMAL_SAND) {
                                Map_MakeExplosion(EXPLOSION_SAND_BURST, newPosition, unit.o.hitpoints, unit.originEncoded);
                            } else if (unit.o.type == UNIT_MISSILE_DEVIATOR) {
                                Map_DeviateArea(ui.explosionType, newPosition, 32, unit.o.houseID);
                            } else {
                                Map_MakeExplosion((ui.explosionType + unit.o.hitpoints / 20) & 3, newPosition, unit.o.hitpoints, unit.originEncoded);
                            }
                        }

                        Unit_Remove(unit);
                        return true;
                    }
                } else if (ui.flags.isGroundUnit) {
                    if (currentDestination.x != 0 || currentDestination.y != 0) newPosition = currentDestination;
                    unit.targetPreLast = unit.targetLast;
                    unit.targetLast = unit.o.position;
                    unit.currentDestination.x = 0;
                    unit.currentDestination.y = 0;

                    if (unit.o.flags.s.degrades && (Tools_Random_256() & 3) == 0) {
                        Unit_Damage(unit, 1, 0);
                    }

                    if (unit.o.type == UNIT_SABOTEUR) {
                        boolean detonate = (Map_GetLandscapeType(Tile_PackTile(newPosition)) == LST_WALL);

                        if (!detonate) {
                            /* ENHANCEMENT -- Saboteurs tend to forget their goal, depending on terrain and game speed: to blow up on reaching their destination. */
                            if (g_dune2_enhanced) {
                                detonate = (unit.targetMove != 0 && Tile_GetDistance(newPosition, Tools_Index_GetTile(unit.targetMove)) < 16);
                            } else {
                                detonate = (unit.targetMove != 0 && Tile_GetDistance(unit.o.position, Tools_Index_GetTile(unit.targetMove)) < 32);
                            }
                        }

                        if (detonate) {
                            Map_MakeExplosion(EXPLOSION_SABOTEUR_DEATH, newPosition, 500, 0);

                            Unit_Remove(unit);
                            return true;
                        }
                    }

                    Unit_SetSpeed(unit, 0);

                    if (unit.targetMove == Tools_Index_Encode(packed, IT_TILE)) {
                        unit.targetMove = 0;
                    }

                    {
                        Structure s;

                        s = Structure_Get_ByPackedTile(packed);
                        if (s != null) {
                            unit.targetPreLast.x = 0;
                            unit.targetPreLast.y = 0;
                            unit.targetLast.x = 0;
                            unit.targetLast.y = 0;
                            Unit_EnterStructure(unit, s);
                            return true;
                        }
                    }

                    if (unit.o.type != UNIT_SANDWORM) {
                        if (g_map[packed].groundTileID == g_bloomTileID || g_map[packed].groundTileID == g_bloomTileID + 1) {
                            isSpiceBloom = true;
                        }
                    }
                }
            }
        }

        unit.distanceToDestination = distance;
        unit.o.position = newPosition;

        Unit_UpdateMap(1, unit);

        if (isSpecialBloom) Map_Bloom_ExplodeSpecial(packed, Unit_GetHouseID(unit));
        if (isSpiceBloom) Map_Bloom_ExplodeSpice(packed, Unit_GetHouseID(unit));

        return ret;
    }

    /**
     * Applies damages to the given unit.
     *
     * @param unit   The Unit to apply damages on.
     * @param damage The amount of damage to apply.
     * @param range  ??.
     * @return True if and only if the unit has no hitpoints left.
     */
    boolean Unit_Damage(Unit unit, int damage, int range) {
	    UnitInfo ui;
        boolean alive = false;
        int houseID;

        if (unit == null || !unit.o.flags.allocated) return false;

        ui = g_table_unitInfo[unit.o.type];

        if (!ui.flags.isNormalUnit && unit.o.type != UNIT_SANDWORM) return false;

        if (unit.o.hitpoints != 0) alive = true;

        if (unit.o.hitpoints >= damage) {
            unit.o.hitpoints -= damage;
        } else {
            unit.o.hitpoints = 0;
        }

        Unit_Deviation_Decrease(unit, 0);

        houseID = Unit_GetHouseID(unit);

        if (unit.o.hitpoints == 0) {
            Unit_RemovePlayer(unit);

            if (unit.o.type == UNIT_HARVESTER)
                Map_FillCircleWithSpice(Tile_PackTile(unit.o.position), unit.amount / 32);

            if (unit.o.type == UNIT_SABOTEUR) {
                Sound_Output_Feedback(20);
            } else {
                if (!ui.o.flags.noMessageOnDeath && alive) {
                    Sound_Output_Feedback((houseID == g_playerHouseID || g_campaignID > 3) ? houseID + 14 : 13);
                }
            }

            Unit_SetAction(unit, ACTION_DIE);
            return true;
        }

        if (range != 0) {
            Map_MakeExplosion((damage < 25) ? EXPLOSION_IMPACT_SMALL : EXPLOSION_IMPACT_MEDIUM, unit.o.position, 0, 0);
        }

        if (houseID != g_playerHouseID && unit.actionID == ACTION_AMBUSH && unit.o.type != UNIT_HARVESTER) {
            Unit_SetAction(unit, ACTION_ATTACK);
        }

        if (unit.o.hitpoints >= ui.o.hitpoints / 2) return false;

        if (unit.o.type == UNIT_SANDWORM) {
            Unit_SetAction(unit, ACTION_DIE);
        }

        if (unit.o.type == UNIT_TROOPERS || unit.o.type == UNIT_INFANTRY) {
            unit.o.type += 2;
            ui = g_table_unitInfo[unit.o.type];
            unit.o.hitpoints = ui.o.hitpoints;

            Unit_UpdateMap(2, unit);

            if (Tools_Random_256() < g_table_houseInfo[unit.o.houseID].toughness) {
                Unit_SetAction(unit, ACTION_RETREAT);
            }
        }

        if (ui.movementType != MOVEMENT_TRACKED && ui.movementType != MOVEMENT_HARVESTER && ui.movementType != MOVEMENT_WHEELED)
            return false;

        unit.o.flags.isSmoking = true;
        unit.spriteOffset = 0;
        unit.timer = 0;

        return false;
    }

    /**
     * Untarget the given Unit.
     *
     * @param unit The Unit to untarget.
     */
    static void Unit_UntargetMe(Unit unit) {
        PoolFindStruct find = new PoolFindStruct();
        int encoded = Tools_Index_Encode(unit.o.index, IT_UNIT);

        Object_Script_Variable4_Clear(unit.o);

        find.houseID = HOUSE_INVALID;
        find.type = 0xFFFF;
        find.index = 0xFFFF;

        while (true) {
            Unit u;

            u = Unit_Find(find);
            if (u == null) break;

            if (u.targetMove == encoded) u.targetMove = 0;
            if (u.targetAttack == encoded) u.targetAttack = 0;
            if (u.o.script.variables[4] == encoded) Object_Script_Variable4_Clear(u.o);
        }

        find.houseID = HOUSE_INVALID;
        find.type = 0xFFFF;
        find.index = 0xFFFF;

        while (true) {
            Structure s;

            s = Structure_Find(find);
            if (s == null) break;

            if (s.o.type != STRUCTURE_TURRET && s.o.type != STRUCTURE_ROCKET_TURRET) continue;
            if (s.o.script.variables[2] == encoded) s.o.script.variables[2] = 0;
        }

        Unit_RemoveFromTeam(unit);

        find.houseID = HOUSE_INVALID;
        find.type = 0xFFFF;
        find.index = 0xFFFF;

        while (true) {
            Team t;

            t = Team_Find(find);
            if (t == null) break;

            if (t.target == encoded) t.target = 0;
        }
    }

    /**
     * Set the new orientation of the unit.
     *
     * @param unit            The Unit to operate on.
     * @param orientation     The new orientation of the unit.
     * @param rotateInstantly If true, rotation is instant. Else the unit turns over the next few ticks slowly.
     * @param level           0 = base, 1 = top (turret etc).
     */
    public static void Unit_SetOrientation(Unit unit, int orientation, boolean rotateInstantly, int level) {
        int diff;

        assert (level == 0 || level == 1);

        if (unit == null) return;

        unit.orientation[level].speed = 0;
        unit.orientation[level].target = orientation;

        if (rotateInstantly) {
            unit.orientation[level].current = orientation;
            return;
        }

        if (unit.orientation[level].current == orientation) return;

        unit.orientation[level].speed = g_table_unitInfo[unit.o.type].turningSpeed * 4;

        diff = orientation - unit.orientation[level].current;

        if ((diff > -128 && diff < 0) || diff > 128) {
            unit.orientation[level].speed = -unit.orientation[level].speed;
        }
    }

    /**
     * Selects the given unit.
     *
     * @param unit The Unit to select.
     */
    public static void Unit_Select(Unit unit) {
        if (unit == g_unitSelected) return;

        if (unit != null && !unit.o.flags.allocated && !g_debugGame) {
            unit = null;
        }

        if (unit != null && (unit.o.seenByHouses & (1 << g_playerHouseID)) == 0 && !g_debugGame) {
            unit = null;
        }

        if (g_unitSelected != null) Unit_UpdateMap(2, g_unitSelected);

        if (unit == null) {
            g_unitSelected = null;

            GUI_ChangeSelectionType(SELECTIONTYPE_STRUCTURE);
            return;
        }

        if (Unit_GetHouseID(unit) == g_playerHouseID) {
		    UnitInfo ui;

            ui = g_table_unitInfo[unit.o.type];

            /* Plays the 'reporting' sound file. */
            Sound_StartSound(ui.movementType == MOVEMENT_FOOT ? 18 : 19);

            GUI_DisplayHint(ui.o.hintStringID, ui.o.spriteID);
        }

        if (g_unitSelected != null) {
            if (g_unitSelected != unit) Unit_DisplayStatusText(unit);

            g_unitSelected = unit;

            GUI_Widget_ActionPanel_Draw(true);
        } else {
            Unit_DisplayStatusText(unit);
            g_unitSelected = unit;

            GUI_ChangeSelectionType(SELECTIONTYPE_UNIT);
        }

        Unit_UpdateMap(2, g_unitSelected);

        Map_SetSelectionObjectPosition(0xFFFF);
    }

    /**
     * Create a unit (and a carryall if needed).
     *
     * @param houseID The House of the new Unit.
     * @param typeID The type of the new Unit.
     * @param destination To where on the map this Unit should move.
     * @return The new created Unit, or NULL if something failed.
     */
    Unit Unit_CreateWrapper(int houseID, int typeID, int destination) {
        Tile32 tile;
        House h;
        int orientation;
        Unit unit;
        Unit carryall;

        tile = Tile_UnpackTile(Map_FindLocationTile(Tools_Random_256() & 3, houseID));

        h = House_Get_ByIndex(houseID);

        {
            Tile32 t = new Tile32();
            t.x = 0x2000;
            t.y = 0x2000;
            orientation = Tile_GetDirection(tile, t);
        }

        if (g_table_unitInfo[typeID].movementType == MOVEMENT_WINGER) {
            g_validateStrictIfZero++;
            unit = Unit_Create(UNIT_INDEX_INVALID, typeID, houseID, tile, orientation);
            g_validateStrictIfZero--;

            if (unit == null) return null;

            unit.o.flags.byScenario = true;

            if (destination != 0) {
                Unit_SetDestination(unit, destination);
            }

            return unit;
        }

        g_validateStrictIfZero++;
        carryall = Unit_Create(UNIT_INDEX_INVALID, UNIT_CARRYALL, houseID, tile, orientation);
        g_validateStrictIfZero--;

        if (carryall == null) {
            if (typeID == UNIT_HARVESTER && h.harvestersIncoming == 0) h.harvestersIncoming++;
            return null;
        }

        if (House_AreAllied(houseID, g_playerHouseID) || Unit_IsTypeOnMap(houseID, UNIT_CARRYALL)) {
            carryall.o.flags.byScenario = true;
        }

        tile.x = 0xFFFF;
        tile.y = 0xFFFF;

        g_validateStrictIfZero++;
        unit = Unit_Create(UNIT_INDEX_INVALID, typeID, houseID, tile, 0);
        g_validateStrictIfZero--;

        if (unit == null) {
            Unit_Remove(carryall);
            if (typeID == UNIT_HARVESTER && h.harvestersIncoming == 0) h.harvestersIncoming++;
            return null;
        }

        carryall.o.flags.inTransport = true;
        carryall.o.linkedID = unit.o.index & 0xFF;
        if (typeID == UNIT_HARVESTER) unit.amount = 1;

        if (destination != 0) {
            Unit_SetDestination(carryall, destination);
        }

        return unit;
    }

    /**
     * Find a target around the given packed tile.
     *
     * @param packed The packed tile around where to look.
     * @return A packed tile where a Unit/Structure is, or the given packed tile if nothing found.
     */
    int Unit_FindTargetAround(int packed) {
        int[] around = new int[] {0, -1, 1, -64, 64, -65, -63, 65, 63};

        int i;

        if (g_selectionType == SELECTIONTYPE_PLACE) return packed;

        if (Structure_Get_ByPackedTile(packed) != null) return packed;

        if (Map_GetLandscapeType(packed) == LST_BLOOM_FIELD) return packed;

        for (i = 0; i < lengthof(around); i++) {
            Unit u;

            u = Unit_Get_ByPackedTile(packed + around[i]);
            if (u == null) continue;

            return Tile_PackTile(u.o.position);
        }

        return packed;
    }

    /**
     * Check if the position the unit is on is already occupied.
     *
     * @param unit The Unit to operate on.
     * @return True if and only if the position of the unit is already occupied.
     */
    static boolean Unit_IsTileOccupied(Unit unit) {
	    UnitInfo ui;
        int packed;
        Unit unit2;
        int speed;

        if (unit == null) return true;

        ui = g_table_unitInfo[unit.o.type];
        packed = Tile_PackTile(unit.o.position);

        speed = g_table_landscapeInfo[Map_GetLandscapeType(packed)].movementSpeed[ui.movementType];
        if (speed == 0) return true;

        if (unit.o.type == UNIT_SANDWORM || ui.movementType == MOVEMENT_WINGER) return false;

        unit2 = Unit_Get_ByPackedTile(packed);
        if (unit2 != null && unit2 != unit) {
            if (House_AreAllied(Unit_GetHouseID(unit2), Unit_GetHouseID(unit))) return true;
            if (ui.movementType != MOVEMENT_TRACKED) return true;
            if (g_table_unitInfo[unit2.o.type].movementType != MOVEMENT_FOOT) return true;
        }

        return (Structure_Get_ByPackedTile(packed) != null);
    }

    /**
     * Set the speed of a Unit.
     *
     * @param unit  The Unit to operate on.
     * @param speed The new speed of the unit (a percent value between 0 and 255).
     */
    public static void Unit_SetSpeed(Unit unit, int speed) {
        int speedPerTick;

        assert (unit != null);

        speedPerTick = 0;

        unit.speed = 0;
        unit.speedRemainder = 0;
        unit.speedPerTick = 0;

        if (unit.o.type == UNIT_HARVESTER) {
            speed = ((255 - unit.amount) * speed) / 256;
        }

        if (speed == 0 || speed >= 256) {
            unit.movingSpeed = 0;
            return;
        }

        unit.movingSpeed = speed & 0xFF;
        speed = g_table_unitInfo[unit.o.type].movingSpeedFactor * speed / 256;

        /* Units in the air don't feel the effect of gameSpeed */
        if (g_table_unitInfo[unit.o.type].movementType != MOVEMENT_WINGER) {
            speed = Tools_AdjustToGameSpeed(speed, 1, 255, false);
        }

        speedPerTick = speed << 4;
        speed = speed >> 4;

        if (speed != 0) {
            speedPerTick = 255;
        } else {
            speed = 1;
        }

        unit.speed = speed & 0xFF;
        unit.speedPerTick = speedPerTick & 0xFF;
    }

    /**
     * Create a new bullet Unit.
     *
     * @param position Where on the map this bullet Unit is created.
     * @param type The type of the new bullet Unit.
     * @param houseID The House of the new bullet Unit.
     * @param damage The hitpoints of the new bullet Unit.
     * @param target The target of the new bullet Unit.
     * @return The new created Unit, or NULL if something failed.
     */
    public static Unit Unit_CreateBullet(Tile32 position, int type, int houseID, int damage, int target) {
	    UnitInfo ui;
        Tile32 tile;

        if (!Tools_Index_IsValid(target)) return null;

        ui = g_table_unitInfo[type];
        tile = Tools_Index_GetTile(target);

        switch (type) {
            case UNIT_MISSILE_HOUSE:
            case UNIT_MISSILE_ROCKET:
            case UNIT_MISSILE_TURRET:
            case UNIT_MISSILE_DEVIATOR:
            case UNIT_MISSILE_TROOPER: {
                int orientation;
                Unit bullet;
                Unit u;

                orientation = Tile_GetDirection(position, tile);

                bullet = Unit_Create(UNIT_INDEX_INVALID, type, houseID, position, orientation);
                if (bullet == null) return null;

                Voice_PlayAtTile(ui.bulletSound, position);

                bullet.targetAttack = target;
                bullet.o.hitpoints = damage;
                bullet.currentDestination = tile;

                if (ui.flags.notAccurate) {
                    bullet.currentDestination = Tile_MoveByRandom(tile, (Tools_Random_256() & 0xF) != 0 ? Tile_GetDistance(position, tile) / 256 + 8 : Tools_Random_256() + 8, false);
                }

                bullet.fireDelay = ui.fireDistance & 0xFF;

                u = Tools_Index_GetUnit(target);
                if (u != null && g_table_unitInfo[u.o.type].movementType == MOVEMENT_WINGER) {
                    bullet.fireDelay <<= 1;
                }

                if (type == UNIT_MISSILE_HOUSE || (bullet.o.seenByHouses & (1 << g_playerHouseID)) != 0)
                    return bullet;

                Tile_RemoveFogInRadius(bullet.o.position, 2);

                return bullet;
            }

            case UNIT_BULLET:
            case UNIT_SONIC_BLAST: {
                int orientation;
                Tile32 t;
                Unit bullet;

                orientation = Tile_GetDirection(position, tile);

                t = Tile_MoveByDirection(Tile_MoveByDirection(position, 0, 32), orientation, 128);

                bullet = Unit_Create(UNIT_INDEX_INVALID, type, houseID, t, orientation);
                if (bullet == null) return null;

                if (type == UNIT_SONIC_BLAST) {
                    bullet.fireDelay = ui.fireDistance & 0xFF;
                }

                bullet.currentDestination = tile;
                bullet.o.hitpoints = damage;

                if (damage > 15) bullet.o.flags.bulletIsBig = true;

                if ((bullet.o.seenByHouses & (1 << g_playerHouseID)) != 0) return bullet;

                Tile_RemoveFogInRadius(bullet.o.position, 2);

                return bullet;
            }

            default:
                return null;
        }
    }

    /**
     * Display status text for the given unit.
     *
     * @param unit The Unit to display status text for.
     */
    public static void Unit_DisplayStatusText(Unit unit) {
	    UnitInfo ui;
        char[] buffer = new char[81];

        if (unit == null) return;

        ui = g_table_unitInfo[unit.o.type];

        if (unit.o.type == UNIT_SANDWORM) {
            snprintf(buffer, sizeof(buffer), "%s", String_Get_ByIndex(ui.o.stringID_abbrev));
        } else {
		    String houseName = g_table_houseInfo[Unit_GetHouseID(unit)].name;
            if (g_config.language == LANGUAGE_FRENCH) {
                snprintf(buffer, sizeof(buffer), "%s %s", String_Get_ByIndex(ui.o.stringID_abbrev), houseName);
            } else {
                snprintf(buffer, sizeof(buffer), "%s %s", houseName, String_Get_ByIndex(ui.o.stringID_abbrev));
            }
        }

        if (unit.o.type == UNIT_HARVESTER) {
            int stringID;

            stringID = STR_IS_D_PERCENT_FULL;

            if (unit.actionID == ACTION_HARVEST && unit.amount < 100) {
                int type = Map_GetLandscapeType(Tile_PackTile(unit.o.position));

                if (type == LST_SPICE || type == LST_THICK_SPICE) stringID = STR_IS_D_PERCENT_FULL_AND_HARVESTING;
            }

            if (unit.actionID == ACTION_MOVE && Tools_Index_GetStructure(unit.targetMove) != null) {
                stringID = STR_IS_D_PERCENT_FULL_AND_HEADING_BACK;
            } else {
                if (unit.o.script.variables[4] != 0) {
                    stringID = STR_IS_D_PERCENT_FULL_AND_AWAITING_PICKUP;
                }
            }

            if (unit.amount == 0) stringID += 4;

            {
                size_t len = strlen(buffer);
                char *s = buffer + len;

                snprintf(s, sizeof(buffer) - len, String_Get_ByIndex(stringID), unit.amount);
            }
        }

        {
            /* add a dot "." at the end of the buffer */
            size_t len = strlen(buffer);
            if (len < sizeof(buffer) - 1) {
                buffer[len] = '.';
                buffer[len + 1] = '\0';
            }
        }
        GUI_DisplayText(buffer, 2);
    }

    /**
     * Hide a unit from the viewport. Happens when a unit enters a structure or
     * gets picked up by a carry-all.
     *
     * @param unit The Unit to hide.
     */
    static void Unit_Hide(Unit unit) {
        if (unit == null) return;

        unit.o.flags.bulletIsBig = true;
        Unit_UpdateMap(0, unit);
        unit.o.flags.bulletIsBig = false;

        Script_Reset(unit.o.script, g_scriptUnit);
        Unit_UntargetMe(unit);

        unit.o.flags.isNotOnMap = true;
        Unit_HouseUnitCount_Remove(unit);
    }

    /**
     * Call a specified type of unit owned by the house to you.
     *
     * @param type The type of the Unit to find.
     * @param houseID The houseID of the Unit to find.
     * @param target To where the found Unit should move.
     * @param createCarryall Create a carryall if none found.
     * @return The found Unit, or NULL if none found.
     */
    static Unit Unit_CallUnitByType(int type, int houseID, int target, boolean createCarryall) {
        PoolFindStruct find = new PoolFindStruct();
        Unit unit = null;

        find.houseID = houseID;
        find.type = type;
        find.index = 0xFFFF;

        while (true) {
            Unit u;

            u = Unit_Find(find);
            if (u == null) break;
            if (u.o.linkedID != 0xFF) continue;
            if (u.targetMove != 0) continue;
            unit = u;
        }

        if (createCarryall && unit == null && type == UNIT_CARRYALL) {
            Tile32 position = new Tile32();

            g_validateStrictIfZero++;
            position.x = 0;
            position.y = 0;
            unit = Unit_Create(UNIT_INDEX_INVALID, type, houseID, position, 96);
            g_validateStrictIfZero--;

            if (unit != null) unit.o.flags.byScenario = true;
        }

        if (unit != null) {
            unit.targetMove = target;

            Object_Script_Variable4_Set(unit.o, target);
        }

        return unit;
    }

    /**
     * Handles what happens when the given unit enters into the given structure.
     *
     * @param unit The Unit.
     * @param s    The Structure.
     */
    public static void Unit_EnterStructure(Unit unit, Structure s) {
	    StructureInfo si;
	    UnitInfo ui;

        if (unit == null || s == null) return;

        if (unit == g_unitSelected) {
            /* ENHANCEMENT -- When a Unit enters a Structure, the last tile the Unit was on becomes selected rather than the entire Structure. */
            if (g_dune2_enhanced) {
                Map_SetSelection(Tile_PackTile(s.o.position));
            } else {
                Unit_Select(null);
            }
        }

        ui = g_table_unitInfo[unit.o.type];
        si = g_table_structureInfo[s.o.type];

        if (!unit.o.flags.allocated || s.o.hitpoints == 0) {
            Unit_Remove(unit);
            return;
        }

        unit.o.seenByHouses |= s.o.seenByHouses;
        Unit_Hide(unit);

        if (House_AreAllied(s.o.houseID, Unit_GetHouseID(unit))) {
            Structure_SetState(s, si.o.flags.busyStateIsIncoming ? STRUCTURE_STATE_READY : STRUCTURE_STATE_BUSY);

            if (s.o.type == STRUCTURE_REPAIR) {
                int countDown;

                countDown = ((ui.o.hitpoints - unit.o.hitpoints) * 256 / ui.o.hitpoints) * (ui.o.buildTime << 6) / 256;

                if (countDown > 1) {
                    s.countDown = countDown;
                } else {
                    s.countDown = 1;
                }
                unit.o.hitpoints = ui.o.hitpoints;
                unit.o.flags.isSmoking = false;
                unit.spriteOffset = 0;
            }
            unit.o.linkedID = s.o.linkedID;
            s.o.linkedID = unit.o.index & 0xFF;
            return;
        }

        if (unit.o.type == UNIT_SABOTEUR) {
            Structure_Damage(s, 500, 1);
            Unit_Remove(unit);
            return;
        }

        /* Take over the building when low on hitpoints */
        if (s.o.hitpoints < si.o.hitpoints / 4) {
            House h;

            h = House_Get_ByIndex(s.o.houseID);
            s.o.houseID = Unit_GetHouseID(unit);
            h.structuresBuilt = Structure_GetStructuresBuilt(h);

            /* ENHANCEMENT -- recalculate the power and credits for the house losing the structure. */
            if (g_dune2_enhanced) House_CalculatePowerAndCredit(h);

            h = House_Get_ByIndex(s.o.houseID);
            h.structuresBuilt = Structure_GetStructuresBuilt(h);

            if (s.o.linkedID != 0xFF) {
                Unit u = Unit_Get_ByIndex(s.o.linkedID);
                if (u != null) u.o.houseID = Unit_GetHouseID(unit);
            }

            House_CalculatePowerAndCredit(House_Get_ByIndex(s.o.houseID));
            Structure_UpdateMap(s);

            /* ENHANCEMENT -- When taking over a structure, untarget it. Else you will destroy the structure you just have taken over very easily */
            if (g_dune2_enhanced) Structure_UntargetMe(s);

            /* ENHANCEMENT -- When taking over a structure, unveil the fog around the structure. */
            if (g_dune2_enhanced) Structure_RemoveFog(s);
        } else {
            Structure_Damage(s, Math.min(unit.o.hitpoints * 2, s.o.hitpoints / 2), 1);
        }

        Object_Script_Variable4_Clear(s.o);

        Unit_Remove(unit);
    }

    /**
     * Gets the best target structure for the given unit.
     *
     * @param unit The Unit to get the best target for.
     * @param mode How to determine the best target.
     * @return The best target or NULL if none found.
     */
    static Structure Unit_FindBestTargetStructure(Unit unit, int mode) {
        Structure best = null;
        int bestPriority = 0;
        Tile32 position;
        int distance;
        PoolFindStruct find = new PoolFindStruct();

        if (unit == null) return null;

        position = Tools_Index_GetTile(unit.originEncoded);
        distance = g_table_unitInfo[unit.o.type].fireDistance << 8;

        find.houseID = HOUSE_INVALID;
        find.index = 0xFFFF;
        find.type = 0xFFFF;

        while (true) {
            Structure s;
            Tile32 curPosition = new Tile32();
            int priority;

            s = Structure_Find(find);
            if (s == null) break;
            if (s.o.type == STRUCTURE_SLAB_1x1 || s.o.type == STRUCTURE_SLAB_2x2 || s.o.type == STRUCTURE_WALL)
                continue;

            curPosition.x = s.o.position.x + g_table_structure_layoutTileDiff[g_table_structureInfo[s.o.type].layout].x;
            curPosition.y = s.o.position.y + g_table_structure_layoutTileDiff[g_table_structureInfo[s.o.type].layout].y;

            if (mode != 0 && mode != 4) {
                if (mode == 1) {
                    if (Tile_GetDistance(unit.o.position, curPosition) > distance) continue;
                } else {
                    if (mode != 2) continue;
                    if (Tile_GetDistance(position, curPosition) > distance * 2) continue;
                }
            }

            priority = Unit_GetTargetStructurePriority(unit, s);

            if (priority >= bestPriority) {
                best = s;
                bestPriority = priority;
            }
        }

        if (bestPriority == 0) return null;

        return best;
    }

    /**
     * Get the score of entering this tile from a direction.
     *
     * @param unit      The Unit to operate on.
     * @param packed    The packed tile.
     * @param orient8 The direction entering this tile from.
     * @return 256 if tile is not accessable, -1 when it is an accessable structure,
     * or a score to enter the tile otherwise.
     */
    static int Unit_GetTileEnterScore(Unit unit, int packed, int orient8) {
	    UnitInfo ui;
        Unit u;
        Structure s;
        int type;
        int res;

        if (unit == null) return 0;

        ui = g_table_unitInfo[unit.o.type];

        if (!Map_IsValidPosition(packed) && ui.movementType != MOVEMENT_WINGER) return 256;

        u = Unit_Get_ByPackedTile(packed);
        if (u != null && u != unit && unit.o.type != UNIT_SANDWORM) {
            if (unit.o.type == UNIT_SABOTEUR && unit.targetMove == Tools_Index_Encode(u.o.index, IT_UNIT))
                return 0;

            if (House_AreAllied(Unit_GetHouseID(u), Unit_GetHouseID(unit))) return 256;
            if (g_table_unitInfo[u.o.type].movementType != MOVEMENT_FOOT || (ui.movementType != MOVEMENT_TRACKED && ui.movementType != MOVEMENT_HARVESTER))
                return 256;
        }

        s = Structure_Get_ByPackedTile(packed);
        if (s != null) {
            res = Unit_IsValidMovementIntoStructure(unit, s);
            if (res == 0) return 256;
            return -res;
        }

        type = Map_GetLandscapeType(packed);

        if (g_dune2_enhanced) {
            res = g_table_landscapeInfo[type].movementSpeed[ui.movementType] * ui.movingSpeedFactor / 256;
        } else {
            res = g_table_landscapeInfo[type].movementSpeed[ui.movementType];
        }

        if (unit.o.type == UNIT_SABOTEUR && type == LST_WALL) {
            if (!House_AreAllied(g_map[packed].houseID, Unit_GetHouseID(unit))) res = 255;
        }

        if (res == 0) return 256;

        /* Check if the unit is travelling diagonally. */
        if ((orient8 & 1) != 0) {
            res -= res / 4 + res / 8;
        }

        /* 'Invert' the speed to get a rough estimate of the time taken. */
        res ^= 0xFF;

        return res;
    }

    /**
     * Gets the best target for the given unit.
     *
     * @param unit The Unit to get the best target for.
     * @param mode How to determine the best target.
     * @return The encoded index of the best target or 0 if none found.
     */
    static int Unit_FindBestTargetEncoded(Unit unit, int mode) {
        Structure s;
        Unit target;

        if (unit == null) return 0;

        s = null;

        if (mode == 4) {
            s = Unit_FindBestTargetStructure(unit, mode);

            if (s != null) return Tools_Index_Encode(s.o.index, IT_STRUCTURE);

            target = Unit_FindBestTargetUnit(unit, mode);

            if (target == null) return 0;
            return Tools_Index_Encode(target.o.index, IT_UNIT);
        }

        target = Unit_FindBestTargetUnit(unit, mode);

        if (unit.o.type != UNIT_DEVIATOR) s = Unit_FindBestTargetStructure(unit, mode);

        if (target != null && s != null) {
            int priority;

            priority = Unit_GetTargetUnitPriority(unit, target);

            if (Unit_GetTargetStructurePriority(unit, s) >= priority)
                return Tools_Index_Encode(s.o.index, IT_STRUCTURE);
            return Tools_Index_Encode(target.o.index, IT_UNIT);
        }

        if (target != null) return Tools_Index_Encode(target.o.index, IT_UNIT);
        if (s != null) return Tools_Index_Encode(s.o.index, IT_STRUCTURE);

        return 0;
    }

    /**
     * Check if the Unit belonged the the current human, and do some extra tasks.
     *
     * @param unit The Unit to operate on.
     */
    static void Unit_RemovePlayer(Unit unit) {
        if (unit == null) return;
        if (Unit_GetHouseID(unit) != g_playerHouseID) return;
        if (!unit.o.flags.allocated) return;

        unit.o.flags.allocated = false;
        Unit_RemoveFromTeam(unit);

        if (unit != g_unitSelected) return;

        if (g_selectionType == SELECTIONTYPE_TARGET) {
            g_unitActive = null;
            g_activeAction = 0xFFFF;

            GUI_ChangeSelectionType(SELECTIONTYPE_STRUCTURE);
        }

        Unit_Select(null);
    }

    /**
     * Update the map around the Unit depending on the type (entering tile, leaving, staying).
     *
     * @param type The type of action on the map.
     * @param unit The Unit doing the action.
     */
    public static void Unit_UpdateMap(int type, Unit unit) {
	    UnitInfo ui;
        Tile32 position;
        int packed;
        Tile t;
        int radius;

        if (unit == null || unit.o.flags.isNotOnMap || !unit.o.flags.used) return;

        ui = g_table_unitInfo[unit.o.type];

        if (ui.movementType == MOVEMENT_WINGER) {
            if (type != 0) {
                unit.o.flags.isDirty = true;
                g_dirtyAirUnitCount++;
            }

            Map_UpdateAround(g_table_unitInfo[unit.o.type].dimension, unit.o.position, unit, g_functions[0][type]);
            return;
        }

        position = unit.o.position;
        packed = Tile_PackTile(position);
        t = g_map[packed];

        if (t.isUnveiled || unit.o.houseID == g_playerHouseID) {
            Unit_HouseUnitCount_Add(unit, g_playerHouseID);
        } else {
            Unit_HouseUnitCount_Remove(unit);
        }

        if (type == 1) {
            if (House_AreAllied(Unit_GetHouseID(unit), g_playerHouseID) && !Map_IsPositionUnveiled(packed) && unit.o.type != UNIT_SANDWORM) {
                Tile_RemoveFogInRadius(position, 1);
            }

            if (Object_GetByPackedTile(packed) == null) {
                t.index = unit.o.index + 1;
                t.hasUnit = true;
            }
        }

        if (type != 0) {
            unit.o.flags.isDirty = true;
            g_dirtyUnitCount++;
        }

        radius = ui.dimension + 3;

        if (unit.o.flags.bulletIsBig || unit.o.flags.isSmoking || (unit.o.type == UNIT_HARVESTER && unit.actionID == ACTION_HARVEST))
            radius = 33;

        Map_UpdateAround(radius, position, unit, g_functions[1][type]);

        if (unit.o.type != UNIT_HARVESTER) return;

        /* The harvester is the only 2x1 unit, so also update tiles in behind us. */
        Map_UpdateAround(radius, unit.targetPreLast, unit, g_functions[1][type]);
        Map_UpdateAround(radius, unit.targetLast, unit, g_functions[1][type]);
    }

    /**
     * Removes the Unit from the given packed tile.
     *
     * @param unit   The Unit to remove.
     * @param packed The packed tile.
     */
    void Unit_RemoveFromTile(Unit unit, int packed) {
        Tile t = g_map[packed];

        if (t.hasUnit && Unit_Get_ByPackedTile(packed) == unit && (packed != Tile_PackTile(unit.currentDestination) || unit.o.flags.s.bulletIsBig)) {
            t.index = 0;
            t.hasUnit = false;
        }

        Map_MarkTileDirty(packed);

        Map_Update(packed, 0, false);
    }

    void Unit_AddToTile(Unit unit, int packed) {
        Map_UnveilTile(packed, Unit_GetHouseID(unit));
        Map_MarkTileDirty(packed);
        Map_Update(packed, 1, false);
    }

    /**
     * Get the priority a target structure has for a given unit. The higher the value,
     * the more serious it should look at the target.
     *
     * @param unit   The unit looking at a target.
     * @param target The structure to look at.
     * @return The priority of the target.
     */
    public static int Unit_GetTargetStructurePriority(Unit unit, Structure target) {
	    StructureInfo si;
        int priority;
        int distance;

        if (unit == null || target == null) return 0;

        if (House_AreAllied(Unit_GetHouseID(unit), target.o.houseID)) return 0;
        if ((target.o.seenByHouses & (1 << Unit_GetHouseID(unit))) == 0) return 0;

        si = g_table_structureInfo[target.o.type];
        priority = si.o.priorityBuild + si.o.priorityTarget;
        distance = Tile_GetDistanceRoundedUp(unit.o.position, target.o.position);
        if (distance != 0) priority /= distance;

        return Math.min(priority, 32000);
    }

    public static void Unit_LaunchHouseMissile(int packed) {
        Tile32 tile;
        boolean isAI;
        House h;

        if (g_unitHouseMissile == null) return;

        h = House_Get_ByIndex(g_unitHouseMissile.o.houseID);

        tile = Tile_UnpackTile(packed);
        tile = Tile_MoveByRandom(tile, 160, false);

        packed = Tile_PackTile(tile);

        isAI = g_unitHouseMissile.o.houseID != g_playerHouseID;

        Unit_Free(g_unitHouseMissile);

        Sound_Output_Feedback(0xFFFE);

        Unit_CreateBullet(h.palacePosition, g_unitHouseMissile.o.type, g_unitHouseMissile.o.houseID, 0x1F4, Tools_Index_Encode(packed, IT_TILE));

        g_houseMissileCountdown = 0;
        g_unitHouseMissile = null;

        if (isAI) {
            Sound_Output_Feedback(39);
            return;
        }

        GUI_ChangeSelectionType(SELECTIONTYPE_STRUCTURE);
    }

    /**
     * This unit is about to disappear from the map. So remove it from the house
     * statistics about allies/enemies.
     *
     * @param unit The unit to remove.
     */
    public static void Unit_HouseUnitCount_Remove(Unit unit) {
        PoolFindStruct find = new PoolFindStruct();

        if (unit == null) return;
        if (unit.o.seenByHouses == 0) return;

        find.houseID = HOUSE_INVALID;
        find.index = 0xFFFF;
        find.type = 0xFFFF;

        while (true) {
            House h;

            h = House_Find(find);
            if (h == null) break;

            if ((unit.o.seenByHouses & (1 << h.index)) == 0) continue;

            if (!House_AreAllied((int) h.index, Unit_GetHouseID(unit))) {
                h.unitCountEnemy--;
            } else {
                h.unitCountAllied--;
            }

            unit.o.seenByHouses &= ~(1 << h.index);
        }

        if (g_dune2_enhanced) unit.o.seenByHouses = 0;
    }

    /**
     * This unit is about to appear on the map. So add it from the house
     * statistics about allies/enemies, and do some other logic.
     *
     * @param unit    The unit to add.
     * @param houseID The house registering the add.
     */
    public static void Unit_HouseUnitCount_Add(Unit unit, int houseID) {
	    UnitInfo ui;
        int houseIDBit;
        House hp;
        House h;

        if (unit == null) return;

        hp = House_Get_ByIndex(g_playerHouseID);
        ui = g_table_unitInfo[unit.o.type];
        h = House_Get_ByIndex(houseID);
        houseIDBit = (1 << houseID);

        if (houseID == HOUSE_ATREIDES && unit.o.type != UNIT_SANDWORM) {
            houseIDBit |= (1 << HOUSE_FREMEN);
        }

        if ((unit.o.seenByHouses & houseIDBit) != 0 && h.flags.isAIActive) {
            unit.o.seenByHouses |= houseIDBit;
            return;
        }

        if (!ui.flags.isNormalUnit && unit.o.type != UNIT_SANDWORM) {
            return;
        }

        if ((unit.o.seenByHouses & houseIDBit) == 0) {
            if (House_AreAllied(houseID, Unit_GetHouseID(unit))) {
                h.unitCountAllied++;
            } else {
                h.unitCountEnemy++;
            }
        }

        if (ui.movementType != MOVEMENT_WINGER) {
            if (!House_AreAllied(houseID, Unit_GetHouseID(unit))) {
                h.flags.isAIActive = true;
                House_Get_ByIndex(Unit_GetHouseID(unit)).flags.isAIActive = true;
            }
        }

        if (houseID == g_playerHouseID && g_selectionType != SELECTIONTYPE_MENTAT) {
            if (unit.o.type == UNIT_SANDWORM) {
                if (hp.timerSandwormAttack == 0) {
                    if (g_musicInBattle == 0) g_musicInBattle = 1;

                    Sound_Output_Feedback(37);

                    if (g_config.language == LANGUAGE_ENGLISH) {
                        GUI_DisplayHint(STR_WARNING_SANDWORMS_SHAIHULUD_ROAM_DUNE_DEVOURING_ANYTHING_ON_THE_SAND, 105);
                    }

                    hp.timerSandwormAttack = 8;
                }
            } else if (!House_AreAllied(g_playerHouseID, Unit_GetHouseID(unit))) {
                Team t;

                if (hp.timerUnitAttack == 0) {
                    if (g_musicInBattle == 0) g_musicInBattle = 1;

                    if (unit.o.type == UNIT_SABOTEUR) {
                        Sound_Output_Feedback(12);
                    } else {
                        if (g_scenarioID < 3) {
                            PoolFindStruct find = new PoolFindStruct();
                            Structure s;
                            int feedbackID;

                            find.houseID = g_playerHouseID;
                            find.index = 0xFFFF;
                            find.type = STRUCTURE_CONSTRUCTION_YARD;

                            s = Structure_Find(find);
                            if (s != null) {
                                feedbackID = ((Orientation_Orientation256ToOrientation8(Tile_GetDirection(s.o.position, unit.o.position)) + 1) & 7) / 2 + 2;
                            } else {
                                feedbackID = 1;
                            }

                            Sound_Output_Feedback(feedbackID);
                        } else {
                            Sound_Output_Feedback(unit.o.houseID + 6);
                        }
                    }

                    hp.timerUnitAttack = 8;
                }

                t = Team_Get_ByIndex(unit.team);
                if (t != null) t.script.variables[4] = 1;
            }
        }

        if (!House_AreAllied(houseID, unit.o.houseID) && unit.actionID == ACTION_AMBUSH)
            Unit_SetAction(unit, ACTION_HUNT);

        if (unit.o.houseID == g_playerHouseID || (unit.o.houseID == HOUSE_FREMEN && g_playerHouseID == HOUSE_ATREIDES)) {
            unit.o.seenByHouses = 0xFF;
        } else {
            unit.o.seenByHouses |= houseIDBit;
        }
    }
}
