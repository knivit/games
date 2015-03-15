package com.tsoft.game.digger;

public class Sprite {
    private World world;

    private boolean sprrdrwf[] = {false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false};    // [17]
    private boolean sprrecf[] = {false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false};    // [17]
    private boolean sprenf[] = {false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false,};    // [16]

    private int sprch[] = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};    // [17]

    private short sprmov[][] = {null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null};    // [16]

    private int sprx[] = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};    // [17]
    private int spry[] = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};    // [17]
    private int sprwid[] = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};    // [17]
    private int sprhei[] = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};    // [17]
    private int sprbwid[] = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};    // [16]
    private int sprbhei[] = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};    // [16]
    private int sprnch[] = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};    // [16]
    private int sprnwid[] = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};    // [16]
    private int sprnhei[] = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};    // [16]
    private int sprnbwid[] = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};    // [16]
    private int sprnbhei[] = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};    // [16]

    private int defsprorder[] = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15};    // [16]
    private int sprorder[] = defsprorder;

    public Sprite(World world) {
        this.world = world;
    }

    private boolean bcollide(int bx, int si) {
        if (sprx[bx] >= sprx[si]) {
            if (sprx[bx] + sprbwid[bx] > sprwid[si] * 4 + sprx[si] - sprbwid[si] - 1)
                return false;
        } else if (sprx[si] + sprbwid[si] > sprwid[bx] * 4 + sprx[bx] - sprbwid[bx] - 1)
            return false;
        if (spry[bx] >= spry[si]) {
            if (spry[bx] + sprbhei[bx] <= sprhei[si] + spry[si] - sprbhei[si] - 1)
                return true;
            return false;
        }
        if (spry[si] + sprbhei[si] <= sprhei[bx] + spry[bx] - sprbhei[bx] - 1)
            return true;
        return false;
    }

    private int bcollides(int bx) {
        int si = bx, ax = 0, dx = 0;
        bx = 0;
        do {
            if (sprenf[bx] && bx != si) {
                if (bcollide(bx, si))
                    ax |= 1 << dx;
                sprx[bx] += 320;
                spry[bx] -= 2;
                if (bcollide(bx, si))
                    ax |= 1 << dx;
                sprx[bx] -= 640;
                spry[bx] += 4;
                if (bcollide(bx, si))
                    ax |= 1 << dx;
                sprx[bx] += 320;
                spry[bx] -= 2;
            }
            bx++;
            dx++;
        } while (dx != 16);
        return ax;
    }

    private void clearrdrwf() {
        clearrecf();
        for (int i = 0; i < 17; i++)
            sprrdrwf[i] = false;
    }

    private void clearrecf() {
        for (int i = 0; i < 17; i++)
            sprrecf[i] = false;
    }

    private boolean collide(int bx, int si) {
        if (sprx[bx] >= sprx[si]) {
            if (sprx[bx] > sprwid[si] * 4 + sprx[si] - 1)
                return false;
        } else if (sprx[si] > sprwid[bx] * 4 + sprx[bx] - 1)
            return false;

        if (spry[bx] >= spry[si]) {
            if (spry[bx] <= sprhei[si] + spry[si] - 1)
                return true;
            return false;
        }

        if (spry[si] <= sprhei[bx] + spry[bx] - 1)
            return true;
        return false;
    }

    public void createspr(int n, int ch, short[] mov, int width, int height) {
        sprnch[n & 15] = ch;
        sprch[n & 15] = ch;
        sprmov[n & 15] = mov;
        sprnwid[n & 15] = width;
        sprwid[n & 15] = width;
        sprnhei[n & 15] = height;
        sprhei[n & 15] = height;
        sprnbwid[n & 15] = 0;
        sprbwid[n & 15] = 0;
        sprnbhei[n & 15] = 0;
        sprbhei[n & 15] = 0;
        sprenf[n & 15] = false;
    }

    public void drawmiscspr(int x, int y, int ch, int wid, int hei) {
        sprx[16] = x & -4;
        spry[16] = y;
        sprch[16] = ch;
        sprwid[16] = wid;
        sprhei[16] = hei;
        world.pc.gputim(sprx[16], spry[16], sprch[16], sprwid[16], sprhei[16]);
    }

    public int drawspr(int n, int x, int y) {
        int t1, t2, t3, t4;
        int bx = n & 15;
        x &= -4;
        clearrdrwf();
        setrdrwflgs(bx);
        t1 = sprx[bx];
        t2 = spry[bx];
        t3 = sprwid[bx];
        t4 = sprhei[bx];
        sprx[bx] = x;
        spry[bx] = y;
        sprwid[bx] = sprnwid[bx];
        sprhei[bx] = sprnhei[bx];
        clearrecf();
        setrdrwflgs(bx);
        sprhei[bx] = t4;
        sprwid[bx] = t3;
        spry[bx] = t2;
        sprx[bx] = t1;
        sprrdrwf[bx] = true;
        putis();
        sprx[bx] = x;
        spry[bx] = y;
        sprch[bx] = sprnch[bx];
        sprwid[bx] = sprnwid[bx];
        sprhei[bx] = sprnhei[bx];
        sprbwid[bx] = sprnbwid[bx];
        sprbhei[bx] = sprnbhei[bx];
        world.pc.ggeti(sprx[bx], spry[bx], sprmov[bx], sprwid[bx], sprhei[bx]);
        putims();
        return bcollides(bx);
    }

    public void erasespr(int n) {
        int bx = n & 15;
        world.pc.gputi(sprx[bx], spry[bx], sprmov[bx], sprwid[bx], sprhei[bx], true);
        sprenf[bx] = false;
        clearrdrwf();
        setrdrwflgs(bx);
        putims();
    }

    public void getis() {
        for (int i = 0; i < 16; i++) {
            if (sprrdrwf[i])
                world.pc.ggeti(sprx[i], spry[i], sprmov[i], sprwid[i], sprhei[i]);
        }
        putims();
    }

    public void initmiscspr(int x, int y, int wid, int hei) {
        sprx[16] = x;
        spry[16] = y;
        sprwid[16] = wid;
        sprhei[16] = hei;
        clearrdrwf();
        setrdrwflgs(16);
        putis();
    }

    public void initspr(int n, int ch, int wid, int hei) {
        sprnch[n & 15] = ch;
        sprnwid[n & 15] = wid;
        sprnhei[n & 15] = hei;
        sprnbwid[n & 15] = 0;
        sprnbhei[n & 15] = 0;
    }

    public int movedrawspr(int n, int x, int y) {
        int bx = n & 15;
        sprx[bx] = x & -4;
        spry[bx] = y;
        sprch[bx] = sprnch[bx];
        sprwid[bx] = sprnwid[bx];
        sprhei[bx] = sprnhei[bx];
        sprbwid[bx] = sprnbwid[bx];
        sprbhei[bx] = sprnbhei[bx];
        clearrdrwf();
        setrdrwflgs(bx);
        putis();
        world.pc.ggeti(sprx[bx], spry[bx], sprmov[bx], sprwid[bx], sprhei[bx]);
        sprenf[bx] = true;
        sprrdrwf[bx] = true;
        putims();
        return bcollides(bx);
    }

    private void putims() {
        for (int i = 0; i < 16; i++) {
            int j = sprorder[i];
            if (sprrdrwf[j])
                world.pc.gputim(sprx[j], spry[j], sprch[j], sprwid[j], sprhei[j]);
        }
    }

    private void putis() {
        for (int i = 0; i < 16; i++) {
            if (sprrdrwf[i])
                world.pc.gputi(sprx[i], spry[i], sprmov[i], sprwid[i], sprhei[i]);
        }
    }

    private void setrdrwflgs(int n) {
        if (!sprrecf[n]) {
            sprrecf[n] = true;

            for (int i = 0; i < 16; i++) {
                if (sprenf[i] && i != n) {
                    if (collide(i, n)) {
                        sprrdrwf[i] = true;
                        setrdrwflgs(i);
                    }
                    sprx[i] += 320;
                    spry[i] -= 2;
                    if (collide(i, n)) {
                        sprrdrwf[i] = true;
                        setrdrwflgs(i);
                    }
                    sprx[i] -= 640;
                    spry[i] += 4;
                    if (collide(i, n)) {
                        sprrdrwf[i] = true;
                        setrdrwflgs(i);
                    }
                    sprx[i] += 320;
                    spry[i] -= 2;
                }
            }
        }
    }

    public void setsprorder(int[] newsprorder) {
        if (newsprorder == null)
            sprorder = defsprorder;
        else
            sprorder = newsprorder;
    }
}
