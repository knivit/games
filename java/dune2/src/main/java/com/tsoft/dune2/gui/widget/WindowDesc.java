package com.tsoft.dune2.gui.widget;

/**
 * Static information per WidgetClick type.
 */
public class WindowDesc {

    public class WidgetDesc {
        int stringID;                                    /*!< String of the Widget. */
        int offsetX;                                     /*!< Offset in X-position of the Widget (relative to Window). */
        int offsetY;                                     /*!< Offset in Y-position of the Widget (relative to Window). */
        int width;                                       /*!< Width of the Widget. */
        int height;                                      /*!< Height of the Widget. */
        int labelStringId;                               /*!< Label of the Widget. */
        int shortcut2;                                   /*!< The shortcut to trigger the Widget. */
    }

    public int index;                                            /*!< Index of the Window. */
    public int stringID;                                         /*!< String for the Window. */
    public boolean   addArrows;                                  /*!< If true, arrows are added to the Window. */
    public int  widgetCount;                                     /*!< Amount of widgets following. */
    public WidgetDesc[] widgets = new WidgetDesc[7];             /*!< The Widgets belonging to the Window. */
}
