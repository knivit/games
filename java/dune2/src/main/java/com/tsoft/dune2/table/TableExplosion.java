package com.tsoft.dune2.table;

import com.tsoft.dune2.explosion.ExplosionCommandStruct;

import static com.tsoft.dune2.explosion.ExplosionCommand.*;

public class TableExplosion {

    /* EXPLOSION_IMPACT_SMALL */
    static ExplosionCommandStruct[] s_explosion00 = new ExplosionCommandStruct[] {
        new ExplosionCommandStruct(EXPLOSION_SET_SPRITE        ,  153),
        new ExplosionCommandStruct(EXPLOSION_SET_TIMEOUT       ,    3),
        new ExplosionCommandStruct(EXPLOSION_BLOOM_EXPLOSION   ,    0),
        new ExplosionCommandStruct(EXPLOSION_SET_SPRITE        ,  153),
        new ExplosionCommandStruct(EXPLOSION_SET_TIMEOUT       ,    3),
        new ExplosionCommandStruct(EXPLOSION_STOP              ,    0)
    };

    /* EXPLOSION_IMPACT_MEDIUM */
    static ExplosionCommandStruct[] s_explosion01 = new ExplosionCommandStruct[] {
        new ExplosionCommandStruct(EXPLOSION_SET_SPRITE        ,  154),
        new ExplosionCommandStruct(EXPLOSION_BLOOM_EXPLOSION   ,    0),
        new ExplosionCommandStruct(EXPLOSION_SET_TIMEOUT       ,    3),
        new ExplosionCommandStruct(EXPLOSION_SET_SPRITE        ,  153),
        new ExplosionCommandStruct(EXPLOSION_SET_TIMEOUT       ,    3),
        new ExplosionCommandStruct(EXPLOSION_SET_SPRITE        ,  154),
        new ExplosionCommandStruct(EXPLOSION_SET_TIMEOUT       ,    3),
        new ExplosionCommandStruct(EXPLOSION_STOP              ,    0)
    };

    /* EXPLOSION_IMPACT_LARGE */
    static ExplosionCommandStruct[] s_explosion02 = new ExplosionCommandStruct[] {
        new ExplosionCommandStruct(EXPLOSION_SET_SPRITE        ,  183),
        new ExplosionCommandStruct(EXPLOSION_PLAY_VOICE        ,   50),
        new ExplosionCommandStruct(EXPLOSION_BLOOM_EXPLOSION   ,    0),
        new ExplosionCommandStruct(EXPLOSION_TILE_DAMAGE       ,    0),
        new ExplosionCommandStruct(EXPLOSION_SET_TIMEOUT       ,   15),
        new ExplosionCommandStruct(EXPLOSION_SET_SPRITE        ,  184),
        new ExplosionCommandStruct(EXPLOSION_SET_TIMEOUT       ,   15),
        new ExplosionCommandStruct(EXPLOSION_STOP              ,    0)
    };

    /* EXPLOSION_IMPACT_EXPLODE */
    static ExplosionCommandStruct[] s_explosion03 = new ExplosionCommandStruct[] {
        new ExplosionCommandStruct(EXPLOSION_SET_SPRITE        ,  183),
        new ExplosionCommandStruct(EXPLOSION_PLAY_VOICE        ,   49),
        new ExplosionCommandStruct(EXPLOSION_BLOOM_EXPLOSION   ,    0),
        new ExplosionCommandStruct(EXPLOSION_TILE_DAMAGE       ,    0),
        new ExplosionCommandStruct(EXPLOSION_SET_TIMEOUT       ,    3),
        new ExplosionCommandStruct(EXPLOSION_SET_SPRITE        ,  184),
        new ExplosionCommandStruct(EXPLOSION_SET_TIMEOUT       ,    3),
        new ExplosionCommandStruct(EXPLOSION_STOP              ,    0)
    };

