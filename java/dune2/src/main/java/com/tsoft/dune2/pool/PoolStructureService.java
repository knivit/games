package com.tsoft.dune2.pool;

import com.tsoft.dune2.house.House;
import com.tsoft.dune2.structure.Structure;

import static com.tsoft.dune2.house.HouseType.HOUSE_INVALID;
import static com.tsoft.dune2.opendune.OpenDuneService.g_validateStrictIfZero;
import static com.tsoft.dune2.pool.PoolHouseService.House_Find;
import static com.tsoft.dune2.script.ScriptService.Script_Reset;
import static com.tsoft.dune2.script.ScriptService.g_scriptStructure;
import static com.tsoft.dune2.structure.StructureType.*;

public class PoolStructureService {

    public static final int STRUCTURE_INDEX_MAX_SOFT = 79;     /*!< The highest possible index for normal Structure. */
    public static final int STRUCTURE_INDEX_MAX_HARD = 82;     /*!< The highest possible index for any Structure. */

    public static final int STRUCTURE_INDEX_WALL     = 79;     /*!< All walls are are put under index 79. */
    public static final int STRUCTURE_INDEX_SLAB_2x2 = 80;     /*!< All 2x2 slabs are put under index 80. */
    public static final int STRUCTURE_INDEX_SLAB_1x1 = 81;     /*!< All 1x1 slabs are put under index 81. */

    public static final int STRUCTURE_INDEX_INVALID  = 0xFFFF;

    private static Structure[] g_structureArray;
    private static Structure[] g_structureFindArray;
    private static int g_structureFindCount;

    /**
     * Get a Structure from the pool with the indicated index.
     *
     * @param index The index of the Structure to get.
     * @return The Structure.
     */
    public static Structure Structure_Get_ByIndex(int index) {
        assert(index < STRUCTURE_INDEX_MAX_HARD);
        return g_structureArray[index];
    }

    /**
     * Find the first matching Structure based on the PoolFindStruct filter data.
     *
     * @param find A pointer to a PoolFindStruct which contains filter data and
     *   last known tried index. Calling this functions multiple times with the
     *   same 'find' parameter walks over all possible values matching the filter.
     * @return The Structure, or null if nothing matches (anymore).
     */
    public static Structure Structure_Find(PoolFindStruct find) {
        if (find.index >= g_structureFindCount + 3 && find.index != 0xFFFF) return null;
        find.index++; /* First, we always go to the next index */

        assert(g_structureFindCount <= STRUCTURE_INDEX_MAX_SOFT);
        for (; find.index < g_structureFindCount + 3; find.index++) {
            Structure s = null;

            if (find.index < g_structureFindCount) {
                s = g_structureFindArray[find.index];
            } else {
                /* There are 3 special structures that are never in the Find array */
                assert(find.index - g_structureFindCount < 3);
                switch (find.index - g_structureFindCount) {
                    case 0:
                        s = Structure_Get_ByIndex(STRUCTURE_INDEX_WALL);
                        if (s.o.index != STRUCTURE_INDEX_WALL) continue;
                        break;

                    case 1:
                        s = Structure_Get_ByIndex(STRUCTURE_INDEX_SLAB_2x2);
                        if (s.o.index != STRUCTURE_INDEX_SLAB_2x2) continue;
                        break;

                    case 2:
                        s = Structure_Get_ByIndex(STRUCTURE_INDEX_SLAB_1x1);
                        if (s.o.index != STRUCTURE_INDEX_SLAB_1x1) continue;
                        break;
                }
            }
            if (s == null) continue;

            if (s.o.flags.isNotOnMap && g_validateStrictIfZero == 0) continue;
            if (find.houseID != HOUSE_INVALID           && find.houseID != s.o.houseID) continue;
            if (find.type    != STRUCTURE_INDEX_INVALID && find.type    != s.o.type)  continue;

            return s;
        }

        return null;
    }

    /**
     * Initialize the Structure array.
     */
    static void Structure_Init()  {
        g_structureArray = new Structure[STRUCTURE_INDEX_MAX_HARD];
        g_structureFindArray = new Structure[STRUCTURE_INDEX_MAX_SOFT];
        g_structureFindCount = 0;
    }

    /**
     * Recount all Structures, ignoring the cache array. Also set the structureCount
     *  of all houses to zero.
     */
    public static void Structure_Recount() {
        int index;
        PoolFindStruct find = new PoolFindStruct( -1, -1, -1);
        House h = House_Find(find);

        while (h != null) {
            h.unitCount = 0;
            h = House_Find(find);
        }

        g_structureFindCount = 0;

        for (index = 0; index < STRUCTURE_INDEX_MAX_SOFT; index++) {
            Structure s = Structure_Get_ByIndex(index);
            if (s.o.flags.used) g_structureFindArray[g_structureFindCount++] = s;
        }
    }

    /**
     * Allocate a Structure.
     *
     * @param index The index to use, or STRUCTURE_INDEX_INVALID to find an unused index.
     * @param type The type of the new Structure.
     * @return The Structure allocated, or null on failure.
     */
    public static Structure Structure_Allocate(int index, int type) {
        Structure s = null;

        switch (type) {
            case STRUCTURE_SLAB_1x1:
                index = STRUCTURE_INDEX_SLAB_1x1;
                s = Structure_Get_ByIndex(index);
                break;

            case STRUCTURE_SLAB_2x2:
                index = STRUCTURE_INDEX_SLAB_2x2;
                s = Structure_Get_ByIndex(index);
                break;

            case STRUCTURE_WALL:
                index = STRUCTURE_INDEX_WALL;
                s = Structure_Get_ByIndex(index);
                break;

            default:
                if (index == STRUCTURE_INDEX_INVALID) {
                    /* Find the first unused index */
                    for (index = 0; index < STRUCTURE_INDEX_MAX_SOFT; index++) {
                        s = Structure_Get_ByIndex(index);
                        if (!s.o.flags.used) break;
                    }
                    if (index == STRUCTURE_INDEX_MAX_SOFT) return null;
                } else {
                    s = Structure_Get_ByIndex(index);
                    if (s.o.flags.used) return null;
                }

                g_structureFindArray[g_structureFindCount++] = s;
                break;
        }

        assert(s != null);

        /* Initialize the Structure */
        s = new Structure();
        s.o.index             = index;
        s.o.type              = type;
        s.o.linkedID          = 0xFF;
        s.o.flags.used        = true;
        s.o.flags.allocated   = true;
        s.o.script.delay      = 0;

        return s;
    }

    /**
     * Free a Structure.
     *
     * @param s The address of the Structure to free.
     */
    public static void Structure_Free(Structure s) {
        int i;

        s.o.flags.reset();

        Script_Reset(s.o.script, g_scriptStructure);

        if (s.o.type == STRUCTURE_SLAB_1x1 || s.o.type == STRUCTURE_SLAB_2x2 || s.o.type == STRUCTURE_WALL) return;

        /* Walk the array to find the Structure we are removing */
        assert(g_structureFindCount <= STRUCTURE_INDEX_MAX_SOFT);

        for (i = 0; i < g_structureFindCount; i++) {
            if (g_structureFindArray[i] == s) break;
        }

        assert(i < g_structureFindCount); /* We should always find an entry */

        g_structureFindCount--;

        /* If needed, close the gap */
        if (i == g_structureFindCount) return;
        System.arraycopy(g_structureFindArray, i + 1, g_structureFindArray, i, (g_structureFindCount - i));
    }
}
