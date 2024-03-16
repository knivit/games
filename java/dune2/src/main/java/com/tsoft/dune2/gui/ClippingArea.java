package com.tsoft.dune2.gui;

public class ClippingArea {

    /* 0000(2)   */ public int left;                       /*!< ?? */
    /* 0002(2)   */ public int top;                        /*!< ?? */
    /* 0004(2)   */ public int right;                      /*!< ?? */
    /* 0006(2)   */ public int bottom;                     /*!< ?? */

    public ClippingArea(int left, int top, int right, int bottom) {
        this.left = left;
        this.top = top;
        this.right = right;
        this.bottom = bottom;
    }
}