    /* EXPLOSION_SABOTEUR_DEATH */
    static ExplosionCommandStruct[] s_explosion04 = new ExplosionCommandStruct[] {
        new ExplosionCommandStruct(EXPLOSION_SET_SPRITE        ,  203),
        new ExplosionCommandStruct(EXPLOSION_PLAY_VOICE        ,   51),
        new ExplosionCommandStruct(EXPLOSION_BLOOM_EXPLOSION   ,    0),
        new ExplosionCommandStruct(EXPLOSION_TILE_DAMAGE       ,    0),
        new ExplosionCommandStruct(EXPLOSION_SET_TIMEOUT       ,    7),
        new ExplosionCommandStruct(EXPLOSION_SET_SPRITE        ,  204),
        new ExplosionCommandStruct(EXPLOSION_SET_TIMEOUT       ,    3),
        new ExplosionCommandStruct(EXPLOSION_SET_SPRITE        ,  205),
        new ExplosionCommandStruct(EXPLOSION_SET_TIMEOUT       ,    3),
        new ExplosionCommandStruct(EXPLOSION_SET_SPRITE        ,  206),
        new ExplosionCommandStruct(EXPLOSION_SET_TIMEOUT       ,    3),
        new ExplosionCommandStruct(EXPLOSION_SET_SPRITE        ,  207),
        new ExplosionCommandStruct(EXPLOSION_SET_TIMEOUT       ,    3),
        new ExplosionCommandStruct(EXPLOSION_STOP              ,    0)
    };

    /* EXPLOSION_SABOTEUR_INFILTRATE */
    static ExplosionCommandStruct[] s_explosion05 = new ExplosionCommandStruct[] {
        new ExplosionCommandStruct(EXPLOSION_SET_RANDOM_TIMEOUT,   60),
        new ExplosionCommandStruct(EXPLOSION_SET_SPRITE        ,  203),
        new ExplosionCommandStruct(EXPLOSION_PLAY_VOICE        ,   41),
        new ExplosionCommandStruct(EXPLOSION_BLOOM_EXPLOSION   ,    0),
        new ExplosionCommandStruct(EXPLOSION_TILE_DAMAGE       ,    0),
        new ExplosionCommandStruct(EXPLOSION_SET_TIMEOUT       ,    7),
        new ExplosionCommandStruct(EXPLOSION_SET_SPRITE        ,  204),
        new ExplosionCommandStruct(EXPLOSION_SET_TIMEOUT       ,    3),
        new ExplosionCommandStruct(EXPLOSION_SET_SPRITE        ,  205),
        new ExplosionCommandStruct(EXPLOSION_SET_TIMEOUT       ,    3),
        new ExplosionCommandStruct(EXPLOSION_SET_SPRITE        ,  206),
        new ExplosionCommandStruct(EXPLOSION_SET_TIMEOUT       ,    3),
        new ExplosionCommandStruct(EXPLOSION_SET_SPRITE        ,  207),
        new ExplosionCommandStruct(EXPLOSION_SET_TIMEOUT       ,    3),
        new ExplosionCommandStruct(EXPLOSION_STOP              ,    0)
    };

    /* EXPLOSION_TANK_EXPLODE */
    static ExplosionCommandStruct[] s_explosion06 = new ExplosionCommandStruct[] {
        new ExplosionCommandStruct(EXPLOSION_SET_SPRITE        ,  198),
        new ExplosionCommandStruct(EXPLOSION_PLAY_VOICE        ,   51),
        new ExplosionCommandStruct(EXPLOSION_BLOOM_EXPLOSION   ,    0),
        new ExplosionCommandStruct(EXPLOSION_TILE_DAMAGE       ,    0),
        new ExplosionCommandStruct(EXPLOSION_SET_TIMEOUT       ,    7),
        new ExplosionCommandStruct(EXPLOSION_SET_SPRITE        ,  199),
        new ExplosionCommandStruct(EXPLOSION_SET_TIMEOUT       ,    3),
        new ExplosionCommandStruct(EXPLOSION_SET_SPRITE        ,  200),
        new ExplosionCommandStruct(EXPLOSION_SET_TIMEOUT       ,    3),
        new ExplosionCommandStruct(EXPLOSION_SET_SPRITE        ,  201),
        new ExplosionCommandStruct(EXPLOSION_SET_TIMEOUT       ,    3),
        new ExplosionCommandStruct(EXPLOSION_SET_SPRITE        ,  202),
        new ExplosionCommandStruct(EXPLOSION_SET_TIMEOUT       ,    3),
        new ExplosionCommandStruct(EXPLOSION_STOP              ,    0)
    };

