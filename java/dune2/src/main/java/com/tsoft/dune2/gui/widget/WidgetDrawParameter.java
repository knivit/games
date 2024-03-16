package com.tsoft.dune2.gui.widget;

import java.util.function.Consumer;

/**
 * The parameter for a given DrawMode.
 */
public class WidgetDrawParameter {

    public int spriteID;                      /*!< Parameter for DRAW_MODE_UNKNOWN3. */
    public Object sprite;                     /*!< Parameter for DRAW_MODE_SPRITE. */
    public String text;                       /*!< Parameter for DRAW_MODE_TEXT. */
    public Consumer<Widget> proc;            /*!< Parameter for DRAW_MODE_CUSTOM_PROC. */
}
