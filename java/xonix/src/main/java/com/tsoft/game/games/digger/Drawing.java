package com.tsoft.game.games.digger;

public class Drawing {
    private World world;

    private int field1[][] = new int[15][10];     // [150]

    private int field2[][] = new int[15][10];    // [150]

    public int field[][] = new int[15][10];    // [150]

    private short[]
            diggerbuf = new short[480],
            bagbuf1 = new short[480],
            bagbuf2 = new short[480],
            bagbuf3 = new short[480],
            bagbuf4 = new short[480],
            bagbuf5 = new short[480],
            bagbuf6 = new short[480],
            bagbuf7 = new short[480],
            monbuf1 = new short[480],
            monbuf2 = new short[480],
            monbuf3 = new short[480],
            monbuf4 = new short[480],
            monbuf5 = new short[480],
            monbuf6 = new short[480],
            bonusbuf = new short[480],
            firebuf = new short[128];

    private int bitmasks[] = {0xfffe, 0xfffd, 0xfffb, 0xfff7, 0xffef, 0xffdf, 0xffbf, 0xff7f, 0xfeff, 0xfdff, 0xfbff, 0xf7ff};    // [12]

    private int monspr[] = {0, 0, 0, 0, 0, 0};    // [6]
    private int monspd[] = {0, 0, 0, 0, 0, 0};    // [6]

    private int digspr = 0;
    private int digspd = 0;
    private int firespr = 0;

    private static final int FIRE_WIDTH = 2;
    private static final int FIRE_HEIGHT = 8;

    public Drawing(World world) {
        this.world = world;
    }

    private void createDbfspr() {
        digspd = 1;
        digspr = 0;
        firespr = 0;
        world.sprite.createspr(0, 0, diggerbuf, 4, 15);
        world.sprite.createspr(14, 81, bonusbuf, 4, 15);
        world.sprite.createspr(15, 82, firebuf, FIRE_WIDTH, FIRE_HEIGHT);
    }

    public void createMbspr() {
        world.sprite.createspr(1, 62, bagbuf1, 4, 15);
        world.sprite.createspr(2, 62, bagbuf2, 4, 15);
        world.sprite.createspr(3, 62, bagbuf3, 4, 15);
        world.sprite.createspr(4, 62, bagbuf4, 4, 15);
        world.sprite.createspr(5, 62, bagbuf5, 4, 15);
        world.sprite.createspr(6, 62, bagbuf6, 4, 15);
        world.sprite.createspr(7, 62, bagbuf7, 4, 15);
        world.sprite.createspr(8, 71, monbuf1, 4, 15);
        world.sprite.createspr(9, 71, monbuf2, 4, 15);
        world.sprite.createspr(10, 71, monbuf3, 4, 15);
        world.sprite.createspr(11, 71, monbuf4, 4, 15);
        world.sprite.createspr(12, 71, monbuf5, 4, 15);
        world.sprite.createspr(13, 71, monbuf6, 4, 15);
        createDbfspr();

        for (int i = 0; i < 6; i++) {
            monspr[i] = 0;
            monspd[i] = 1;
        }
    }

    private void drawBackg(int l) {
        for (int y = 14; y < 200; y += 4)
            for (int x = 0; x < 320; x += 20)
                world.sprite.drawmiscspr(x, y, 93 + l, 5, 4);
    }

    public void drawBonus(int x, int y) {
        world.sprite.initspr(14, 81, 4, 15);
        world.sprite.movedrawspr(14, x, y);
    }

    public void drawBottomBlob(int x, int y) {
        world.sprite.initmiscspr(x - 4, y + 15, 6, 6);
        world.sprite.drawmiscspr(x - 4, y + 15, 105, 6, 6);
        world.sprite.getis();
    }