    /* EXPLOSION_DEVIATOR_GAS */
    static ExplosionCommandStruct[] s_explosion07 = new ExplosionCommandStruct[] {
        new ExplosionCommandStruct(EXPLOSION_SET_SPRITE        ,  208),
        new ExplosionCommandStruct(EXPLOSION_PLAY_VOICE        ,   39),
        new ExplosionCommandStruct(EXPLOSION_SET_TIMEOUT       ,   15),
        new ExplosionCommandStruct(EXPLOSION_SET_SPRITE        ,  209),
        new ExplosionCommandStruct(EXPLOSION_SET_TIMEOUT       ,   15),
        new ExplosionCommandStruct(EXPLOSION_SET_SPRITE        ,  210),
        new ExplosionCommandStruct(EXPLOSION_SET_TIMEOUT       ,   15),
        new ExplosionCommandStruct(EXPLOSION_SET_SPRITE        ,  211),
        new ExplosionCommandStruct(EXPLOSION_SET_TIMEOUT       ,   15),
        new ExplosionCommandStruct(EXPLOSION_SET_SPRITE        ,  212),
        new ExplosionCommandStruct(EXPLOSION_SET_TIMEOUT       ,   15),
        new ExplosionCommandStruct(EXPLOSION_STOP              ,    0)
    };

    /* EXPLOSION_SAND_BURST */
    static ExplosionCommandStruct[] s_explosion08 = new ExplosionCommandStruct[] {
        new ExplosionCommandStruct(EXPLOSION_SET_SPRITE        ,  156),
        new ExplosionCommandStruct(EXPLOSION_PLAY_VOICE        ,   40),
        new ExplosionCommandStruct(EXPLOSION_BLOOM_EXPLOSION   ,    0),
        new ExplosionCommandStruct(EXPLOSION_SET_TIMEOUT       ,    7),
        new ExplosionCommandStruct(EXPLOSION_SET_SPRITE        ,  157),
        new ExplosionCommandStruct(EXPLOSION_SET_TIMEOUT       ,    3),
        new ExplosionCommandStruct(EXPLOSION_SET_SPRITE        ,  158),
        new ExplosionCommandStruct(EXPLOSION_SET_TIMEOUT       ,    3),
        new ExplosionCommandStruct(EXPLOSION_SET_SPRITE        ,  157),
        new ExplosionCommandStruct(EXPLOSION_SET_TIMEOUT       ,    3),
        new ExplosionCommandStruct(EXPLOSION_TILE_DAMAGE       ,    0),
        new ExplosionCommandStruct(EXPLOSION_STOP              ,    0)
    };

