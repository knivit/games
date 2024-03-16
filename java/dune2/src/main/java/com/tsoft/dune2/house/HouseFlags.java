package com.tsoft.dune2.house;

/**
 * Flags for House structure
 */
public class HouseFlags {

    public boolean used;                      /* The House is in use (no longer free in the pool). */
    public boolean human;                     /* The House is controlled by a human. */
    public boolean doneFullScaleAttack;       /* The House did his one time attack the human with everything we have. */
    public boolean isAIActive;                /* The House has been seen by the human, and everything now becomes active (Team attack, house missiles, rebuilding, ..). */
    public boolean radarActivated;            /* The radar is activated. */
    public boolean unused_0020;               /* Unused */
}
