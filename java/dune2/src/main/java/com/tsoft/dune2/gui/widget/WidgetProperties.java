package com.tsoft.dune2.gui.widget;

/** Widget properties. */
public class WidgetProperties {

    public int xBase;                                           /*!< Horizontal base coordinate divided by 8. */
    public int yBase;                                           /*!< Vertical base coordinate. */
    public int width;                                           /*!< Width of the widget divided by 8. */
    public int height;                                          /*!< Height of the widget. */
    public byte fgColourBlink;                                   /*!< Foreground colour for 'blink'. */
    public byte fgColourNormal;                                  /*!< Foreground colour for 'normal'. */
    public byte fgColourSelected;                                /*!< Foreground colour when 'selected' */

    public WidgetProperties(int xBase, int yBase, int width, int height, byte fgColourBlink, byte fgColourNormal, byte fgColourSelected) {
        this.xBase = xBase;
        this.yBase = yBase;
        this.width = width;
        this.height = height;
        this.fgColourBlink = fgColourBlink;
        this.fgColourNormal = fgColourNormal;
        this.fgColourSelected = fgColourSelected;
    }
}
