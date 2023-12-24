package com.tsoft.game.games.digger;

public class Digger {
    public static final int MAX_RATE = 200;
    public static final int MIN_RATE = 40;

    private World world;

    public int frametime = 66;

    public String subaddr;

    public int diggerx = 0;
    public int diggery = 0;
    private int diggerh = 0;
    private int diggerv = 0;
    private int diggerrx = 0;
    private int diggerry = 0;
    private Input.Dir digmdir = Input.Dir.RIGHT;
    private Input.Dir digdir = Input.Dir.RIGHT;
    public int digtime = 0;
    private int rechargetime = 0;
    private int firex = 0;
    private int firey = 0;
    private Input.Dir firedir = Input.Dir.RIGHT;
    private int expsn = 0;
    private int deathstage = 0;
    private int deathbag = 0;
    private int deathani = 0;
    private int deathtime = 0;
    private int startbonustimeleft = 0;
    private int bonustimeleft = 0;
    public int eatmsc = 0;
    private int emocttime = 0;

    public boolean digonscr = false;
    private boolean notfiring = false;
    public boolean bonusvisible = false;
    public boolean bonusmode = false;
    private boolean diggervisible = false;

    public long time;
    public long ftime = 50;
    private int deatharc[] = {3, 5, 6, 6, 5, 3, 0};        // [7]

    public Digger(World world) {
        this.world = world;
    }

    public void initDigger() {
        diggerv = 9;
        digmdir = Input.Dir.LEFT;
        diggerh = 7;
        diggerx = diggerh * 20 + 12;
        digdir = Input.Dir.RIGHT;
        diggerrx = 0;
        diggerry = 0;
        digtime = 0;
        digonscr = true;
        deathstage = 1;
        diggervisible = true;
        diggery = diggerv * 18 + 18;
        world.sprite.movedrawspr(0, diggerx, diggery);
        notfiring = true;
        emocttime = 0;
        bonusvisible = bonusmode = false;
        world.input.firepressed = false;
        expsn = 0;
        rechargetime = 0;
    }

    public boolean checkDiggerUnderBag(int h, int v) {
        if (digmdir == Input.Dir.UP || digmdir == Input.Dir.DOWN)
            if ((diggerx - 12) / 20 == h)
                if ((diggery - 18) / 18 == v || (diggery - 18) / 18 + 1 == v)
                    return true;
        return false;
    }

    public void createBonus() {
        bonusvisible = true;
        world.drawing.drawBonus(292, 18);
    }

    private void diggerDie() {
        switch (deathstage) {
            case 1: {
                if (world.bags.getBagY(deathbag) + 6 > diggery)
                    diggery = world.bags.getBagY(deathbag) + 6;

                world.drawing.drawDiggerAnim(15, diggerx, diggery, false);
                world.main.incPenalty();
                if (world.bags.getBagDir(deathbag) == Input.Dir.NONE) {
                    world.sound.soundddie();
                    deathtime = 5;
                    deathstage = 2;
                    deathani = 0;
                    diggery -= 6;
                }
                break;
            }
            case 2: {
                if (deathtime != 0) {
                    deathtime--;
                    break;
                }
                if (deathani == 0)
                    world.sound.music(2);

                int clbits = world.drawing.drawDiggerAnim(14 - deathani, diggerx, diggery, false);
                world.main.incPenalty();
                if (deathani == 0 && ((clbits & 0x3f00) != 0))
                    world.monsters.killMonsters(clbits);
                if (deathani < 4) {
                    deathani++;
                    deathtime = 2;
                } else {
                    deathstage = 4;
                    if (world.sound.musicflag)
                        deathtime = 60;
                    else
                        deathtime = 10;
                }
                break;
            }
            case 3: {
                deathstage = 5;
                deathani = 0;
                deathtime = 0;
                break;
            }

            case 4: {
                if (deathtime != 0)
                    deathtime--;
                else
                    world.main.setdead(true);
                break;
            }

            case 5: {
                if (deathani >= 0 && deathani <= 6) {
                    world.drawing.drawDiggerAnim(15, diggerx, diggery - deatharc[deathani], false);
                    if (deathani == 6)
                        world.sound.musicoff();

                    world.main.incPenalty();
                    deathani++;
                    if (deathani == 1)
                        world.sound.soundddie();
                    if (deathani == 7) {
                        deathtime = 5;
                        deathani = 0;
                        deathstage = 2;
                    }
                }
                break;
            }
        }
    }

