package com.tsoft.dune2.unit;

import static com.tsoft.dune2.unit.UnitType.*;

/**
 * Flags used to indicate units in a bitmask.
 */
public class UnitFlag {

    public static int FLAG_UNIT_CARRYALL         = 1 << UNIT_CARRYALL;         /* 0x______01 */
    public static int FLAG_UNIT_ORNITHOPTER      = 1 << UNIT_ORNITHOPTER;      /* 0x______02 */
    public static int FLAG_UNIT_INFANTRY         = 1 << UNIT_INFANTRY;         /* 0x______04 */
    public static int FLAG_UNIT_TROOPERS         = 1 << UNIT_TROOPERS;         /* 0x______08 */
    public static int FLAG_UNIT_SOLDIER          = 1 << UNIT_SOLDIER;          /* 0x______10 */
    public static int FLAG_UNIT_TROOPER          = 1 << UNIT_TROOPER;          /* 0x______20 */
    public static int FLAG_UNIT_SABOTEUR         = 1 << UNIT_SABOTEUR;         /* 0x______40 */
    public static int FLAG_UNIT_LAUNCHER         = 1 << UNIT_LAUNCHER;         /* 0x______80 */
    public static int FLAG_UNIT_DEVIATOR         = 1 << UNIT_DEVIATOR;         /* 0x____01__ */
    public static int FLAG_UNIT_TANK             = 1 << UNIT_TANK;             /* 0x____02__ */
    public static int FLAG_UNIT_SIEGE_TANK       = 1 << UNIT_SIEGE_TANK;       /* 0x____04__ */
    public static int FLAG_UNIT_DEVASTATOR       = 1 << UNIT_DEVASTATOR;       /* 0x____08__ */
    public static int FLAG_UNIT_SONIC_TANK       = 1 << UNIT_SONIC_TANK;       /* 0x____10__ */
    public static int FLAG_UNIT_TRIKE            = 1 << UNIT_TRIKE;            /* 0x____20__ */
    public static int FLAG_UNIT_RAIDER_TRIKE     = 1 << UNIT_RAIDER_TRIKE;     /* 0x____40__ */
    public static int FLAG_UNIT_QUAD             = 1 << UNIT_QUAD;             /* 0x____80__ */
    public static int FLAG_UNIT_HARVESTER        = 1 << UNIT_HARVESTER;        /* 0x__01____ */
    public static int FLAG_UNIT_MCV              = 1 << UNIT_MCV;              /* 0x__02____ */
    public static int FLAG_UNIT_MISSILE_HOUSE    = 1 << UNIT_MISSILE_HOUSE;    /* 0x__04____ */
    public static int FLAG_UNIT_MISSILE_ROCKET   = 1 << UNIT_MISSILE_ROCKET;   /* 0x__08____ */
    public static int FLAG_UNIT_MISSILE_TURRET   = 1 << UNIT_MISSILE_TURRET;   /* 0x__10____ */
    public static int FLAG_UNIT_MISSILE_DEVIATOR = 1 << UNIT_MISSILE_DEVIATOR; /* 0x__20____ */
    public static int FLAG_UNIT_MISSILE_TROOPER  = 1 << UNIT_MISSILE_TROOPER;  /* 0x__40____ */
    public static int FLAG_UNIT_BULLET           = 1 << UNIT_BULLET;           /* 0x__80____ */
    public static int FLAG_UNIT_SONIC_BLAST      = 1 << UNIT_SONIC_BLAST;      /* 0x01______ */
    public static int FLAG_UNIT_SANDWORM         = 1 << UNIT_SANDWORM;         /* 0x02______ */
    public static int FLAG_UNIT_FRIGATE          = 1 << UNIT_FRIGATE;          /* 0x04______ */

    public static int FLAG_UNIT_NONE             = 0;
}
