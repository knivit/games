package com.tsoft.dune2.explosion;

import com.tsoft.dune2.tile.Tile32;

/**
 * The layout of a Explosion.
 */
public class Explosion {

    public long timeOut;                           /*!< Time out for the next command. */
    public int houseID;                            /*!< A houseID. */
    public boolean isDirty;                        /*!< Does the Explosion require a redraw next round. */
    public int  current;                           /*!< Index in #commands pointing to the next command. */
    public int spriteID;                           /*!< SpriteID. */
    public ExplosionCommandStruct[] commands;      /*!< Commands being executed. */
    public Tile32 position = new Tile32();         /*!< Position where this explosion acts. */
}
