package com.tsoft.dune2.pool;

import com.tsoft.dune2.team.Team;
import com.tsoft.dune2.team.TeamFlags;

import java.util.Arrays;

import static com.tsoft.dune2.house.HouseType.HOUSE_INVALID;

public class PoolTeamService {

    public static final int TEAM_INDEX_MAX = 16;             /*!< The highest possible index for any Team.  */
    public static final int TEAM_INDEX_INVALID = 0xFFFF;

    static Team[] g_teamArray = new Team[TEAM_INDEX_MAX];
    static Team[] g_teamFindArray = new Team[TEAM_INDEX_MAX];
    static int g_teamFindCount;

    /**
     * Get a Team from the pool with the indicated index.
     *
     * @param index The index of the Team to get.
     * @return The Team.
     */
    public static Team Team_Get_ByIndex(int index) {
        assert(index < TEAM_INDEX_MAX);
        return g_teamArray[index];
    }

    /**
     * Find the first matching Team based on the PoolFindStruct filter data.
     *
     * @param find A pointer to a PoolFindStruct which contains filter data and
     *   last known tried index. Calling this functions multiple times with the
     *   same 'find' parameter walks over all possible values matching the filter.
     * @return The Team, or null if nothing matches (anymore).
     */
    public static Team Team_Find(PoolFindStruct find) {
        if (find.index >= g_teamFindCount && find.index != 0xFFFF) return null;
        find.index++; /* First, we always go to the next index */

        for (; find.index < g_teamFindCount; find.index++) {
            Team t = g_teamFindArray[find.index];
            if (t == null) continue;

            if (find.houseID == HOUSE_INVALID || find.houseID == t.houseID) return t;
        }

        return null;
    }

    /**
     * Initialize the Team array.
     */
    static void Team_Init() {
        Arrays.fill(g_teamArray, null);
        Arrays.fill(g_teamFindArray, null);
        g_teamFindCount = 0;
    }

    /**
     * Recount all Teams, ignoring the cache array.
     */
    public static void Team_Recount() {
        int index;

        g_teamFindCount = 0;

        for (index = 0; index < TEAM_INDEX_MAX; index++) {
            Team t = Team_Get_ByIndex(index);
            if (t.flags.used) g_teamFindArray[g_teamFindCount++] = t;
        }
    }

    /**
     * Allocate a Team.
     *
     * @param index The index to use, or TEAM_INDEX_INVALID to find an unused index.
     * @return The Team allocated, or null on failure.
     */
    public static Team Team_Allocate(int index) {
        Team t = null;

        if (index == TEAM_INDEX_INVALID) {
            /* Find the first unused index */
            for (index = 0; index < TEAM_INDEX_MAX; index++) {
                t = Team_Get_ByIndex(index);
                if (!t.flags.used) break;
            }
            if (index == TEAM_INDEX_MAX) return null;
        } else {
            t = Team_Get_ByIndex(index);
            if (t.flags.used) return null;
        }
        assert(t != null);

        /* Initialize the Team */
        t.reset();
        t.index      = index;
        t.flags.used = true;

        g_teamFindArray[g_teamFindCount++] = t;

        return t;
    }

    /**
     * Free a Team.
     */
    static void Team_Free(Team t) {
        int i;

        t.flags = new TeamFlags();

        /* Walk the array to find the Team we are removing */
        for (i = 0; i < g_teamFindCount; i++) {
            if (g_teamFindArray[i] == t) break;
        }
        assert(i < g_teamFindCount); /* We should always find an entry */

        g_teamFindCount--;

        /* If needed, close the gap */
        if (i == g_teamFindCount) return;
        memmove(g_teamFindArray[i], g_teamFindArray[i + 1], (g_teamFindCount - i) * sizeof(g_teamFindArray[0]));
    }
}