    /* EXPLOSION_TANK_FLAMES */
    static ExplosionCommandStruct[] s_explosion09 = new ExplosionCommandStruct[] {
        new ExplosionCommandStruct(EXPLOSION_SET_SPRITE        ,  183),
        new ExplosionCommandStruct(EXPLOSION_PLAY_VOICE        ,   41),
        new ExplosionCommandStruct(EXPLOSION_BLOOM_EXPLOSION   ,    0),
        new ExplosionCommandStruct(EXPLOSION_TILE_DAMAGE       ,    0),
        new ExplosionCommandStruct(EXPLOSION_SET_TIMEOUT       ,    3),
        new ExplosionCommandStruct(EXPLOSION_SET_SPRITE        ,  203),
        new ExplosionCommandStruct(EXPLOSION_SET_TIMEOUT       ,    3),
        new ExplosionCommandStruct(EXPLOSION_MOVE_Y_POSITION   ,  -80),
        new ExplosionCommandStruct(EXPLOSION_SET_SPRITE        ,  168),
        new ExplosionCommandStruct(EXPLOSION_SET_TIMEOUT       ,   15),
        new ExplosionCommandStruct(EXPLOSION_SET_SPRITE        ,  169),
        new ExplosionCommandStruct(EXPLOSION_SET_TIMEOUT       ,   15),
        new ExplosionCommandStruct(EXPLOSION_SET_SPRITE        ,  170),
        new ExplosionCommandStruct(EXPLOSION_SET_TIMEOUT       ,   15),
        new ExplosionCommandStruct(EXPLOSION_SET_SPRITE        ,  168),
        new ExplosionCommandStruct(EXPLOSION_SET_TIMEOUT       ,   15),
        new ExplosionCommandStruct(EXPLOSION_SET_SPRITE        ,  169),
        new ExplosionCommandStruct(EXPLOSION_SET_TIMEOUT       ,   15),
        new ExplosionCommandStruct(EXPLOSION_SET_SPRITE        ,  170),
        new ExplosionCommandStruct(EXPLOSION_SET_TIMEOUT       ,   15),
        new ExplosionCommandStruct(EXPLOSION_SET_SPRITE        ,  168),
        new ExplosionCommandStruct(EXPLOSION_SET_TIMEOUT       ,   15),
        new ExplosionCommandStruct(EXPLOSION_SET_SPRITE        ,  169),
        new ExplosionCommandStruct(EXPLOSION_SET_TIMEOUT       ,   15),
        new ExplosionCommandStruct(EXPLOSION_SET_SPRITE        ,  170),
        new ExplosionCommandStruct(EXPLOSION_SET_TIMEOUT       ,   15),
        new ExplosionCommandStruct(EXPLOSION_SET_SPRITE        ,  168),
        new ExplosionCommandStruct(EXPLOSION_SET_TIMEOUT       ,   15),
        new ExplosionCommandStruct(EXPLOSION_SET_SPRITE        ,  169),
        new ExplosionCommandStruct(EXPLOSION_SET_TIMEOUT       ,   15),
        new ExplosionCommandStruct(EXPLOSION_SET_SPRITE        ,  170),
        new ExplosionCommandStruct(EXPLOSION_SET_TIMEOUT       ,   15),
        new ExplosionCommandStruct(EXPLOSION_SET_SPRITE        ,  168),
        new ExplosionCommandStruct(EXPLOSION_SET_TIMEOUT       ,   15),
        new ExplosionCommandStruct(EXPLOSION_SET_SPRITE        ,  169),
        new ExplosionCommandStruct(EXPLOSION_SET_TIMEOUT       ,   15),
        new ExplosionCommandStruct(EXPLOSION_SET_SPRITE        ,  170),
        new ExplosionCommandStruct(EXPLOSION_SET_TIMEOUT       ,   15),
        new ExplosionCommandStruct(EXPLOSION_STOP              ,    0)
    };

    /* EXPLOSION_WHEELED_VEHICLE */
    static ExplosionCommandStruct[] s_explosion10 = new ExplosionCommandStruct[] {
        new ExplosionCommandStruct(EXPLOSION_SET_SPRITE        ,  151),
        new ExplosionCommandStruct(EXPLOSION_PLAY_VOICE        ,   49),
        new ExplosionCommandStruct(EXPLOSION_BLOOM_EXPLOSION   ,    0),
        new ExplosionCommandStruct(EXPLOSION_TILE_DAMAGE       ,    0),
        new ExplosionCommandStruct(EXPLOSION_SET_TIMEOUT       ,    7),
        new ExplosionCommandStruct(EXPLOSION_SET_SPRITE        ,  152),
        new ExplosionCommandStruct(EXPLOSION_SET_TIMEOUT       ,    7),
        new ExplosionCommandStruct(EXPLOSION_STOP              ,    0)
    };

