package com.tsoft.dune2.map;

/** Definition of the map size of a map scale. */
public class MapInfo {

    public int minX;                                            /* Minimal X position of the map. */
    public int minY;                                            /* Minimal Y position of the map. */
    public int sizeX;                                           /* Width of the map. */
    public int sizeY;                                           /* Height of the map. */

    public MapInfo(int minX, int minY, int sizeX, int sizeY) {
        this.minX = minX;
        this.minY = minY;
        this.sizeX = sizeX;
        this.sizeY = sizeY;
    }
}
