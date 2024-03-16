package com.tsoft.dune2.unit;

/**
 * Static information per Action type.
 */
public class ActionInfo {

    int stringID;                                     /* StringID of Action name. */
	String name;                                      /* Name of Action. */
    int switchType;                                   /* When going to new mode, how do we handle it? 0: queue if needed, 1: change immediately, 2: run via subroutine. */
    int selectionType;                                /* Selection type attached to this action. */
    int soundID;                                      /* The sound played when unit is a Foot unit. */
}
