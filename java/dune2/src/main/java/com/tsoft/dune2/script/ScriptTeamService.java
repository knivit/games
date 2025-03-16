package com.tsoft.dune2.script;

import com.tsoft.dune2.pool.PoolFindStruct;
import com.tsoft.dune2.team.Team;
import com.tsoft.dune2.tile.Tile32;
import com.tsoft.dune2.unit.Unit;

import static com.tsoft.dune2.gobject.GObjectService.Object_GetByPackedTile;
import static com.tsoft.dune2.gui.GuiService.GUI_DisplayText;
import static com.tsoft.dune2.house.HouseService.g_playerHouseID;
import static com.tsoft.dune2.os.EndianService.BETOH16;
import static com.tsoft.dune2.pool.PoolTeamService.Team_Get_ByIndex;
import static com.tsoft.dune2.pool.PoolUnitService.Unit_Find;
import static com.tsoft.dune2.script.Script.STACK_PEEK;
import static com.tsoft.dune2.script.ScriptService.*;
import static com.tsoft.dune2.table.TableUnitInfo.g_table_unitInfo;
import static com.tsoft.dune2.team.TeamActionType.TEAM_ACTION_KAMIKAZE;
import static com.tsoft.dune2.tile.TileService.*;
import static com.tsoft.dune2.tools.IndexType.IT_TILE;
import static com.tsoft.dune2.tools.ToolsService.*;
import static com.tsoft.dune2.unit.ActionType.*;
import static com.tsoft.dune2.unit.UnitService.*;
import static com.tsoft.dune2.unit.UnitType.UNIT_SABOTEUR;

public class ScriptTeamService {

    /**
     * Gets the amount of members in the current team.
     *
     * Stack: *none*.
     *
     * @param script The script engine to operate on.
     * @return Amount of members in current team.
     */
    static int Script_Team_GetMembers(ScriptEngine script) {
        return g_scriptCurrentTeam.members;
    }

    /**
     * Gets the variable_06 of the current team.
     *
     * Stack: *none*.
     *
     * @param script The script engine to operate on.
     * @return The variable_06 of the current team.
     */
    static int Script_Team_GetVariable6(ScriptEngine script) {
        return g_scriptCurrentTeam.minMembers;
    }

    /**
     * Gets the target for the current team.
     *
     * Stack: *none*.
     *
     * @param script The script engine to operate on.
     * @return The encoded target.
     */
    static int Script_Team_GetTarget(ScriptEngine script) {
        return g_scriptCurrentTeam.target;
    }

    /**
     * Tries to add the closest unit to the current team.
     *
     * Stack: *none*.
     *
     * @param script The script engine to operate on.
     * @return The amount of space left in current team.
     */
    static int Script_Team_AddClosestUnit(ScriptEngine script) {
        Team t;
        Unit closest = null;
        Unit closest2 = null;
        int minDistance = 0;
        int minDistance2 = 0;
        PoolFindStruct find = new PoolFindStruct();

        t = g_scriptCurrentTeam;

        if (t.members >= t.maxMembers) return 0;

        find.houseID = t.houseID;
        find.index   = 0xFFFF;
        find.type    = 0xFFFF;

        while (true) {
            Unit u;
            Team t2;
            int distance;

            u = Unit_Find(find);
            if (u == null) break;
            if (!u.o.flags.byScenario) continue;
            if (u.o.type == UNIT_SABOTEUR) continue;
            if (g_table_unitInfo[u.o.type].movementType != t.movementType) continue;
            if (u.team == 0) {
                distance = Tile_GetDistance(t.position, u.o.position);
                if (distance >= minDistance && minDistance != 0) continue;
                minDistance = distance;
                closest = u;
                continue;
            }

            t2 = Team_Get_ByIndex(u.team - 1);
            if (t2.members > t2.minMembers) continue;

            distance = Tile_GetDistance(t.position, u.o.position);
            if (distance >= minDistance2 && minDistance2 != 0) continue;
            minDistance2 = distance;
            closest2 = u;
        }

        if (closest == null) closest = closest2;
        if (closest == null) return 0;

        Unit_RemoveFromTeam(closest);
        return Unit_AddToTeam(closest, t);
    }

