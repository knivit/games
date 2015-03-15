package com.tsoft.game.digger;

public class Bags {
    private World world;

    private Bag[] bagdat1 = {new Bag(), new Bag(), new Bag(), new Bag(), new Bag(), new Bag(), new Bag(), new Bag()};
    private Bag[] bagdat2 = {new Bag(), new Bag(), new Bag(), new Bag(), new Bag(), new Bag(), new Bag(), new Bag()};
    private Bag[] bagdat = {new Bag(), new Bag(), new Bag(), new Bag(), new Bag(), new Bag(), new Bag(), new Bag()};

    private int pushcount = 0;
    private int goldtime = 0;

    private int wblanim[] = {2, 0, 1, 0};  // [4]

    public Bags(World world) {
        this.world = world;
    }

    public int bagBits() {
        int bags = 0;
        for (int bag = 1, b = 2; bag < 8; bag++, b <<= 1)
            if (bagdat[bag].exist)
                bags |= b;
        return bags;
    }

    private void bagHitGround(int bag) {
        if (bagdat[bag].dir == Input.Dir.DOWN && bagdat[bag].fallh > 1)
            bagdat[bag].gt = 1;
        else
            bagdat[bag].fallh = 0;

        bagdat[bag].dir = Input.Dir.NONE;
        bagdat[bag].wt = 15;
        bagdat[bag].wobbling = false;

        int clbits = world.drawing.drawGold(bag, 0, bagdat[bag].xPix, bagdat[bag].yPix);
        world.main.incPenalty();

        for (int i = 1, b = 2; i < 8; i++, b <<= 1) {
            if ((b & clbits) != 0)
                removeBag(i);
        }
    }

    public int getBagY(int bag) {
        return bagdat[bag].yPix;
    }

    public void cleanupBags() {
        world.sound.soundfalloff();

        for (int bag = 1; bag < 8; bag++) {
            if (bagdat[bag].exist && ((bagdat[bag].x == 7 && bagdat[bag].y == 9) ||
                    bagdat[bag].xr != 0 || bagdat[bag].yr != 0 || bagdat[bag].gt != 0 ||
                    bagdat[bag].fallh != 0 || bagdat[bag].wobbling)) {

                bagdat[bag].exist = false;
                world.sprite.erasespr(bag);
            }

            if (world.main.getCurrPlayer() == 0)
                bagdat1[bag].copyFrom(bagdat[bag]);
            else
                bagdat2[bag].copyFrom(bagdat[bag]);
        }
    }

    public void doBags() {
        boolean soundfalloffflag = true, soundwobbleoffflag = true;
        for (int bag = 1; bag < 8; bag++)
            if (bagdat[bag].exist) {
                if (bagdat[bag].gt != 0) {
                    if (bagdat[bag].gt == 1) {
                        world.sound.soundbreak();
                        world.drawing.drawGold(bag, 4, bagdat[bag].xPix, bagdat[bag].yPix);
                        world.main.incPenalty();
                    }
                    if (bagdat[bag].gt == 3) {
                        world.drawing.drawGold(bag, 5, bagdat[bag].xPix, bagdat[bag].yPix);
                        world.main.incPenalty();
                    }
                    if (bagdat[bag].gt == 5) {
                        world.drawing.drawGold(bag, 6, bagdat[bag].xPix, bagdat[bag].yPix);
                        world.main.incPenalty();
                    }

                    bagdat[bag].gt++;
                    if (bagdat[bag].gt == goldtime)
                        removeBag(bag);
                    else if (bagdat[bag].y < 9 && bagdat[bag].gt < goldtime - 10)
                        if ((world.drawing.getField(bagdat[bag].x, bagdat[bag].y + 1) & 0x2000) == 0)
                            bagdat[bag].gt = goldtime - 10;
                } else
                    updateBag(bag);
            }

        for (int bag = 1; bag < 8; bag++) {
            if (bagdat[bag].dir == Input.Dir.DOWN && bagdat[bag].exist)
                soundfalloffflag = false;
            if (bagdat[bag].dir != Input.Dir.DOWN && bagdat[bag].wobbling && bagdat[bag].exist)
                soundwobbleoffflag = false;
        }

        if (soundfalloffflag)
            world.sound.soundfalloff();
        if (soundwobbleoffflag)
            world.sound.soundwobbleoff();
    }

