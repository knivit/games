package com.tsoft.dune2.house;

import static com.tsoft.dune2.house.HouseType.*;

/**
 * Flags used to indicate houses in a bitmask.
 */
public class HouseFlag {

    public static int FLAG_HOUSE_HARKONNEN    = 1 << HOUSE_HARKONNEN; /* 0x01 */
    public static int FLAG_HOUSE_ATREIDES     = 1 << HOUSE_ATREIDES;  /* 0x02 */
    public static int FLAG_HOUSE_ORDOS        = 1 << HOUSE_ORDOS;     /* 0x04 */
    public static int FLAG_HOUSE_FREMEN       = 1 << HOUSE_FREMEN;    /* 0x08 */
    public static int FLAG_HOUSE_SARDAUKAR    = 1 << HOUSE_SARDAUKAR; /* 0x10 */
    public static int FLAG_HOUSE_MERCENARY    = 1 << HOUSE_MERCENARY; /* 0x20 */

    public static int FLAG_HOUSE_ALL          = FLAG_HOUSE_MERCENARY | FLAG_HOUSE_SARDAUKAR | FLAG_HOUSE_FREMEN | FLAG_HOUSE_ORDOS | FLAG_HOUSE_ATREIDES | FLAG_HOUSE_HARKONNEN;
}