    /* EXPLOSION_DEATH_HAND */
    static ExplosionCommandStruct[] s_explosion11 = new ExplosionCommandStruct[] {
        new ExplosionCommandStruct(EXPLOSION_SET_RANDOM_TIMEOUT,   60),
        new ExplosionCommandStruct(EXPLOSION_SET_SPRITE        ,  188),
        new ExplosionCommandStruct(EXPLOSION_PLAY_VOICE        ,   51),
        new ExplosionCommandStruct(EXPLOSION_BLOOM_EXPLOSION   ,    0),
        new ExplosionCommandStruct(EXPLOSION_TILE_DAMAGE       ,    0),
        new ExplosionCommandStruct(EXPLOSION_SET_TIMEOUT       ,    7),
        new ExplosionCommandStruct(EXPLOSION_SET_SPRITE        ,  189),
        new ExplosionCommandStruct(EXPLOSION_SET_TIMEOUT       ,    3),
        new ExplosionCommandStruct(EXPLOSION_SET_SPRITE        ,  190),
        new ExplosionCommandStruct(EXPLOSION_SET_TIMEOUT       ,    3),
        new ExplosionCommandStruct(EXPLOSION_SET_SPRITE        ,  191),
        new ExplosionCommandStruct(EXPLOSION_SET_TIMEOUT       ,    3),
        new ExplosionCommandStruct(EXPLOSION_SET_SPRITE        ,  192),
        new ExplosionCommandStruct(EXPLOSION_SET_TIMEOUT       ,    3),
        new ExplosionCommandStruct(EXPLOSION_STOP              ,    0)
    };

    /* EXPLOSION_UNUSED_12 */
    static ExplosionCommandStruct[] s_explosion12 = new ExplosionCommandStruct[] {
        new ExplosionCommandStruct(EXPLOSION_SET_SPRITE        ,  213),
        new ExplosionCommandStruct(EXPLOSION_SET_TIMEOUT       ,   15),
        new ExplosionCommandStruct(EXPLOSION_SET_SPRITE        ,  214),
        new ExplosionCommandStruct(EXPLOSION_SET_TIMEOUT       ,   15),
        new ExplosionCommandStruct(EXPLOSION_SET_SPRITE        ,  215),
        new ExplosionCommandStruct(EXPLOSION_SET_TIMEOUT       ,   15),
        new ExplosionCommandStruct(EXPLOSION_SET_SPRITE        ,  216),
        new ExplosionCommandStruct(EXPLOSION_SET_TIMEOUT       ,   15),
        new ExplosionCommandStruct(EXPLOSION_SET_SPRITE        ,  217),
        new ExplosionCommandStruct(EXPLOSION_SET_TIMEOUT       ,   30),
        new ExplosionCommandStruct(EXPLOSION_STOP              ,    0)
    };

    /* EXPLOSION_SANDWORM_SWALLOW */
    static ExplosionCommandStruct[] s_explosion13 = new ExplosionCommandStruct[] {
        new ExplosionCommandStruct(EXPLOSION_SET_SPRITE        ,  218),
        new ExplosionCommandStruct(EXPLOSION_SET_TIMEOUT       ,   15),
        new ExplosionCommandStruct(EXPLOSION_SET_SPRITE        ,  219),
        new ExplosionCommandStruct(EXPLOSION_SET_TIMEOUT       ,   15),
        new ExplosionCommandStruct(EXPLOSION_SET_SPRITE        ,  220),
        new ExplosionCommandStruct(EXPLOSION_SET_TIMEOUT       ,   15),
        new ExplosionCommandStruct(EXPLOSION_SET_SPRITE        ,  221),
        new ExplosionCommandStruct(EXPLOSION_SET_TIMEOUT       ,   15),
        new ExplosionCommandStruct(EXPLOSION_SET_SPRITE        ,  222),
        new ExplosionCommandStruct(EXPLOSION_SET_TIMEOUT       ,   30),
        new ExplosionCommandStruct(EXPLOSION_STOP              ,    0)
    };