    public void drawBags() {
        for (int bag = 1; bag < 8; bag++) {
            if (world.main.getCurrPlayer() == 0)
                bagdat[bag].copyFrom(bagdat1[bag]);
            else
                bagdat[bag].copyFrom(bagdat2[bag]);

            if (bagdat[bag].exist)
                world.sprite.movedrawspr(bag, bagdat[bag].xPix, bagdat[bag].yPix);
        }
    }

    public Input.Dir getBagDir(int bag) {
        if (bagdat[bag].exist)
            return bagdat[bag].dir;
        return Input.Dir.NONE;
    }

    private void getGold(int bag) {
        int clbits;
        clbits = world.drawing.drawGold(bag, 6, bagdat[bag].xPix, bagdat[bag].yPix);
        world.main.incPenalty();
        if ((clbits & 1) != 0) {
            world.scores.scoregold();
            world.sound.soundgold();
            world.digger.digtime = 0;
        } else
            world.monsters.mongold();
        removeBag(bag);
    }

    public int getNMovingBags() {
        int n = 0;
        for (int bag = 1; bag < 8; bag++)
            if (bagdat[bag].exist && bagdat[bag].gt < 10 &&
                    (bagdat[bag].gt != 0 || bagdat[bag].wobbling))
                n++;
        return n;
    }

    public void initBags() {
        pushcount = 0;
        goldtime = 150 - world.main.levof10() * 10;
        for (int bag = 1; bag < 8; bag++)
            bagdat[bag].exist = false;

        int bag = 1;
        for (int x = 0; x < 15; x++)
            for (int y = 0; y < 10; y++)
                if (world.main.getlevch(x, y, world.main.levplan()) == 'B')
                    if (bag < 8) {
                        bagdat[bag].init(x, y);
                        bag ++;
                    }

        if (world.main.getCurrPlayer() == 0) {
            for (int i = 1; i < 8; i++)
                bagdat1[i].copyFrom(bagdat[i]);
        } else {
            for (int i = 1; i < 8; i++)
                bagdat2[i].copyFrom(bagdat[i]);
        }
    }

    private boolean pushBag(int bag, Input.Dir dir) {
        boolean push = true;
        int x = bagdat[bag].xPix;
        int y = bagdat[bag].yPix;
        int ox = x;
        int oy = y;
        int h = bagdat[bag].x;
        int v = bagdat[bag].y;
        if (bagdat[bag].gt != 0) {
            getGold(bag);
            return true;
        }

        if (bagdat[bag].dir == Input.Dir.DOWN && (dir == Input.Dir.LEFT || dir == Input.Dir.RIGHT)) {
            int clbits = world.drawing.drawGold(bag, 3, x, y);
            world.main.incPenalty();
            if (((clbits & 1) != 0) && (world.digger.diggery >= y))
                world.digger.killDigger(1, bag);
            if ((clbits & 0x3f00) != 0)
                world.monsters.squashMonsters(bag, clbits);

            return true;
        }

        if ((x == 292 && dir == Input.Dir.RIGHT) || (x == 12 && dir == Input.Dir.LEFT) || (y == 180 && dir == Input.Dir.DOWN) ||
                (y == 18 && dir == Input.Dir.UP))
            push = false;

        if (push) {
            switch (dir) {
                case RIGHT:
                    x += 4;
                    break;
                case LEFT:
                    x -= 4;
                    break;
                case DOWN:
                    if (bagdat[bag].unfallen) {
                        bagdat[bag].unfallen = false;
                        world.drawing.drawSquareBlob(x, y);
                        world.drawing.drawTopBlob(x, y + 21);
                    } else
                        world.drawing.drawFurryBlob(x, y);

                    world.drawing.eatField(x, y, dir);
                    world.emerald.killEmerald(h, v);
                    y += 6;
                    break;
            }

            switch (dir) {
                case DOWN: {
                    int clbits = world.drawing.drawGold(bag, 3, x, y);
                    world.main.incPenalty();
                    if (((clbits & 1) != 0) && world.digger.diggery >= y)
                        world.digger.killDigger(1, bag);
                    if ((clbits & 0x3f00) != 0)
                        world.monsters.squashMonsters(bag, clbits);
                    break;
                }

                case RIGHT: case LEFT: {
                    bagdat[bag].wt = 15;
                    bagdat[bag].wobbling = false;
                    int clbits = world.drawing.drawGold(bag, 0, x, y);
                    world.main.incPenalty();
                    pushcount = 1;
                    if ((clbits & 0xfe) != 0)
                        if (!pushBags(dir, clbits)) {
                            x = ox;
                            y = oy;
                            world.drawing.drawGold(bag, 0, ox, oy);
                            world.main.incPenalty();
                            push = false;
                        }
                    if (((clbits & 1) != 0) || ((clbits & 0x3f00) != 0)) {
                        x = ox;
                        y = oy;
                        world.drawing.drawGold(bag, 0, ox, oy);
                        world.main.incPenalty();
                        push = false;
                    }
                    break;
                }
            }

            if (push)
                bagdat[bag].dir = dir;
            else
                bagdat[bag].dir = dir.getReverse();

            bagdat[bag].xPix = x;
            bagdat[bag].yPix = y;
            bagdat[bag].x = (x - 12) / 20;
            bagdat[bag].y = (y - 18) / 18;
            bagdat[bag].xr = (x - 12) % 20;
            bagdat[bag].yr = (y - 18) % 18;
        }
        return push;
    }