    public void doDigger() {
        newframe();
        if (expsn != 0)
            drawExplosion();
        else
            updateFire();

        if (diggervisible)
            if (digonscr) {
                if (digtime != 0) {
                    world.drawing.drawDigger(digmdir, diggerx, diggery, notfiring && rechargetime == 0);
                    world.main.incPenalty();
                    digtime--;
                } else
                    updateDigger();
            } else
                diggerDie();

        if (bonusmode && digonscr) {
            if (bonustimeleft != 0) {
                bonustimeleft--;
                if (startbonustimeleft != 0 || bonustimeleft < 20) {
                    startbonustimeleft--;
                    if ((bonustimeleft & 1) != 0) {
                        world.pc.ginten(0);
                        world.sound.soundbonus();
                    } else {
                        world.pc.ginten(1);
                        world.sound.soundbonus();
                    }

                    if (startbonustimeleft == 0) {
                        world.sound.music(0);
                        world.sound.soundbonusoff();
                        world.pc.ginten(1);
                    }
                }
            } else {
                endBonusMode();
                world.sound.soundbonusoff();
                world.sound.music(1);
            }
        }

        if (bonusmode && !digonscr) {
            endBonusMode();
            world.sound.soundbonusoff();
            world.sound.music(1);
        }

        if (emocttime > 0)
            emocttime--;
    }

    private void drawExplosion() {
        switch (expsn) {
            case 1:
                world.sound.soundexplode();
            case 2:
            case 3:
                world.drawing.drawFire(firex, firey, expsn);
                world.main.incPenalty();
                expsn++;
                break;
            default:
                killFire();
                expsn = 0;
                break;
        }
    }

    private void endBonusMode() {
        bonusmode = false;
        world.pc.ginten(0);
    }

    public void eraseBonus() {
        if (bonusvisible) {
            bonusvisible = false;
            world.sprite.erasespr(14);
        }
        world.pc.ginten(0);
    }

    public void eraseDigger() {
        world.sprite.erasespr(0);
        diggervisible = false;
    }

    private boolean getFirepFlag() {
        return world.input.firepflag;
    }

    public boolean hitEmerald(int x, int y, int rx, int ry, Input.Dir dir) {
        int off = dir.getOff();
        if (off < 0 || off > 6 || ((off & 1) != 0))
            return false;

        if (dir == Input.Dir.RIGHT && rx != 0)
            x++;
        if (dir == Input.Dir.DOWN && ry != 0)
            y++;

        int r;
        if (dir == Input.Dir.RIGHT || dir == Input.Dir.LEFT)
            r = rx;
        else
            r = ry;

        boolean hit = world.emerald.checkDiggerHitEmerald(x, y, r, off);
        return hit;
    }

    private void initBonusMode() {
        bonusmode = true;
        eraseBonus();
        world.pc.ginten(1);
        bonustimeleft = 250 - world.main.levof10() * 20;
        startbonustimeleft = 20;
        eatmsc = 1;
    }

    public void killDigger(int stage, int bag) {
        if (deathstage < 2 || deathstage > 4) {
            digonscr = false;
            deathstage = stage;
            deathbag = bag;
        }
    }

    public void killFire() {
        if (!notfiring) {
            notfiring = true;
            world.sprite.erasespr(15);
            world.sound.soundfireoff();
        }
    }

    public void newframe() {
        world.input.checkkeyb();
        time += frametime;
        long l = time - world.pc.gethrt();
        if (l > 0) {
            try {
                Thread.sleep((int) l);
            } catch (Exception e) {
            }
        }
        world.pc.currentSource.newPixels();
    }

    private void updateDigger() {
        Input.Dir ddir;
        boolean push = false;

        world.input.readdir();
        Input.Dir dir = world.input.getdir();
        if (dir == Input.Dir.RIGHT || dir == Input.Dir.UP || dir == Input.Dir.LEFT || dir == Input.Dir.DOWN)
            ddir = dir;
        else
            ddir = Input.Dir.NONE;

        if (diggerrx == 0 && (ddir == Input.Dir.UP || ddir == Input.Dir.DOWN))
            digdir = digmdir = ddir;
        if (diggerry == 0 && (ddir == Input.Dir.LEFT || ddir == Input.Dir.RIGHT))
            digdir = digmdir = ddir;
        if (dir == Input.Dir.NONE)
            digmdir = Input.Dir.NONE;
        else
            digmdir = digdir;

        if ((diggerx == 292 && digmdir == Input.Dir.RIGHT) || (diggerx == 12 && digmdir == Input.Dir.LEFT) ||
                (diggery == 180 && digmdir == Input.Dir.DOWN) || (diggery == 18 && digmdir == Input.Dir.UP))
            digmdir = Input.Dir.NONE;

        int diggerox = diggerx;
        int diggeroy = diggery;
        if (digmdir != Input.Dir.NONE)
            world.drawing.eatField(diggerox, diggeroy, digmdir);

        switch (digmdir) {
            case RIGHT:
                world.drawing.drawRightBlob(diggerx, diggery);
                diggerx += 4;
                break;
            case LEFT:
                world.drawing.drawLeftBlob(diggerx, diggery);
                diggerx -= 4;
                break;
            case UP:
                world.drawing.drawTopBlob(diggerx, diggery);
                diggery -= 3;
                break;
            case DOWN:
                world.drawing.drawBottomBlob(diggerx, diggery);
                diggery += 3;
                break;
        }

        if (hitEmerald((diggerx - 12) / 20, (diggery - 18) / 18, (diggerx - 12) % 20,
                (diggery - 18) % 18, digmdir)) {
            world.scores.scoreemerald();
            world.sound.soundem();
            world.sound.soundemerald(emocttime);
            emocttime = 9;
        }

        int clbits = world.drawing.drawDigger(digdir, diggerx, diggery, notfiring && rechargetime == 0);
        world.main.incPenalty();
        if ((world.bags.bagBits() & clbits) != 0) {
            if (digmdir == Input.Dir.RIGHT || digmdir == Input.Dir.LEFT) {
                push = world.bags.pushBags(digmdir, clbits);
                digtime++;
            } else if (!world.bags.pushudbags(clbits))
                push = false;
            if (!push) { /* Strange, push not completely defined */
                diggerx = diggerox;
                diggery = diggeroy;
                world.drawing.drawDigger(digmdir, diggerx, diggery, notfiring && rechargetime == 0);
                world.main.incPenalty();
                digdir = digmdir.getReverse();
            }
        }

        if (((clbits & 0x3f00) != 0) && bonusmode)
            for (int nmon = world.monsters.killMonsters(clbits); nmon != 0; nmon--) {
                world.sound.soundeatm();
                world.scores.scoreeatm();
            }

        if ((clbits & 0x4000) != 0) {
            world.scores.scorebonus();
            initBonusMode();
        }

        diggerh = (diggerx - 12) / 20;
        diggerrx = (diggerx - 12) % 20;
        diggerv = (diggery - 18) / 18;
        diggerry = (diggery - 18) % 18;
    }