    /* EXPLOSION_STRUCTURE */
    static ExplosionCommandStruct[] s_explosion14 = new ExplosionCommandStruct[] {
        new ExplosionCommandStruct(EXPLOSION_SET_RANDOM_TIMEOUT,   60),
        new ExplosionCommandStruct(EXPLOSION_SET_SPRITE        ,  188),
        new ExplosionCommandStruct(EXPLOSION_PLAY_VOICE        ,   51),
        new ExplosionCommandStruct(EXPLOSION_SET_TIMEOUT       ,    7),
        new ExplosionCommandStruct(EXPLOSION_SET_SPRITE        ,  189),
        new ExplosionCommandStruct(EXPLOSION_BLOOM_EXPLOSION   ,    0),
        new ExplosionCommandStruct(EXPLOSION_SCREEN_SHAKE      ,    0),
        new ExplosionCommandStruct(EXPLOSION_SET_TIMEOUT       ,    3),
        new ExplosionCommandStruct(EXPLOSION_SET_SPRITE        ,  190),
        new ExplosionCommandStruct(EXPLOSION_SET_TIMEOUT       ,    3),
        new ExplosionCommandStruct(EXPLOSION_SET_SPRITE        ,  191),
        new ExplosionCommandStruct(EXPLOSION_SET_TIMEOUT       ,    3),
        new ExplosionCommandStruct(EXPLOSION_SET_SPRITE        ,  192),
        new ExplosionCommandStruct(EXPLOSION_SET_TIMEOUT       ,    3),
        new ExplosionCommandStruct(EXPLOSION_STOP              ,    0)
    };

