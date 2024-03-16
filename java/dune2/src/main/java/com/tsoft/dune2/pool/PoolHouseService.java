package com.tsoft.dune2.pool;

import com.tsoft.dune2.house.House;

public class PoolHouseService {

    public static final int HOUSE_INDEX_MAX = 6;      /*!< The highest possible index for any House.  */
    public static final int HOUSE_INDEX_INVALID = 0xFFFF;

    private static House[] g_houseArray;
    private static House[] g_houseFindArray;
    private static int g_houseFindCount;

    /**
     * Get a House from the pool with the indicated index.
     *
     * @param index The index of the House to get.
     * @return The House.
     */
    public static House House_Get_ByIndex(int index) {
        assert(index < HOUSE_INDEX_MAX);
        return g_houseArray[index];
    }

    /**
     * Find the first matching House based on the PoolFindStruct filter data.
     *
     * @param find A pointer to a PoolFindStruct which contains filter data and
     *   last known tried index. Calling this functions multiple times with the
     *   same 'find' parameter walks over all possible values matching the filter.
     * @return The House, or null if nothing matches (anymore).
     */
    public static House House_Find(PoolFindStruct find) {
        if (find.index >= g_houseFindCount && find.index != 0xFFFF) return null;
        find.index++; /* First, we always go to the next index */

        for (; find.index < g_houseFindCount; find.index++) {
            House h = g_houseFindArray[find.index];
            if (h != null) return h;
        }

        return null;
    }

    /**
     * Initialize the House array.
     */
    void House_Init() {
        g_houseArray = new House[HOUSE_INDEX_MAX]
        g_houseFindArray = new House[HOUSE_INDEX_MAX];
        g_houseFindCount = 0;
    }

    /**
     * Allocate a House.
     *
     * @param index The index to use.
     * @return The House allocated, or null on failure.
     */
    House House_Allocate(int index) {
        House h;

        if (index >= HOUSE_INDEX_MAX) return null;

        h = House_Get_ByIndex(index);
        if (h.flags.used) return null;

        /* Initialize the House */
        h = new House();
        h.index            = index;
        h.flags.used       = true;
        h.starportLinkedID = UNIT_INDEX_INVALID;

        g_houseFindArray[g_houseFindCount++] = h;

        return h;
    }

    /**
     * Free a House.
     */
    void House_Free(House h) {
        int i;

        /* Walk the array to find the House we are removing */
        for (i = 0; i < g_houseFindCount; i++) {
            if (g_houseFindArray[i] == h) break;
        }

        assert(i < g_houseFindCount); /* We should always find an entry */

        g_houseFindCount--;

        /* If needed, close the gap */
        if (i == g_houseFindCount) return;
        System.arraycopy(g_houseFindArray, i + 1, g_houseFindArray, i, (g_houseFindCount - i));
    }
}
