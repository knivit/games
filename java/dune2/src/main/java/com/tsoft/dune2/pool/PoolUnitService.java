package com.tsoft.dune2.pool;

import com.tsoft.dune2.house.House;
import com.tsoft.dune2.unit.Unit;

import static com.tsoft.dune2.house.HouseType.HOUSE_INVALID;
import static com.tsoft.dune2.opendune.OpenDuneService.g_validateStrictIfZero;
import static com.tsoft.dune2.pool.PoolHouseService.House_Find;
import static com.tsoft.dune2.pool.PoolHouseService.House_Get_ByIndex;
import static com.tsoft.dune2.script.ScriptService.Script_Reset;
import static com.tsoft.dune2.script.ScriptService.g_scriptUnit;
import static com.tsoft.dune2.table.TableUnitInfo.g_table_unitInfo;
import static com.tsoft.dune2.unit.MovementType.MOVEMENT_SLITHER;
import static com.tsoft.dune2.unit.MovementType.MOVEMENT_WINGER;
import static com.tsoft.dune2.unit.UnitService.Unit_GetHouseID;
import static com.tsoft.dune2.unit.UnitType.UNIT_SANDWORM;

public class PoolUnitService {

    public static final int UNIT_INDEX_MAX = 102;     /*!< The highest possible index for any Unit. */
    public static final int UNIT_INDEX_INVALID = 0xFFFF;

    private static Unit[] g_unitArray;
    private static Unit[] g_unitFindArray;
    private static int g_unitFindCount;

    /**
     * Get a Unit from the pool with the indicated index.
     *
     * @param index The index of the Unit to get.
     * @return The Unit.
     */
    public static Unit Unit_Get_ByIndex(int index) {
        assert(index < UNIT_INDEX_MAX);
        return g_unitArray[index];
    }

    /**
     * Find the first matching Unit based on the PoolFindStruct filter data.
     *
     * @param find A pointer to a PoolFindStruct which contains filter data and
     *   last known tried index. Calling this functions multiple times with the
     *   same 'find' parameter walks over all possible values matching the filter.
     * @return The Unit, or NULL if nothing matches (anymore).
     */
    public static Unit Unit_Find(PoolFindStruct find) {
        if (find.index >= g_unitFindCount && find.index != 0xFFFF) return null;
        find.index++; /* First, we always go to the next index */

        for (; find.index < g_unitFindCount; find.index++) {
            Unit u = g_unitFindArray[find.index];
            if (u == null) continue;

            if (u.o.flags.isNotOnMap && g_validateStrictIfZero == 0) continue;
            if (find.houseID != HOUSE_INVALID       && find.houseID != Unit_GetHouseID(u)) continue;
            if (find.type    != UNIT_INDEX_INVALID  && find.type    != u.o.type)  continue;

            return u;
        }

        return null;
    }

    /**
     * Initialize the Unit array.
     */
    static void Unit_Init() {
        g_unitArray = new Unit[UNIT_INDEX_MAX];
        g_unitFindArray = new Unit[UNIT_INDEX_MAX];
        g_unitFindCount = 0;
    }

    /**
     * Recount all Units, ignoring the cache array. Also set the unitCount
     *  of all houses to zero.
     */
    static void Unit_Recount() {
        int index;
        PoolFindStruct find = new PoolFindStruct(-1, -1, -1);
        House h = House_Find(find);

        while (h != null) {
            h.unitCount = 0;
            h = House_Find(find);
        }

        g_unitFindCount = 0;

        for (index = 0; index < UNIT_INDEX_MAX; index++) {
            Unit u = Unit_Get_ByIndex(index);
            if (!u.o.flags.used) continue;

            h = House_Get_ByIndex(u.o.houseID);
            h.unitCount++;

            g_unitFindArray[g_unitFindCount++] = u;
        }
    }

    /**
     * Allocate a Unit.
     *
     * @param index The index to use, or UNIT_INDEX_INVALID to find an unused index.
     * @param type The type of the new Unit.
     * @param houseID The House of the new Unit.
     * @return The Unit allocated, or NULL on failure.
     */
    public static Unit Unit_Allocate(int index, int type, int houseID) {
        House h;
        Unit u = null;

        if (type == 0xFF || houseID == 0xFF) return null;

        h = House_Get_ByIndex(houseID);
        if (h.unitCount >= h.unitCountMax) {
            if (g_table_unitInfo[type].movementType != MOVEMENT_WINGER && g_table_unitInfo[type].movementType != MOVEMENT_SLITHER) {
                if (g_validateStrictIfZero == 0) return null;
            }
        }

        if (index == 0 || index == UNIT_INDEX_INVALID) {
            int indexStart = g_table_unitInfo[type].indexStart;
            int indexEnd   = g_table_unitInfo[type].indexEnd;

            for (index = indexStart; index <= indexEnd; index++) {
                u = Unit_Get_ByIndex(index);
                if (!u.o.flags.used) break;
            }
            if (index > indexEnd) return null;
        } else {
            u = Unit_Get_ByIndex(index);
            if (u.o.flags.used) return null;
        }

        h.unitCount++;

        /* Initialize the Unit */
        u = new Unit();
        u.o.index           = index;
        u.o.type            = type;
        u.o.houseID         = houseID;
        u.o.linkedID        = 0xFF;
        u.o.flags.used      = true;
        u.o.flags.allocated = true;
        u.o.flags.isUnit    = true;
        u.o.script.delay    = 0;
        u.route[0]          = 0xFF;
        if (type == UNIT_SANDWORM) u.amount = 3;

        g_unitFindArray[g_unitFindCount++] = u;

        return u;
    }

    /**
     * Free a Unit.
     */
    static void Unit_Free(Unit u) {
        int i;

        u.o.flags.reset();

        Script_Reset(u.o.script, g_scriptUnit);

        /* Walk the array to find the Unit we are removing */
        for (i = 0; i < g_unitFindCount; i++) {
            if (g_unitFindArray[i] == u) break;
        }

        assert(i < g_unitFindCount); /* We should always find an entry */

        g_unitFindCount--;

        House h = House_Get_ByIndex(u.o.houseID);
        h.unitCount--;

        /* If needed, close the gap */
        if (i == g_unitFindCount) return;
        System.arraycopy(g_unitFindArray, i + 1, g_unitFindArray, i, (g_unitFindCount - i));
    }
}
