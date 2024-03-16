package com.tsoft.dune2.script;

import com.tsoft.dune2.pool.PoolFindStruct;
import com.tsoft.dune2.team.Team;
import com.tsoft.dune2.tile.Tile32;
import com.tsoft.dune2.unit.Unit;

import static com.tsoft.dune2.gobject.GObjectService.Object_GetByPackedTile;
import static com.tsoft.dune2.gui.GuiService.GUI_DisplayText;
import static com.tsoft.dune2.script.ScriptService.Script_Load;
import static com.tsoft.dune2.script.ScriptService.Script_Reset;
import static com.tsoft.dune2.team.TeamActionType.TEAM_ACTION_KAMIKAZE;
import static com.tsoft.dune2.tile.TileService.*;
import static com.tsoft.dune2.tools.IndexType.IT_TILE;
import static com.tsoft.dune2.tools.ToolsService.Tools_Index_GetPackedTile;
import static com.tsoft.dune2.tools.ToolsService.Tools_Index_GetTile;
import static com.tsoft.dune2.unit.ActionType.*;
import static com.tsoft.dune2.unit.UnitService.Unit_SetAction;
import static com.tsoft.dune2.unit.UnitService.Unit_SetDestination;
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
    int Script_Team_GetMembers(ScriptEngine script) {
        VARIABLE_NOT_USED(script);
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
    int Script_Team_GetVariable6(ScriptEngine script) {
        VARIABLE_NOT_USED(script);
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
    int Script_Team_GetTarget(ScriptEngine script) {
        VARIABLE_NOT_USED(script);
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
    int Script_Team_AddClosestUnit(ScriptEngine script) {
        Team t;
        Unit closest = null;
        Unit closest2 = null;
        int minDistance = 0;
        int minDistance2 = 0;
        PoolFindStruct find = new PoolFindStruct();

        VARIABLE_NOT_USED(script);

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
    int Script_Team_GetAverageDistance(ScriptEngine script) {
        int averageX = 0;
        int averageY = 0;
        int count = 0;
        int distance = 0;
        Team t;
        PoolFindStruct find = new PoolFindStruct();

        VARIABLE_NOT_USED(script);

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
    int Script_Team_Unknown0543(ScriptEngine script) {
        Team t;
        int count = 0;
        int distance;
        PoolFindStruct find;

        t = g_scriptCurrentTeam;
        distance = STACK_PEEK(1);

        find.houseID = t.houseID;
        find.index   = 0xFFFF;
        find.type    = 0xFFFF;

        while (true) {
            Unit u;
            Tile32 tile;
            int distanceUnitDest;
            int distanceUnitTeam;
            int distanceTeamDest;

            u = Unit_Find(find);
            if (u == null) break;
            if (t.index != u.team - 1) continue;

            tile = Tools_Index_GetTile(u.targetMove);
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
    int Script_Team_FindBestTarget(ScriptEngine script) {
        Team t;
        PoolFindStruct find = new PoolFindStruct();

        VARIABLE_NOT_USED(script);

        t = g_scriptCurrentTeam;

        find.houseID = t.houseID;
        find.index   = 0xFFFF;
        find.type    = 0xFFFF;

        while (true) {
            Unit u;
            int target;

            u = Unit_Find(&find);
            if (u == null) break;
            if (u.team - 1 != t.index) continue;
            target = Unit_FindBestTargetEncoded(u, t.action == TEAM_ACTION_KAMIKAZE ? 4 : 0);
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
    int Script_Team_Load(ScriptEngine script) {
        Team t;
        int type;

        t = g_scriptCurrentTeam;
        type = STACK_PEEK(1);

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
    int Script_Team_Load2(ScriptEngine script) {
        Team t;
        int type;

        VARIABLE_NOT_USED(script);

        t = g_scriptCurrentTeam;
        type = t.actionStart;

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
    int Script_Team_Unknown0788(ScriptEngine script) {
        Team t;
        Tile32 tile;
        PoolFindStruct find = new PoolFindStruct();

        VARIABLE_NOT_USED(script);

        t = g_scriptCurrentTeam;
        if (t.target == 0) return 0;

        tile = Tools_Index_GetTile(t.target);

        find.houseID = t.houseID;
        find.index   = 0xFFFF;
        find.type    = 0xFFFF;

        while (true) {
            Unit u;
            int distance;
            int packed;
            int orientation;

            u = Unit_Find(find);
            if (u == null) break;
            if (u.team - 1 != t.index) continue;
            if (t.target == 0) {
                Unit_SetAction(u, ACTION_GUARD);
                continue;
            }

            distance = g_table_unitInfo[u.o.type].fireDistance << 8;
            if (u.actionID == ACTION_ATTACK && u.targetAttack == t.target) {
                if (u.targetMove != 0) continue;
                if (Tile_GetDistance(u.o.position, tile) >= distance) continue;
            }

            if (u.actionID != ACTION_ATTACK) Unit_SetAction(u, ACTION_ATTACK);

            orientation = (Tile_GetDirection(tile, u.o.position) & 0xC0) + Tools_RandomLCG_Range(0, 127);
            if (orientation < 0) orientation += 256;

            packed = Tile_PackTile(Tile_MoveByDirection(tile, orientation, distance));

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
    int Script_Team_DisplayText(ScriptEngine script) {
        Team t;
        String text;
        int offset;

        t = g_scriptCurrentTeam;
        if (t.houseID == g_playerHouseID) return 0;

        offset = BETOH16(*(script.scriptInfo.text + STACK_PEEK(1)));
        text = (char *)script.scriptInfo.text + offset;

        GUI_DisplayText(text, 0, STACK_PEEK(2), STACK_PEEK(3), STACK_PEEK(4));

        return 0;
    }
}
