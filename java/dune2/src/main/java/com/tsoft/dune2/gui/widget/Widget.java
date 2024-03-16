package com.tsoft.dune2.gui.widget;

import java.util.function.Function;

/**
 * A Widget as stored in the memory.
 */
public class Widget {

    public Widget next;                                           /*!< Next widget in the list. */
    public int index;                                             /*!< Index of the widget. */
    public int shortcut;                                          /*!< What key triggers this widget. */
    public int shortcut2;                                         /*!< What key (also) triggers this widget. */
    public int drawModeNormal;                                    /*!< Draw mode when normal. */
    public int drawModeSelected;                                  /*!< Draw mode when selected. */
    public int drawModeDown;                                      /*!< Draw mode when down. */
    public Flags flags = new Flags();                             /*!< General flags of the Widget. */
    public WidgetDrawParameter drawParameterNormal;               /*!< Draw parameter when normal. */
    public WidgetDrawParameter drawParameterSelected;             /*!< Draw parameter when selected. */
    public WidgetDrawParameter drawParameterDown;                 /*!< Draw parameter when down. */
    public int parentID;                                          /*!< Parent window we are nested in. */
    public int offsetX;                                           /*!< X position from parent we are at, in pixels. */
    public int offsetY;                                           /*!< Y position from parent we are at, in pixels. */
    public int width;                                             /*!< Width of widget in pixels. */
    public int height;                                            /*!< Height of widget in pixels. */
    public int fgColourNormal;                                    /*!< Foreground colour for draw proc when normal. */
    public int bgColourNormal;                                    /*!< Background colour for draw proc when normal. */
    public int fgColourSelected;                                  /*!< Foreground colour for draw proc when selected. */
    public int bgColourSelected;                                  /*!< Background colour for draw proc when selected. */
    public int fgColourDown;                                      /*!< Foreground colour for draw proc when down. */
    public int bgColourDown;                                      /*!< Background colour for draw proc when down. */
    public State state = new State();                             /*!< State of the Widget. */
    public Function<Widget, Boolean> clickProc;                   /*!< Function to execute when widget is pressed. */
    public Object data;                                           /*!< If non-NULL, it points to WidgetScrollbar or HallOfFameData belonging to this widget. */
    public int stringID;                                          /*!< Strings to print on the widget. Index above 0xFFF2 are special. */
}
