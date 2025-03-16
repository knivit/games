package com.tsoft.dune2.scenario;

/**
 * Information about reinforcements in the scenario.
 */
public class Reinforcement {

    public int unitID;          /* The Unit which is already created and ready to join the game. */
    public int locationID;      /* The location where the Unit will appear. */
    public int timeLeft;        /* In how many ticks the Unit will appear. */
    public int timeBetween;     /* In how many ticks the Unit will appear again if repeat is set. */
    public int repeat;          /* If non-zero, the Unit will appear every timeBetween ticks. */
}
