package com.tsoft.dune2.unit;

/**
 * Static information per Action type.
 */
public class ActionInfo {

    public int stringID;                                     /* StringID of Action name. */
	public String name;                                      /* Name of Action. */
    public int switchType;                                   /* When going to new mode, how do we handle it? 0: queue if needed, 1: change immediately, 2: run via subroutine. */
    public int selectionType;                                /* Selection type attached to this action. */
    public int soundID;                                      /* The sound played when unit is a Foot unit. */

    public ActionInfo(int stringID, String name, int switchType, int selectionType, int soundID) {
        this.stringID = stringID;
        this.name = name;
        this.switchType = switchType;
        this.selectionType = selectionType;
        this.soundID = soundID;
    }
}