    /* EXPLOSION_SMOKE_PLUME */
    static ExplosionCommandStruct[] s_explosion15 = new ExplosionCommandStruct[] {
        new ExplosionCommandStruct(EXPLOSION_SET_SPRITE        ,  183),
        new ExplosionCommandStruct(EXPLOSION_PLAY_VOICE        ,   49),
        new ExplosionCommandStruct(EXPLOSION_MOVE_Y_POSITION   ,  -80),
        new ExplosionCommandStruct(EXPLOSION_SET_TIMEOUT       ,    3),
        new ExplosionCommandStruct(EXPLOSION_SET_SPRITE        ,  184),
        new ExplosionCommandStruct(EXPLOSION_SET_TIMEOUT       ,    3),
        new ExplosionCommandStruct(EXPLOSION_SET_SPRITE        ,  180),
        new ExplosionCommandStruct(EXPLOSION_SET_TIMEOUT       ,   15),
        new ExplosionCommandStruct(EXPLOSION_SET_SPRITE        ,  181),
        new ExplosionCommandStruct(EXPLOSION_SET_TIMEOUT       ,   15),
        new ExplosionCommandStruct(EXPLOSION_SET_SPRITE        ,  182),
        new ExplosionCommandStruct(EXPLOSION_SET_TIMEOUT       ,   15),
        new ExplosionCommandStruct(EXPLOSION_SET_SPRITE        ,  181),
        new ExplosionCommandStruct(EXPLOSION_SET_TIMEOUT       ,   15),
        new ExplosionCommandStruct(EXPLOSION_SET_SPRITE        ,  180),
        new ExplosionCommandStruct(EXPLOSION_SET_TIMEOUT       ,   15),
        new ExplosionCommandStruct(EXPLOSION_SET_SPRITE        ,  181),
        new ExplosionCommandStruct(EXPLOSION_SET_TIMEOUT       ,   15),
        new ExplosionCommandStruct(EXPLOSION_SET_SPRITE        ,  182),
        new ExplosionCommandStruct(EXPLOSION_SET_TIMEOUT       ,   15),
        new ExplosionCommandStruct(EXPLOSION_SET_SPRITE        ,  181),
        new ExplosionCommandStruct(EXPLOSION_SET_TIMEOUT       ,   15),
        new ExplosionCommandStruct(EXPLOSION_SET_SPRITE        ,  180),
        new ExplosionCommandStruct(EXPLOSION_SET_TIMEOUT       ,   15),
        new ExplosionCommandStruct(EXPLOSION_SET_SPRITE        ,  181),
        new ExplosionCommandStruct(EXPLOSION_SET_TIMEOUT       ,   15),
        new ExplosionCommandStruct(EXPLOSION_SET_SPRITE        ,  182),
        new ExplosionCommandStruct(EXPLOSION_SET_TIMEOUT       ,   15),
        new ExplosionCommandStruct(EXPLOSION_SET_SPRITE        ,  181),
        new ExplosionCommandStruct(EXPLOSION_SET_TIMEOUT       ,   15),
        new ExplosionCommandStruct(EXPLOSION_SET_SPRITE        ,  180),
        new ExplosionCommandStruct(EXPLOSION_SET_TIMEOUT       ,   15),
        new ExplosionCommandStruct(EXPLOSION_SET_SPRITE        ,  181),
        new ExplosionCommandStruct(EXPLOSION_SET_TIMEOUT       ,   15),
        new ExplosionCommandStruct(EXPLOSION_SET_SPRITE        ,  182),
        new ExplosionCommandStruct(EXPLOSION_SET_TIMEOUT       ,   15),
        new ExplosionCommandStruct(EXPLOSION_SET_SPRITE        ,  181),
        new ExplosionCommandStruct(EXPLOSION_SET_TIMEOUT       ,   15),
        new ExplosionCommandStruct(EXPLOSION_SET_SPRITE        ,  180),
        new ExplosionCommandStruct(EXPLOSION_SET_TIMEOUT       ,   15),
        new ExplosionCommandStruct(EXPLOSION_SET_SPRITE        ,  181),
        new ExplosionCommandStruct(EXPLOSION_SET_TIMEOUT       ,   15),
        new ExplosionCommandStruct(EXPLOSION_SET_SPRITE        ,  182),
        new ExplosionCommandStruct(EXPLOSION_SET_TIMEOUT       ,   15),
        new ExplosionCommandStruct(EXPLOSION_SET_SPRITE        ,  181),
        new ExplosionCommandStruct(EXPLOSION_SET_TIMEOUT       ,   15),
        new ExplosionCommandStruct(EXPLOSION_SET_SPRITE        ,  180),
        new ExplosionCommandStruct(EXPLOSION_SET_TIMEOUT       ,   15),
        new ExplosionCommandStruct(EXPLOSION_SET_SPRITE        ,  181),
        new ExplosionCommandStruct(EXPLOSION_SET_TIMEOUT       ,   15),
        new ExplosionCommandStruct(EXPLOSION_SET_SPRITE        ,  182),
        new ExplosionCommandStruct(EXPLOSION_SET_TIMEOUT       ,   15),
        new ExplosionCommandStruct(EXPLOSION_SET_SPRITE        ,  181),
        new ExplosionCommandStruct(EXPLOSION_SET_TIMEOUT       ,   15),
        new ExplosionCommandStruct(EXPLOSION_STOP              ,    0)
    };

    /* EXPLOSION_ORNITHOPTER_CRASH */
    static ExplosionCommandStruct[] s_explosion16 = new ExplosionCommandStruct[] {
        new ExplosionCommandStruct(EXPLOSION_SET_SPRITE        ,  203),
        new ExplosionCommandStruct(EXPLOSION_PLAY_VOICE        ,   49),
        new ExplosionCommandStruct(EXPLOSION_BLOOM_EXPLOSION   ,    0),
        new ExplosionCommandStruct(EXPLOSION_SET_ANIMATION     ,    0),
        new ExplosionCommandStruct(EXPLOSION_SET_TIMEOUT       ,    3),
        new ExplosionCommandStruct(EXPLOSION_SET_SPRITE        ,  204),
        new ExplosionCommandStruct(EXPLOSION_SET_SPRITE        ,  207),
        new ExplosionCommandStruct(EXPLOSION_SET_TIMEOUT       ,    3),
        new ExplosionCommandStruct(EXPLOSION_STOP              ,    0)
    };

