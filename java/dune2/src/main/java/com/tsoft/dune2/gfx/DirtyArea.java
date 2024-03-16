package com.tsoft.dune2.gfx;

public class DirtyArea {

    public int left;
    public int top;
    public int right;
    public int bottom;

    public DirtyArea(int left, int top, int right, int bottom) {
        this.left = left;
        this.top = top;
        this.right = right;
        this.bottom = bottom;
    }
}
