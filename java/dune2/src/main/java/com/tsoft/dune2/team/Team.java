package com.tsoft.dune2.team;

import com.tsoft.dune2.script.ScriptEngine;
import com.tsoft.dune2.tile.Tile32;

/**
 * An Team as stored in the memory.
 */
public class Team {

    public int index;                                           /*!< The index of the Team in the array. */
    public TeamFlags flags = new TeamFlags();                   /*!< General flags of the Team. */
    public int members;                                         /*!< Amount of members in Team. */
    public int minMembers;                                      /*!< Minimum amount of members in Team. */
    public int maxMembers;                                      /*!< Maximum amount of members in Team. */
    public int movementType;                                    /*!< MovementType of Team. */
    public int action;                                          /*!< Current TeamActionType of Team. */
    public int actionStart;                                     /*!< The TeamActionType Team starts with. */
    public int  houseID;                                        /*!< House of Team. */
    public Tile32 position = new Tile32();                      /*!< Position on the map. */
    public int targetTile;                                      /*!< Current target tile around the target. Only used as a bool, so either set or not. */
    public int target;                                          /*!< Current target of team (encoded index). */
    public ScriptEngine script;                                 /*!< The script engine instance of this Team. */
}