    /* EXPLOSION_CARRYALL_CRASH */
    static ExplosionCommandStruct[] s_explosion17 = new ExplosionCommandStruct[] {
        new ExplosionCommandStruct(EXPLOSION_SET_SPRITE        ,  203),
        new ExplosionCommandStruct(EXPLOSION_PLAY_VOICE        ,   49),
        new ExplosionCommandStruct(EXPLOSION_BLOOM_EXPLOSION   ,    0),
        new ExplosionCommandStruct(EXPLOSION_SET_ANIMATION     ,    4),
        new ExplosionCommandStruct(EXPLOSION_SET_TIMEOUT       ,    3),
        new ExplosionCommandStruct(EXPLOSION_SET_SPRITE        ,  204),
        new ExplosionCommandStruct(EXPLOSION_SET_SPRITE        ,  207),
        new ExplosionCommandStruct(EXPLOSION_SET_TIMEOUT       ,    3),
        new ExplosionCommandStruct(EXPLOSION_STOP              ,    0)
    };

    /* EXPLOSION_MINI_ROCKET */
    static ExplosionCommandStruct[] s_explosion18 = new ExplosionCommandStruct[] {
        new ExplosionCommandStruct(EXPLOSION_SET_SPRITE        ,  183),
        new ExplosionCommandStruct(EXPLOSION_PLAY_VOICE        ,   54),
        new ExplosionCommandStruct(EXPLOSION_BLOOM_EXPLOSION   ,    0),
        new ExplosionCommandStruct(EXPLOSION_SET_TIMEOUT       ,    3),
        new ExplosionCommandStruct(EXPLOSION_SET_SPRITE        ,  184),
        new ExplosionCommandStruct(EXPLOSION_SET_TIMEOUT       ,    3),
        new ExplosionCommandStruct(EXPLOSION_STOP              ,    0)
    };

    /* EXPLOSION_SPICE_BLOOM_TREMOR */
    static ExplosionCommandStruct[] s_explosion19 = new ExplosionCommandStruct[] {
        new ExplosionCommandStruct(EXPLOSION_SET_SPRITE        ,  156),
        new ExplosionCommandStruct(EXPLOSION_PLAY_VOICE        ,   40),
        new ExplosionCommandStruct(EXPLOSION_SCREEN_SHAKE      ,    0),
        new ExplosionCommandStruct(EXPLOSION_SET_TIMEOUT       ,    7),
        new ExplosionCommandStruct(EXPLOSION_SCREEN_SHAKE      ,    0),
        new ExplosionCommandStruct(EXPLOSION_SET_SPRITE        ,  157),
        new ExplosionCommandStruct(EXPLOSION_SET_TIMEOUT       ,    3),
        new ExplosionCommandStruct(EXPLOSION_SCREEN_SHAKE      ,    0),
        new ExplosionCommandStruct(EXPLOSION_SET_SPRITE        ,  158),
        new ExplosionCommandStruct(EXPLOSION_SET_TIMEOUT       ,    3),
        new ExplosionCommandStruct(EXPLOSION_SCREEN_SHAKE      ,    0),
        new ExplosionCommandStruct(EXPLOSION_SET_SPRITE        ,  157),
        new ExplosionCommandStruct(EXPLOSION_SET_TIMEOUT       ,    3),
        new ExplosionCommandStruct(EXPLOSION_SCREEN_SHAKE      ,    0),
        new ExplosionCommandStruct(EXPLOSION_TILE_DAMAGE       ,    0),
        new ExplosionCommandStruct(EXPLOSION_STOP              ,    0)
    };

    public static ExplosionCommandStruct[][] g_table_explosion = new ExplosionCommandStruct[][] {
        s_explosion00,
        s_explosion01,
        s_explosion02,
        s_explosion03,
        s_explosion04,
        s_explosion05,
        s_explosion06,
        s_explosion07,
        s_explosion08,
        s_explosion09,
        s_explosion10,
        s_explosion11,
        s_explosion12,
        s_explosion13,
        s_explosion14,
        s_explosion15,
        s_explosion16,
        s_explosion17,
        s_explosion18,
        s_explosion19
    };
}
