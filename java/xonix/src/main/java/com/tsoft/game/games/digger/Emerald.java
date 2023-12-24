package com.tsoft.game.games.digger;

public class Emerald {
    private World world;

    private byte emfield[] = {    //[150]
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};

    private int embox[] = {8, 12, 12, 9, 16, 12, 6, 9};    // [8]

    private int emmask = 0;

    public Emerald(World world) {
        this.world = world;
    }

    public void killEmerald(int x, int y) {
        if ((emfield[y * 15 + x + 15] & emmask) != 0) {
            emfield[y * 15 + x + 15] &= ~emmask;
            world.drawing.eraseEmerald(x * 20 + 12, (y + 1) * 18 + 21);
        }
    }

    public int countEm() {
        int n = 0;
        for (int x = 0; x < 15; x++)
            for (int y = 0; y < 10; y++)
                if ((emfield[y * 15 + x] & emmask) != 0)
                    n++;
        return n;
    }

    public void drawEmeralds() {
        emmask = 1 << world.main.getCurrPlayer();
        for (int x = 0; x < 15; x++)
            for (int y = 0; y < 10; y++)
                if ((emfield[y * 15 + x] & emmask) != 0)
                    world.drawing.drawEmerald(x * 20 + 12, y * 18 + 21);
    }

    public boolean checkDiggerHitEmerald(int x, int y, int r, int off) {
        boolean hit = false;
        if ((emfield[y * 15 + x] & emmask) != 0) {
            if (r == embox[off]) {
                world.drawing.drawEmerald(x * 20 + 12, y * 18 + 21);
                world.main.incPenalty();
            }

            if (r == embox[off + 1]) {
                world.drawing.eraseEmerald(x * 20 + 12, y * 18 + 21);
                world.main.incPenalty();
                hit = true;
                emfield[y * 15 + x] &= ~emmask;
            }
        }
        return hit;
    }

    public void makeEmField() {
        emmask = 1 << world.main.getCurrPlayer();
        for (int x = 0; x < 15; x++)
            for (int y = 0; y < 10; y++)
                if (world.main.getlevch(x, y, world.main.levplan()) == 'C')
                    emfield[y * 15 + x] |= emmask;
                else
                    emfield[y * 15 + x] &= ~emmask;
    }

}