    private void updateFire() {
        int pix = 0;
        if (notfiring) {
            if (rechargetime != 0)
                rechargetime--;
            else if (getFirepFlag())
                if (digonscr) {
                    rechargetime = world.main.levof10() * 3 + 60;
                    notfiring = false;
                    switch (digdir) {
                        case RIGHT:
                            firex = diggerx + 8;
                            firey = diggery + 4;
                            break;
                        case LEFT:
                            firex = diggerx;
                            firey = diggery + 4;
                            break;
                        case UP:
                            firex = diggerx + 4;
                            firey = diggery;
                            break;
                        case DOWN:
                            firex = diggerx + 4;
                            firey = diggery + 8;
                            break;
                    }
                    firedir = digdir;
                    world.sprite.movedrawspr(15, firex, firey);
                    world.sound.soundfire();
                }
        } else {
            switch (firedir) {
                case RIGHT:
                    firex += 8;
                    pix = world.pc.ggetpix(firex, firey + 4) | world.pc.ggetpix(firex + 4, firey + 4);
                    break;
                case LEFT:
                    firex -= 8;
                    pix = world.pc.ggetpix(firex, firey + 4) | world.pc.ggetpix(firex + 4, firey + 4);
                    break;
                case UP:
                    firey -= 7;
                    pix = (world.pc.ggetpix(firex + 4, firey) | world.pc.ggetpix(firex + 4, firey + 1) |
                            world.pc.ggetpix(firex + 4, firey + 2) | world.pc.ggetpix(firex + 4, firey + 3) |
                            world.pc.ggetpix(firex + 4, firey + 4) | world.pc.ggetpix(firex + 4, firey + 5) |
                            world.pc.ggetpix(firex + 4, firey + 6)) & 0xc0;
                    break;
                case DOWN:
                    firey += 7;
                    pix = (world.pc.ggetpix(firex, firey) | world.pc.ggetpix(firex, firey + 1) |
                            world.pc.ggetpix(firex, firey + 2) | world.pc.ggetpix(firex, firey + 3) |
                            world.pc.ggetpix(firex, firey + 4) | world.pc.ggetpix(firex, firey + 5) |
                            world.pc.ggetpix(firex, firey + 6)) & 3;
                    break;
            }

            int clbits = world.drawing.drawFire(firex, firey, 0);
            world.main.incPenalty();
            if ((clbits & 0x3f00) != 0)
                for (int mon = 0, b = 256; mon < 6; mon++, b <<= 1)
                    if ((clbits & b) != 0) {
                        world.monsters.killmon(mon);
                        world.scores.scorekill();
                        expsn = 1;
                    }
            if ((clbits & 0x40fe) != 0)
                expsn = 1;

            switch (firedir) {
                case RIGHT:
                    if (firex > 296)
                        expsn = 1;
                    else if (pix != 0 && clbits == 0) {
                        expsn = 1;
                        firex -= 8;
                        world.drawing.drawFire(firex, firey, 0);
                    }
                    break;
                case LEFT:
                    if (firex < 16)
                        expsn = 1;
                    else if (pix != 0 && clbits == 0) {
                        expsn = 1;
                        firex += 8;
                        world.drawing.drawFire(firex, firey, 0);
                    }
                    break;
                case UP:
                    if (firey < 15)
                        expsn = 1;
                    else if (pix != 0 && clbits == 0) {
                        expsn = 1;
                        firey += 7;
                        world.drawing.drawFire(firex, firey, 0);
                    }
                    break;
                case DOWN:
                    if (firey > 183)
                        expsn = 1;
                    else if (pix != 0 && clbits == 0) {
                        expsn = 1;
                        firey -= 7;
                        world.drawing.drawFire(firex, firey, 0);
                    }
                    break;
            }
        }
    }
}