    public int drawDigger(Input.Dir dir, int x, int y, boolean f) {
        digspr += digspd;
        if (digspr == 2 || digspr == 0)
            digspd = -digspd;
        if (digspr > 2)
            digspr = 2;
        if (digspr < 0)
            digspr = 0;

        int t = dir.getOff();
        if (t >= 0 && t <= 6 && !((t & 1) != 0)) {
            world.sprite.initspr(0, (t + (f ? 0 : 1)) * 3 + digspr + 1, 4, 15);
            return world.sprite.drawspr(0, x, y);
        }

        return 0;
    }

    public int drawDiggerAnim(int t, int x, int y, boolean f) {
        digspr += digspd;
        if (digspr == 2 || digspr == 0)
            digspd = -digspd;
        if (digspr > 2)
            digspr = 2;
        if (digspr < 0)
            digspr = 0;

        if (t >= 10 && t <= 15) {
            world.sprite.initspr(0, 40 - t, 4, 15);
            return world.sprite.drawspr(0, x, y);
        }
        return 0;
    }

    public void drawEmerald(int x, int y) {
        world.sprite.initmiscspr(x, y, 4, 10);
        world.sprite.drawmiscspr(x, y, 108, 4, 10);
        world.sprite.getis();
    }

    private void drawField() {
        for (int x = 0; x < 15; x++)
            for (int y = 0; y < 10; y++)
                if ((field[x][y] & 0x2000) == 0) {
                    int xp = x * 20 + 12;
                    int yp = y * 18 + 18;
                    if ((field[x][y] & 0xfc0) != 0xfc0) {
                        field[x][y] &= 0xd03f;
                        drawBottomBlob(xp, yp - 15);
                        drawBottomBlob(xp, yp - 12);
                        drawBottomBlob(xp, yp - 9);
                        drawBottomBlob(xp, yp - 6);
                        drawBottomBlob(xp, yp - 3);
                        drawTopBlob(xp, yp + 3);
                    }

                    if ((field[x][y] & 0x1f) != 0x1f) {
                        field[x][y] &= 0xdfe0;
                        drawRightBlob(xp - 16, yp);
                        drawRightBlob(xp - 12, yp);
                        drawRightBlob(xp - 8, yp);
                        drawRightBlob(xp - 4, yp);
                        drawLeftBlob(xp + 4, yp);
                    }

                    if (x < 14)
                        if ((field[x + 1][y] & 0xfdf) != 0xfdf)
                            drawRightBlob(xp, yp);

                    if (y < 9)
                        if ((field[x][y + 1] & 0xfdf) != 0xfdf)
                            drawBottomBlob(xp, yp);
                }
    }

    public int drawFire(int x, int y, int t) {
        if (t == 0) {
            firespr++;
            if (firespr > 2)
                firespr = 0;
            world.sprite.initspr(15, 82 + firespr, FIRE_WIDTH, FIRE_HEIGHT);
        } else
            world.sprite.initspr(15, 84 + t, FIRE_WIDTH, FIRE_HEIGHT);
        return world.sprite.drawspr(15, x, y);
    }

    public void drawFurryBlob(int x, int y) {
        world.sprite.initmiscspr(x - 4, y + 15, 6, 8);
        world.sprite.drawmiscspr(x - 4, y + 15, 107, 6, 8);
        world.sprite.getis();
    }

    public int drawGold(int n, int t, int x, int y) {
        world.sprite.initspr(n, t + 62, 4, 15);
        return world.sprite.drawspr(n, x, y);
    }

    public void drawLeftBlob(int x, int y) {
        world.sprite.initmiscspr(x - 8, y - 1, 2, 18);
        world.sprite.drawmiscspr(x - 8, y - 1, 104, 2, 18);
        world.sprite.getis();
    }

    private void drawLife(int t, int x, int y) {
        world.sprite.drawmiscspr(x, y, t + 110, 4, 12);
    }

    public void drawLives() {
        int n;
        n = world.main.getLives(1) - 1;
        for (int l = 1; l < 5; l++) {
            drawLife(n > 0 ? 0 : 2, l * 20 + 60, 0);
            n--;
        }

        if (world.main.nplayers == 2) {
            n = world.main.getLives(2) - 1;
            for (int l = 1; l < 5; l++) {
                drawLife(n > 0 ? 1 : 2, 244 - l * 20, 0);
                n--;
            }
        }
    }