    /**
     * Gets the average distance between current team members, and set the
     *  position of the team to the average position.
     *
     * Stack: *none*.
     *
     * @param script The script engine to operate on.
     * @return The average distance.
     */
    static int Script_Team_GetAverageDistance(ScriptEngine script) {
        int averageX = 0;
        int averageY = 0;
        int count = 0;
        int distance = 0;
        Team t;
        PoolFindStruct find = new PoolFindStruct();

        t = g_scriptCurrentTeam;

        find.houseID = t.houseID;
        find.index   = 0xFFFF;
        find.type    = 0xFFFF;

        while (true) {
            Unit u;

            u = Unit_Find(find);
            if (u == null) break;
            if (t.index != u.team - 1) continue;
            count++;
            averageX += (u.o.position.x >> 8) & 0x3f;
            averageY += (u.o.position.y >> 8) & 0x3f;
        }

        if (count == 0) return 0;
        averageX /= count;
        averageY /= count;

        Tile_MakeXY(t.position, averageX, averageY);

        find.houseID = t.houseID;
        find.index   = 0xFFFF;
        find.type    = 0xFFFF;

        while (true) {
            Unit u;

            u = Unit_Find(find);
            if (u == null) break;
            if (t.index != u.team - 1) continue;
            distance += Tile_GetDistanceRoundedUp(u.o.position, t.position);
        }

        distance /= count;

        if (t.target == 0 || t.targetTile == 0) return distance;

        if (Tile_GetDistancePacked(Tile_PackXY(averageX, averageY), Tools_Index_GetPackedTile(t.target)) <= 10) t.targetTile = 2;

        return distance;
    }

    /**
     * Unknown function 0543.
     *
     * Stack: 1 - A distance.
     *
     * @param script The script engine to operate on.
     * @return The number of moving units.
     */
    static int Script_Team_Unknown0543(ScriptEngine script) {
        int count = 0;
        int distance;

        Team t = g_scriptCurrentTeam;
        distance = STACK_PEEK(script,  1);

        PoolFindStruct find = new PoolFindStruct();
        find.houseID = t.houseID;
        find.index = 0xFFFF;
        find.type = 0xFFFF;

        while (true) {
            int distanceUnitDest;
            int distanceUnitTeam;
            int distanceTeamDest;

            Unit u = Unit_Find(find);
            if (u == null) break;
            if (t.index != u.team - 1) continue;

            Tile32 tile = Tools_Index_GetTile(u.targetMove);
            distanceUnitTeam = Tile_GetDistanceRoundedUp(u.o.position, t.position);

            if (u.targetMove != 0) {
                distanceUnitDest = Tile_GetDistanceRoundedUp(u.o.position, tile);
                distanceTeamDest = Tile_GetDistanceRoundedUp(t.position, tile);
            } else {
                distanceUnitDest = 64;
                distanceTeamDest = 64;
            }

            if ((distanceUnitDest < distanceTeamDest && (distance + 2) < distanceUnitTeam) || (distanceUnitDest >= distanceTeamDest && distanceUnitTeam > distance)) {
                Unit_SetAction(u, ACTION_MOVE);

                tile = Tile_MoveByRandom(t.position, distance << 4, true);

                Unit_SetDestination(u, Tools_Index_Encode(Tile_PackTile(tile), IT_TILE));
                count++;
                continue;
            }

            Unit_SetAction(u, ACTION_GUARD);
        }

        return count;
    }

