package com.tsoft.dune2.animation;

import com.tsoft.dune2.tile.Tile32;

public class Animation {

    public long tickNext;                        /*!< Which tick this Animation should be called again. */
    public int tileLayout;                       /*!< Tile layout of the Animation. */
    public int houseID;                          /*!< House of the item being animated. */
    public int current;                          /*!< At which command we currently are in the Animation. */
    public int iconGroup;                        /*!< Which iconGroup the sprites of the Animation belongs. */
    public AnimationCommandStruct[] commands;    /*!< List of commands for this Animation. */
    public Tile32 tile = new Tile32();           /*!< Top-left tile of Animation. */
}