    public int drawMonster(int n, boolean nobf, Input.Dir dir, int x, int y) {
        monspr[n] += monspd[n];
        if (monspr[n] == 2 || monspr[n] == 0)
            monspd[n] = -monspd[n];
        if (monspr[n] > 2)
            monspr[n] = 2;
        if (monspr[n] < 0)
            monspr[n] = 0;

        if (nobf)
            world.sprite.initspr(n + 8, monspr[n] + 69, 4, 15);
        else
            switch (dir) {
                case RIGHT:
                    world.sprite.initspr(n + 8, monspr[n] + 73, 4, 15);
                    break;
                case LEFT:
                    world.sprite.initspr(n + 8, monspr[n] + 77, 4, 15);
                    break;
            }

        return world.sprite.drawspr(n + 8, x, y);
    }

    public int drawMonsterDie(int n, boolean nobf, Input.Dir dir, int x, int y) {
        if (nobf)
            world.sprite.initspr(n + 8, 72, 4, 15);
        else
            switch (dir) {
                case RIGHT:
                    world.sprite.initspr(n + 8, 76, 4, 15);
                    break;
                case LEFT:
                    world.sprite.initspr(n + 8, 80, 4, 14);
                    break;
            }

        return world.sprite.drawspr(n + 8, x, y);
    }

    public void drawRightBlob(int x, int y) {
        world.sprite.initmiscspr(x + 16, y - 1, 2, 18);
        world.sprite.drawmiscspr(x + 16, y - 1, 102, 2, 18);
        world.sprite.getis();
    }

    public void drawSquareBlob(int x, int y) {
        world.sprite.initmiscspr(x - 4, y + 17, 6, 6);
        world.sprite.drawmiscspr(x - 4, y + 17, 106, 6, 6);
        world.sprite.getis();
    }

    public void drawStatics() {
        loadField();

        world.pc.gpal(0);
        world.pc.ginten(0);
        drawBackg(world.main.levplan());
        drawField();
        world.pc.currentSource.newPixels(0, 0, world.pc.width, world.pc.height);
    }

    public void drawTopBlob(int x, int y) {
        world.sprite.initmiscspr(x - 4, y - 6, 6, 6);
        world.sprite.drawmiscspr(x - 4, y - 6, 103, 6, 6);
        world.sprite.getis();
    }

    public void eatField(int xPix, int yPix, Input.Dir dir) {
        int x = (xPix - 12) / 20;
        int xr = ((xPix - 12) % 20) / 4;
        int y = (yPix - 18) / 18;
        int yr = ((yPix - 18) % 18) / 3;
        world.main.incPenalty();
        switch (dir) {
            case RIGHT:
                x++;
                field[x][y] &= bitmasks[xr];
                if ((field[x][y] & 0x1f) != 0)
                    break;
                field[x][y] &= 0xdfff;
                break;

            case LEFT:
                xr--;
                if (xr < 0) {
                    xr += 5;
                    x--;
                }
                field[x][y] &= bitmasks[xr];
                if ((field[x][y] & 0x1f) != 0)
                    break;
                field[x][y] &= 0xdfff;
                break;

            case UP:
                yr--;
                if (yr < 0) {
                    yr += 6;
                    y--;
                }
                field[x][y] &= bitmasks[6 + yr];
                if ((field[x][y] & 0xfc0) != 0)
                    break;
                field[x][y] &= 0xdfff;
                break;

            case DOWN:
                y++;
                field[x][y] &= bitmasks[6 + yr];
                if ((field[x][y] & 0xfc0) != 0)
                    break;
                field[x][y] &= 0xdfff;
                break;
        }
    }

    public void eraseEmerald(int x, int y) {
        world.sprite.initmiscspr(x, y, 4, 10);
        world.sprite.drawmiscspr(x, y, 109, 4, 10);
        world.sprite.getis();
    }

