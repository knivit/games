package com.tsoft.dune2.gobject;

import com.tsoft.dune2.script.ScriptEngine;
import com.tsoft.dune2.tile.Tile32;

/**
 * Data common to Structure and Unit.
 */
public class GObject {

    public int index;                                           /*!< The index of the Structure/Unit in the array. */
    public int type;                                            /*!< Type of Structure/Unit. */
    public int linkedID;                                        /*!< Structure/Unit we are linked to, or 0xFF if we are not linked to a Structure/Unit. */
    public GObjectFlags flags = new GObjectFlags();              /*!< General flags of the Structure/Unit. */
    public int houseID;                                         /*!< House of Structure. */
    public int seenByHouses;                                    /*!< Bitmask of which houses have seen this object. */
    public Tile32 position = new Tile32();                       /*!< Position on the map. */
    public int hitpoints;                                       /*!< Current hitpoints left. */
    public ScriptEngine script;                                  /*!< The script engine instance of this Structure. */
}
