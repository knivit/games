package com.tsoft.game.games.digger;

public class Bag {
    public int xPix;
    public int yPix;
    public int x;
    public int y;
    public int xr;
    public int yr;
    public Input.Dir dir;
    public int wt;
    public int gt;
    public int fallh;

    public boolean wobbling;
    public boolean unfallen;
    public boolean exist;

    public void copyFrom(Bag t) {
        xPix = t.xPix;
        yPix = t.yPix;
        x = t.x;
        y = t.y;
        xr = t.xr;
        yr = t.yr;
        dir = t.dir;
        wt = t.wt;
        gt = t.gt;
        fallh = t.fallh;
        wobbling = t.wobbling;
        unfallen = t.unfallen;
        exist = t.exist;
    }

    public void init(int x, int y) {
        exist = true;
        gt = 0;
        fallh = 0;
        dir = Input.Dir.NONE;
        wobbling = false;
        wt = 15;
        unfallen = true;
        xPix = x * 20 + 12;
        yPix = y * 18 + 18;
        this.x = x;
        this.y = y;
        xr = 0;
        yr = 0;
    }
}