    private void initDbfspr() {
        digspd = 1;
        digspr = 0;
        firespr = 0;
        world.sprite.initspr(0, 0, 4, 15);
        world.sprite.initspr(14, 81, 4, 15);
        world.sprite.initspr(15, 82, FIRE_WIDTH, FIRE_HEIGHT);
    }

    public void initMbspr() {
        world.sprite.initspr(1, 62, 4, 15);
        world.sprite.initspr(2, 62, 4, 15);
        world.sprite.initspr(3, 62, 4, 15);
        world.sprite.initspr(4, 62, 4, 15);
        world.sprite.initspr(5, 62, 4, 15);
        world.sprite.initspr(6, 62, 4, 15);
        world.sprite.initspr(7, 62, 4, 15);
        world.sprite.initspr(8, 71, 4, 15);
        world.sprite.initspr(9, 71, 4, 15);
        world.sprite.initspr(10, 71, 4, 15);
        world.sprite.initspr(11, 71, 4, 15);
        world.sprite.initspr(12, 71, 4, 15);
        world.sprite.initspr(13, 71, 4, 15);
        initDbfspr();
    }

    public void makeField() {
        for (int x = 0; x < 15; x++)
            for (int y = 0; y < 10; y++) {
                field[x][y] = -1;
                int c = world.main.getlevch(x, y, world.main.levplan());
                if (c == 'S' || c == 'V')
                    field[x][y] &= 0xd03f;
                if (c == 'S' || c == 'H')
                    field[x][y] &= 0xdfe0;

                if (world.main.getCurrPlayer() == 0)
                    field1[x][y] = field[x][y];
                else
                    field2[x][y] = field[x][y];
            }
    }

    public void outText(String p, int x, int y, int c) {
        outText(p, x, y, c, false);
    }

    public void outText(String p, int x, int y, int c, boolean b) {
        int rx = x;
        for (int i = 0; i < p.length(); i++) {
            world.pc.gwrite(x, y, p.charAt(i), c);
            x += 12;
        }
        if (b)
            world.pc.currentSource.newPixels(rx, y, p.length() * 12, 12);
    }

    public void saveField() {
        for (int x = 0; x < 15; x++) {
            for (int y = 0; y < 10; y++) {
                if (world.main.getCurrPlayer() == 0)
                    field1[x][y] = field[x][y];
                else
                    field2[x][y] = field[x][y];
            }
        }
    }

    private void loadField() {
        for (int x = 0; x < 15; x++)
            for (int y = 0; y < 10; y++)
                if (world.main.getCurrPlayer() == 0)
                    field[x][y] = field1[x][y];
                else
                    field[x][y] = field2[x][y];
    }

    public int getField(int x, int y) {
        if (x < 0 || x > 14) throw new IllegalArgumentException("x=" + x + " but must be in [0, 14]");
        if (y < 0 || y > 9) throw new IllegalArgumentException("y=" + y + " but must be in [0, 9]");
        return field[x][y];
    }

    public boolean isFieldClear(Input.Dir dir, int x, int y) {
        switch (dir) {
            case RIGHT:
                if (x < 14)
                    if ((getField(x + 1, y) & 0x2000) == 0)
                        if ((getField(x + 1, y) & 1) == 0 || (getField(x, y) & 0x10) == 0)
                            return true;
                break;
            case LEFT:
                if (x > 0)
                    if ((getField(x - 1, y) & 0x2000) == 0)
                        if ((getField(x - 1, y) & 0x10) == 0 || (getField(x, y) & 1) == 0)
                            return true;
                break;
            case UP:
                if (y > 0)
                    if ((getField(x, y - 1) & 0x2000) == 0)
                        if ((getField(x, y - 1) & 0x800) == 0 || (getField(x, y) & 0x40) == 0)
                            return true;
                break;
            case DOWN:
                if (y < 9)
                    if ((getField(x, y + 1) & 0x2000) == 0)
                        if ((getField(x, y + 1) & 0x40) == 0 || (getField(x, y) & 0x800) == 0)
                            return true;
                break;
        }
        return false;
    }
}