    public boolean pushBags(Input.Dir dir, int bits) {
        boolean push = true;
        for (int bag = 1, bit = 2; bag < 8; bag++, bit <<= 1)
            if ((bits & bit) != 0)
                if (!pushBag(bag, dir))
                    push = false;
        return push;
    }

    public boolean pushudbags(int bits) {
        boolean push = true;
        for (int bag = 1, b = 2; bag < 8; bag++, b <<= 1)
            if ((bits & b) != 0)
                if (bagdat[bag].gt != 0)
                    getGold(bag);
                else
                    push = false;
        return push;
    }

    private void removeBag(int bag) {
        if (bagdat[bag].exist) {
            bagdat[bag].exist = false;
            world.sprite.erasespr(bag);
        }
    }

    public void removeBags(int bits) {
        for (int bag = 1, b = 2; bag < 8; bag++, b <<= 1)
            if ((bagdat[bag].exist) && ((bits & b) != 0))
                removeBag(bag);
    }

    private void updateBag(int bag) {
        int xPix = bagdat[bag].xPix;
        int yPix = bagdat[bag].yPix;

        int x = bagdat[bag].x;
        int y = bagdat[bag].y;

        int xr = bagdat[bag].xr;
        int yr = bagdat[bag].yr;

        switch (bagdat[bag].dir) {
            case NONE:
                if (yPix < 180 && xr == 0) {
                    if (bagdat[bag].wobbling) {
                        if (bagdat[bag].wt == 0) {
                            bagdat[bag].dir = Input.Dir.DOWN;
                            world.sound.soundfall();
                            break;
                        }

                        bagdat[bag].wt--;
                        int wbl = bagdat[bag].wt % 8;
                        if (!((wbl & 1) != 0)) {
                            world.drawing.drawGold(bag, wblanim[wbl >> 1], xPix, yPix);
                            world.main.incPenalty();
                            world.sound.soundwobble();
                        }
                    } else if ((world.drawing.getField(x, y + 1) & 0xfdf) != 0xfdf)
                        if (!world.digger.checkDiggerUnderBag(x, y + 1))
                            bagdat[bag].wobbling = true;
                } else {
                    bagdat[bag].wt = 15;
                    bagdat[bag].wobbling = false;
                }
                break;

            case RIGHT:
            case LEFT:
                if (xr == 0)
                    if (yPix < 180 && (world.drawing.getField(x, y + 1) & 0xfdf) != 0xfdf) {
                        bagdat[bag].dir = Input.Dir.DOWN;
                        bagdat[bag].wt = 0;
                        world.sound.soundfall();
                    } else
                        bagHitGround(bag);
                break;

            case DOWN:
                if (yr == 0)
                    bagdat[bag].fallh++;
                if (yPix >= 180)
                    bagHitGround(bag);
                else if ((world.drawing.getField(x, y + 1) & 0xfdf) == 0xfdf)
                    if (yr == 0)
                        bagHitGround(bag);
                world.monsters.checkMonsterScared(bagdat[bag].x);
                break;
        }

        if (bagdat[bag].dir != Input.Dir.NONE)
            if (bagdat[bag].dir != Input.Dir.DOWN && pushcount != 0)
                pushcount--;
            else
                pushBag(bag, bagdat[bag].dir);
    }
}
