package com.tsoft.dune2.explosion;

/**
 * Types of Explosions available in the game.
 */
public class ExplosionType {

    public static final int EXPLOSION_MAX = 32;           /* The maximum amount of active explosions we can have. */

    public static final int EXPLOSION_IMPACT_SMALL        = 0;
    public static final int EXPLOSION_IMPACT_MEDIUM       = 1;
    public static final int EXPLOSION_IMPACT_LARGE        = 2;
    public static final int EXPLOSION_IMPACT_EXPLODE      = 3;
    public static final int EXPLOSION_SABOTEUR_DEATH      = 4;
    public static final int EXPLOSION_SABOTEUR_INFILTRATE = 5;
    public static final int EXPLOSION_TANK_EXPLODE        = 6;
    public static final int EXPLOSION_DEVIATOR_GAS        = 7;
    public static final int EXPLOSION_SAND_BURST          = 8;
    public static final int EXPLOSION_TANK_FLAMES         = 9;
    public static final int EXPLOSION_WHEELED_VEHICLE     = 10;
    public static final int EXPLOSION_DEATH_HAND          = 11;
    public static final int EXPLOSION_UNUSED_12           = 12;
    public static final int EXPLOSION_SANDWORM_SWALLOW    = 13;
    public static final int EXPLOSION_STRUCTURE           = 14;
    public static final int EXPLOSION_SMOKE_PLUME         = 15;
    public static final int EXPLOSION_ORNITHOPTER_CRASH   = 16;
    public static final int EXPLOSION_CARRYALL_CRASH      = 17;
    public static final int EXPLOSION_MINI_ROCKET         = 18;
    public static final int EXPLOSION_SPICE_BLOOM_TREMOR  = 19;

    public static final int EXPLOSIONTYPE_MAX             = 20;
    public static final int EXPLOSION_INVALID             = 0xFFFF;
}
