package com.tsoft.dune2.house;

/**
 * A House as stored in the memory.
 */
public class House {

    public int index;                                     /* The index of the House in the array. */
    public int harvestersIncoming;                        /* How many harvesters are waiting to be delivered. Only happens when we run out of Units to do it immediately. */
    public HouseFlags flags = new HouseFlags();           /* General flags of the House. */
    public int unitCount;                                 /* Amount of units owned by House. */
    public int unitCountMax;                              /* Maximum amount of units this House is allowed to have. */
    public int unitCountEnemy;                            /* Amount of units owned by enemy. */
    public int unitCountAllied;                           /* Amount of units owned by allies. */
    public long structuresBuilt;                          /* The Nth bit active means the Nth structure type is built (one or more). */
    public int credits;                                   /* Amount of credits the House currently has. */
    public int creditsStorage;                            /* Amount of credits the House can store. */
    public int powerProduction;                           /* Amount of power the House produces. */
    public int powerUsage;                                /* Amount of power the House requires. */
    public int windtrapCount;                             /* Amount of windtraps the House currently has. */
    public int creditsQuota;                              /* Quota house has to reach to win the mission. */
    public long palacePosition;                           /* Position of the Palace. */
    public int timerUnitAttack;                           /* Timer to count down when next 'unit approaching' message can be showed again. */
    public int timerSandwormAttack;                       /* Timer to count down when next 'sandworm approaching' message can be showed again. */
    public int timerStructureAttack;                      /* Timer to count down when next 'base is under attack' message can be showed again. */
    public int starportTimeLeft;                          /* How much time is left before starport transport arrives. */
    public int starportLinkedID;                          /* If there is a starport delivery, this indicates the first unit of the linked list. Otherwise it is 0xFFFF. */
    public int[][] ai_structureRebuild = new int[5][2];   /* An array for the AI which stores the type and position of a destroyed structure, for rebuilding. */
}
