package com.tsoft.dune2.map;

/** Types of available landscapes. */
public class LandscapeType {

    public static final int LST_NORMAL_SAND       =  0;     /* Flat sand. */
    public static final int LST_PARTIAL_ROCK      =  1;     /* Edge of a rocky area (mostly sand). */
    public static final int LST_ENTIRELY_DUNE     =  2;     /* Entirely sand dunes. */
    public static final int LST_PARTIAL_DUNE      =  3;     /* Partial sand dunes. */
    public static final int LST_ENTIRELY_ROCK     =  4;     /* Center part of rocky area. */
    public static final int LST_MOSTLY_ROCK       =  5;     /* Edge of a rocky area (mostly rocky). */
    public static final int LST_ENTIRELY_MOUNTAIN =  6;     /* Center part of the mountain. */
    public static final int LST_PARTIAL_MOUNTAIN  =  7;     /* Edge of a mountain. */
    public static final int LST_SPICE             =  8;     /* Sand with spice. */
    public static final int LST_THICK_SPICE       =  9;     /* Sand with thick spice. */
    public static final int LST_CONCRETE_SLAB     = 10;     /* Concrete slab. */
    public static final int LST_WALL              = 11;     /* Wall. */
    public static final int LST_STRUCTURE         = 12;     /* Structure. */
    public static final int LST_DESTROYED_WALL    = 13;     /* Destroyed wall. */
    public static final int LST_BLOOM_FIELD       = 14;     /* Bloom field. */

    public static final int LST_MAX               = 15;
}
