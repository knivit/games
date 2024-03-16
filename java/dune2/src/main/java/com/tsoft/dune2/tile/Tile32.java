package com.tsoft.dune2.tile;

/**
 * bits 0 to 7 are the offset in the tile.
 * bits 8 to 13 are the position on the map.
 * bits 14 and 15 are never used (or should never be used).
 */
public class Tile32 {

    public int x;
    public int y;

    public Tile32() { }

    public Tile32(int x, int y) {
        this.x = x;
        this.y = y;
    }
}