    /**
     * Gets the best target for the current team.
     *
     * Stack: *none*.
     *
     * @param script The script engine to operate on.
     * @return The encoded index of the best target or 0 if none found.
     */
    static int Script_Team_FindBestTarget(ScriptEngine script) {
        Team t = g_scriptCurrentTeam;

        PoolFindStruct find = new PoolFindStruct();
        find.houseID = t.houseID;
        find.index = 0xFFFF;
        find.type = 0xFFFF;

        while (true) {
            Unit u = Unit_Find(find);
            if (u == null) break;
            if (u.team - 1 != t.index) continue;

            int target = Unit_FindBestTargetEncoded(u, t.action == TEAM_ACTION_KAMIKAZE ? 4 : 0);
            if (target == 0) continue;
            if (t.target == target) return target;

            t.target = target;
            t.targetTile = Tile_GetTileInDirectionOf(Tile_PackTile(u.o.position), Tools_Index_GetPackedTile(target));
            return target;
        }

        return 0;
    }

    /**
     * Loads a new script for the current team.
     *
     * Stack: 1 - The script type.
     *
     * @param script The script engine to operate on.
     * @return The value 0. Always.
     */
    static int Script_Team_Load(ScriptEngine script) {
        Team t = g_scriptCurrentTeam;
        int type = STACK_PEEK(script, 1);

        if (t.action == type) return 0;

        t.action = type;

        Script_Reset(t.script, g_scriptTeam);
        Script_Load(t.script, type & 0xFF);

        return 0;
    }

    /**
     * Loads a new script for the current team.
     *
     * Stack: *none*.
     *
     * @param script The script engine to operate on.
     * @return The value 0. Always.
     */
    static int Script_Team_Load2(ScriptEngine script) {
        Team t = g_scriptCurrentTeam;
        int type = t.actionStart;

        if (t.action == type) return 0;

        t.action = type;

        Script_Reset(t.script, g_scriptTeam);
        Script_Load(t.script, type & 0xFF);

        return 0;
    }

    /**
     * Unknown function 0788.
     *
     * Stack: *none*.
     *
     * @param script The script engine to operate on.
     * @return The value 0. Always.
     */
    static int Script_Team_Unknown0788(ScriptEngine script) {
        Team t = g_scriptCurrentTeam;
        if (t.target == 0) return 0;

        Tile32 tile = Tools_Index_GetTile(t.target);

        PoolFindStruct find = new PoolFindStruct();
        find.houseID = t.houseID;
        find.index = 0xFFFF;
        find.type = 0xFFFF;

        while (true) {
            Unit u = Unit_Find(find);
            if (u == null) break;
            if (u.team - 1 != t.index) continue;
            if (t.target == 0) {
                Unit_SetAction(u, ACTION_GUARD);
                continue;
            }

            int distance = g_table_unitInfo[u.o.type].fireDistance << 8;
            if (u.actionID == ACTION_ATTACK && u.targetAttack == t.target) {
                if (u.targetMove != 0) continue;
                if (Tile_GetDistance(u.o.position, tile) >= distance) continue;
            }

            if (u.actionID != ACTION_ATTACK) Unit_SetAction(u, ACTION_ATTACK);

            int orientation = (Tile_GetDirection(tile, u.o.position) & 0xC0) + Tools_RandomLCG_Range(0, 127);
            if (orientation < 0) orientation += 256;

            int packed = Tile_PackTile(Tile_MoveByDirection(tile, orientation, distance));

            if (Object_GetByPackedTile(packed) == null) {
                Unit_SetDestination(u, Tools_Index_Encode(packed, IT_TILE));
            } else {
                Unit_SetDestination(u, Tools_Index_Encode(Tile_PackTile(tile), IT_TILE));
            }

            Unit_SetTarget(u, t.target);
        }

        return 0;
    }

    /**
     * Draws a string.
     *
     * Stack: 1 - The index of the string to draw.
     *        2-4 - The arguments for the string.
     *
     * @param script The script engine to operate on.
     * @return The value 0. Always.
     */
    public static int Script_Team_DisplayText(ScriptEngine script) {
        Team t = g_scriptCurrentTeam;
        if (t.houseID == g_playerHouseID) return 0;

        int offset = BETOH16(*(script.scriptInfo.text + STACK_PEEK(script, 1)));
        String text = (char *)script.scriptInfo.text + offset;

        GUI_DisplayText(text, 0, STACK_PEEK(script, 2), STACK_PEEK(script, 3), STACK_PEEK(script, 4));

        return 0;
    }
}
