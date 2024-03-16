package com.tsoft.dune2.gui.widget;

import java.util.function.Function;

/**
 * Static information per WidgetClick type.
 */
public class WidgetInfo {

    public int index;                           /*!< ?? */
    public Function<Widget, Boolean> clickProc; /*!< Function to execute when widget is pressed. */
    public int shortcut;                        /*!< ?? */
    public int flags;                           /*!< ?? */
    public int spriteID;                        /*!< ?? */
    public int offsetX;                         /*!< ?? */
    public int offsetY;                         /*!< ?? */
    public int width;                           /*!< only used if spriteID < 0 */
    public int height;                          /*!< only used if spriteID < 0 */
    public int stringID;                        /*!< ?? */

    public WidgetInfo(int index, Function<Widget, Boolean> clickProc, int shortcut, int flags, int spriteID, int offsetX, int offsetY, int width, int height, int stringID) {
        this.index = index;
        this.clickProc = clickProc;
        this.shortcut = shortcut;
        this.flags = flags;
        this.spriteID = spriteID;
        this.offsetX = offsetX;
        this.offsetY = offsetY;
        this.width = width;
        this.height = height;
        this.stringID = stringID;
    }
}
