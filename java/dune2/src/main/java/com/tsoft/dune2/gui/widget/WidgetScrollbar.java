package com.tsoft.dune2.gui.widget;

import java.util.function.Consumer;

/**
 * Scrollbar information as stored in the memory.
 */
public class WidgetScrollbar {

    public Widget parent;                                       /*!< Parent widget we belong to. */
    public int size;                                            /*!< Size (in pixels) of the scrollbar. */
    public int position;                                        /*!< Current position of the scrollbar. */
    public int scrollMax;                                       /*!< Maximum position of the scrollbar cursor. */
    public int scrollPageSize;                                  /*!< Amount of elements to scroll per page. */
    public int scrollPosition;                                  /*!< Current position of the scrollbar cursor. */
    public int  pressed;                                        /*!< If non-zero, the scrollbar is currently pressed. */
    public int  dirty;                                          /*!< If non-zero, the scrollbar is dirty (requires repaint). */
    public int pressedPosition;                                 /*!< Position where we clicked on the scrollbar when pressed. */
    public Consumer<Widget> drawProc;                          /*!< Draw proc (called on every draw). Can be null. */
}
