package com.tsoft.dune2.explosion;

/**
 * The valid types for command in Explosion.
 */
public class ExplosionCommand {

    public static final int EXPLOSION_STOP = 1;                   /* Stop the Explosion. */
    public static final int EXPLOSION_SET_SPRITE = 2;             /* Set the sprite for the Explosion. */
    public static final int EXPLOSION_SET_TIMEOUT = 3;            /* Set the timeout for the Explosion. */
    public static final int EXPLOSION_SET_RANDOM_TIMEOUT = 4;     /* Set a random timeout for the Explosion. */
    public static final int EXPLOSION_MOVE_Y_POSITION = 5;        /* Move the Y-position of the Explosion. */
    public static final int EXPLOSION_TILE_DAMAGE = 6;            /* Handle damage to a tile in a Explosion. */
    public static final int EXPLOSION_PLAY_VOICE = 7;             /* Play a voice. */
    public static final int EXPLOSION_SCREEN_SHAKE = 8;           /* Shake the screen around. */
    public static final int EXPLOSION_SET_ANIMATION = 9;          /* Set the animation for the Explosion. */
    public static final int EXPLOSION_BLOOM_EXPLOSION = 10;       /* Make a bloom explode. */
}
