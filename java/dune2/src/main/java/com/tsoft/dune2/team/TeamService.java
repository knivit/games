package com.tsoft.dune2.team;

import com.tsoft.dune2.house.House;
import com.tsoft.dune2.pool.PoolFindStruct;

import static com.tsoft.dune2.house.HouseType.HOUSE_INVALID;
import static com.tsoft.dune2.opendune.OpenDuneService.g_dune2_enhanced;
import static com.tsoft.dune2.pool.PoolHouseService.House_Get_ByIndex;
import static com.tsoft.dune2.pool.PoolTeamService.Team_Allocate;
import static com.tsoft.dune2.pool.PoolTeamService.Team_Find;
import static com.tsoft.dune2.script.ScriptService.*;
import static com.tsoft.dune2.team.TeamActionType.TEAM_ACTION_INVALID;
import static com.tsoft.dune2.team.TeamActionType.TEAM_ACTION_MAX;
import static com.tsoft.dune2.timer.TimerService.g_timerGame;
import static com.tsoft.dune2.tools.ToolsService.Tools_Random_256;

public class TeamService {

    static long s_tickTeamGameLoop = 0; /*!< Indicates next time the GameLoop function is executed. */

    /**
     * Loop over all teams, performing various of tasks.
     */
    public static void GameLoop_Team() {
        PoolFindStruct find = new PoolFindStruct();

        if (s_tickTeamGameLoop > g_timerGame) return;
        s_tickTeamGameLoop = g_timerGame + (Tools_Random_256() & 7) + 5;

        find.houseID = HOUSE_INVALID;
        find.index   = 0xFFFF;
        find.type    = 0xFFFF;

        g_scriptCurrentObject    = null;
        g_scriptCurrentUnit      = null;
        g_scriptCurrentStructure = null;

        while (true) {
            Team t;
            House h;

            t = Team_Find(find);
            if (t == null) break;

            h = House_Get_ByIndex(t.houseID);

            g_scriptCurrentTeam = t;

            if (!h.flags.isAIActive) continue;

            if (t.script.delay != 0) {
                t.script.delay--;
                continue;
            }

            if (!Script_IsLoaded(t.script)) continue;

            if (!Script_Run(t.script)) {
                /* ENHANCEMENT -- Dune2 aborts all other teams if one gives a script error. This doesn't seem correct */
                if (g_dune2_enhanced) continue;
                break;
            }
        }
    }

    /**
     * Create a new Team.
     *
     * @param houseID The House of the new Team.
     * @param teamActionType The teamActionType of the new Team.
     * @param movementType The movementType of the new Team.
     * @param minMembers The minimum amount of members in the new Team.
     * @param maxMembers The maximum amount of members in the new Team.
     * @return The new created Team, or NULL if something failed.
     */
    public static Team Team_Create(int houseID, int teamActionType, int movementType, int minMembers, int maxMembers) {
        Team t;

        t = Team_Allocate(0xFFFF);

        if (t == null) return null;
        t.flags.used  = true;
        t.houseID     = houseID;
        t.action      = teamActionType;
        t.actionStart = teamActionType;
        t.movementType = movementType;
        t.minMembers  = minMembers;
        t.maxMembers  = maxMembers;

        Script_Reset(t.script, g_scriptTeam);
        Script_Load(t.script, teamActionType);

        t.script.delay = 0;

        return t;
    }

    /**
     * Convert the name of a team action to the type value of that team action, or
     *  TEAM_ACTION_INVALID if not found.
     */
    static int Team_ActionStringToType(String name) {
        int type;
        if (name == null) return TEAM_ACTION_INVALID;

        for (type = 0; type < TEAM_ACTION_MAX; type++) {
            if (strcasecmp(g_table_teamActionName[type], name) == 0) return type;
        }

        return TEAM_ACTION_INVALID;
    }
}
