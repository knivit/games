package com.tsoft.dune2.gui;

import com.tsoft.dune2.gobject.GObjectInfo;

/**
 * Factory Window Item struct.
 */
public class FactoryWindowItem {

    public int objectType;                                      /*!< Which object is this item. */
    public int amount;                                          /*!< How many are available. */
    public int credits;                                         /*!< What is the current price. */
    public int sortPriority;                                    /*!< The sorting priority. */
    public GObjectInfo objectInfo = new GObjectInfo();          /*!< The ObjectInfo of the item. */
}
