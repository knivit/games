package com.tsoft.dune2.pool;

/**
 * To find a pool item of a given type/house, this struct is used. The result
 *  is also written back in this struct.
 */
public class PoolFindStruct {

    public int houseID; /* House to search for, or HOUSE_INVALID for all. */
    public int type;    /* Type to search for, or -1 for all. */
    public int index;   /* Last index of search, or -1 to start from begin. */

    public PoolFindStruct() { }

    public PoolFindStruct(int houseID, int type, int index) {
        this.houseID = houseID;
        this.type = type;
        this.index = index;
    }
}
