package com.tsoft.game.digger;

public class Monsters {
    private World world;

    private Monster[] mondat = {new Monster(), new Monster(), new Monster(), new Monster(), new Monster(), new Monster()};    // [6]

    private int nextmonster = 0;
    private int totalmonsters = 0;
    private int maxmononscr = 0;
    private int nextmontime = 0;
    private int mongaptime = 0;

    private boolean unbonusflag = false;
    private boolean mongotgold = false;

    public Monsters(World world) {
        this.world = world;
    }

    private void checkCoincide(int mon, int bits) {
        for (int i = 0, b = 256; i < 6; i++, b <<= 1)
            if (((bits & b) != 0) && (mondat[mon].dir == mondat[i].dir) && (mondat[i].startTime == 0) && (mondat[mon].startTime == 0))
                mondat[i].dir = mondat[i].dir.getReverse();
    }

    public void checkMonsterScared(int h) {
        for (int i = 0; i < 6; i++) {
            mondat[i].checkMonsterScared(h);
        }
    }

    private void createMonster() {
        for (int i = 0; i < 6; i++)
            if (!mondat[i].flag) {
                mondat[i].createMonster();

                nextmonster++;
                nextmontime = mongaptime;
                world.sprite.movedrawspr(i + 8, mondat[i].x, mondat[i].y);
                break;
            }
    }

    public void doMonsters() {
        if (nextmontime > 0)
            nextmontime--;
        else {
            if (nextmonster < totalmonsters && getNumberOfMonstersOnScreen() < maxmononscr && world.digger.digonscr &&
                    !world.digger.bonusmode)
                createMonster();

            if (unbonusflag && nextmonster == totalmonsters && nextmontime == 0)
                if (world.digger.digonscr) {
                    unbonusflag = false;
                    world.digger.createBonus();
                }
        }

        for (int i = 0; i < 6; i++)
            if (mondat[i].flag) {
                if (mondat[i].hnt > 10 - world.main.levof10()) {
                    if (mondat[i].isNobbin) {
                        mondat[i].isNobbin = false;
                        mondat[i].hnt = 0;
                    }
                }

                if (mondat[i].alive)
                    if (mondat[i].t == 0) {
                        monsterAI(i);
                        if (world.main.randno(15 - world.main.levof10()) == 0 && mondat[i].isNobbin)
                            monsterAI(i);
                    } else
                        mondat[i].t--;
                else
                    monsterDie(i);
            }
    }

    public void eraseMonsters() {
        for (int i = 0; i < 6; i++)
            if (mondat[i].flag)
                world.sprite.erasespr(i + 8);
    }

    public void incMonsters(int n) {
        if (n > 6)
            n = 6;
        for (int m = 1; m < n; m++)
            mondat[m].t++;
    }

    private void incPenalties(int bits) {
        for (int m = 0, b = 256; m < 6; m++, b <<= 1) {
            if ((bits & b) != 0)
                world.main.incPenalty();
            b <<= 1;
        }
    }

    public void initMonsters() {
        for (int i = 0; i < 6; i++)
            mondat[i].flag = false;

        nextmonster = 0;
        mongaptime = 45 - (world.main.levof10() << 1);
        totalmonsters = world.main.levof10() + 5;

        switch (world.main.levof10()) {
            case 1:
                maxmononscr = 3;
                break;
            case 2:
            case 3:
            case 4:
            case 5:
            case 6:
            case 7:
                maxmononscr = 4;
                break;
            case 8:
            case 9:
            case 10:
                maxmononscr = 5;
                break;
        }
        nextmontime = 10;
        unbonusflag = true;
    }

    public void killmon(int mon) {
        if (mondat[mon].flag) {
            mondat[mon].flag = mondat[mon].alive = false;
            world.sprite.erasespr(mon + 8);
            if (world.digger.bonusmode)
                totalmonsters++;
        }
    }

    public int killMonsters(int bits) {
        int n = 0;
        for (int m = 0, b = 256; m < 6; m++, b <<= 1)
            if ((bits & b) != 0) {
                killmon(m);
                n++;
            }
        return n;
    }

    private void monsterAI(int mon) {
        int clbits;
        Input.Dir dir;
        Input.Dir mdirp1, mdirp2, mdirp3, mdirp4;
        int monox = mondat[mon].x;
        int monoy = mondat[mon].y;
        if (mondat[mon].xr == 0 && mondat[mon].yr == 0) {
            /* If we are here the monster needs to know which way to turn next. */
            /* Turn hobbin back into nobbin if it's had its time */
            if (mondat[mon].hnt > 30 + (world.main.levof10() << 1))
                if (!mondat[mon].isNobbin) {
                    mondat[mon].hnt = 0;
                    mondat[mon].isNobbin = true;
                }

            /* Set up monster direction properties to chase dig */
            int dy = world.digger.diggery - mondat[mon].y;
            int dx = world.digger.diggerx - mondat[mon].x;
            if (Math.abs(dy) > Math.abs(dx)) {
                if (world.digger.diggery < mondat[mon].y) {
                    mdirp1 = Input.Dir.UP;
                    mdirp4 = Input.Dir.DOWN;
                } else {
                    mdirp1 = Input.Dir.DOWN;
                    mdirp4 = Input.Dir.UP;
                }
                if (world.digger.diggerx < mondat[mon].x) {
                    mdirp2 = Input.Dir.LEFT;
                    mdirp3 = Input.Dir.RIGHT;
                } else {
                    mdirp2 = Input.Dir.RIGHT;
                    mdirp3 = Input.Dir.LEFT;
                }
            } else {
                if (world.digger.diggerx < mondat[mon].x) {
                    mdirp1 = Input.Dir.LEFT;
                    mdirp4 = Input.Dir.RIGHT;
                } else {
                    mdirp1 = Input.Dir.RIGHT;
                    mdirp4 = Input.Dir.LEFT;
                }
                if (world.digger.diggery < mondat[mon].y) {
                    mdirp2 = Input.Dir.UP;
                    mdirp3 = Input.Dir.DOWN;
                } else {
                    mdirp2 = Input.Dir.DOWN;
                    mdirp3 = Input.Dir.UP;
                }
            }

            /* In bonus mode, run away from digger */
            if (world.digger.bonusmode) {
                Input.Dir t = mdirp1;
                mdirp1 = mdirp4;
                mdirp4 = t;
                t = mdirp2;
                mdirp2 = mdirp3;
                mdirp3 = t;
            }

            /* Adjust priorities so that monsters don't reverse direction unless they really have to */
            dir = mondat[mon].dir.getReverse();
            if (dir == mdirp1) {
                mdirp1 = mdirp2;
                mdirp2 = mdirp3;
                mdirp3 = mdirp4;
                mdirp4 = dir;
            }
            if (dir == mdirp2) {
                mdirp2 = mdirp3;
                mdirp3 = mdirp4;
                mdirp4 = dir;
            }
            if (dir == mdirp3) {
                mdirp3 = mdirp4;
                mdirp4 = dir;
            }

            /* Introduce a randno element on levels <6 : occasionally swap p1 and p3 */
            if (world.main.randno(world.main.levof10() + 5) == 1 && world.main.levof10() < 6) {
                Input.Dir t = mdirp1;
                mdirp1 = mdirp3;
                mdirp3 = t;
            }

            /* Check field and find direction */
            if (world.drawing.isFieldClear(mdirp1, mondat[mon].h, mondat[mon].v))
                dir = mdirp1;
            else if (world.drawing.isFieldClear(mdirp2, mondat[mon].h, mondat[mon].v))
                dir = mdirp2;
            else if (world.drawing.isFieldClear(mdirp3, mondat[mon].h, mondat[mon].v))
                dir = mdirp3;
            else if (world.drawing.isFieldClear(mdirp4, mondat[mon].h, mondat[mon].v))
                dir = mdirp4;

            /* Hobbins don't care about the field: they go where they want. */
            if (!mondat[mon].isNobbin)
                dir = mdirp1;

            /* Monsters take a time penalty for changing direction */
            if (mondat[mon].dir != dir)
                mondat[mon].t++;

            /* Save the new direction */
            mondat[mon].dir = dir;
        }

        /* If monster is about to go off edge of screen, stop it. */
        if ((mondat[mon].x == 292 && mondat[mon].dir == Input.Dir.RIGHT) ||
                (mondat[mon].x == 12 && mondat[mon].dir == Input.Dir.LEFT) ||
                (mondat[mon].y == 180 && mondat[mon].dir == Input.Dir.DOWN) ||
                (mondat[mon].y == 18 && mondat[mon].dir == Input.Dir.UP))
            mondat[mon].dir = Input.Dir.NONE;

        /* Change hdir for hobbin */
        if (mondat[mon].dir == Input.Dir.LEFT || mondat[mon].dir == Input.Dir.RIGHT)
            mondat[mon].hdir = mondat[mon].dir;

        /* Hobbins digger */
        if (!mondat[mon].isNobbin)
            world.drawing.eatField(mondat[mon].x, mondat[mon].y, mondat[mon].dir);

        /* (Draw new tunnels) and move monster */
        switch (mondat[mon].dir) {
            case RIGHT:
                if (!mondat[mon].isNobbin)
                    world.drawing.drawRightBlob(mondat[mon].x, mondat[mon].y);
                mondat[mon].x += 4;
                break;
            case LEFT:
                if (!mondat[mon].isNobbin)
                    world.drawing.drawLeftBlob(mondat[mon].x, mondat[mon].y);
                mondat[mon].x -= 4;
                break;
            case UP:
                if (!mondat[mon].isNobbin)
                    world.drawing.drawTopBlob(mondat[mon].x, mondat[mon].y);
                mondat[mon].y -= 3;
                break;
            case DOWN:
                if (!mondat[mon].isNobbin)
                    world.drawing.drawBottomBlob(mondat[mon].x, mondat[mon].y);
                mondat[mon].y += 3;
                break;
        }

        /* Hobbins can eat emeralds */
        if (!mondat[mon].isNobbin)
            world.digger.hitEmerald((mondat[mon].x - 12) / 20, (mondat[mon].y - 18) / 18, (mondat[mon].x - 12) % 20, (mondat[mon].y - 18) % 18, mondat[mon].dir);

        /* If digger's gone, don't bother */
         if (!world.digger.digonscr) {
            mondat[mon].x = monox;
            mondat[mon].y = monoy;
        }

        /* If monster's just started, don't move yet */
        if (mondat[mon].startTime != 0) {
            mondat[mon].startTime--;
            mondat[mon].x = monox;
            mondat[mon].y = monoy;
        }

        /* Increase time counter for hobbin */
        if (!mondat[mon].isNobbin && mondat[mon].hnt < 100)
            mondat[mon].hnt++;

        /* Draw monster */
        clbits = world.drawing.drawMonster(mon, mondat[mon].isNobbin, mondat[mon].hdir, mondat[mon].x, mondat[mon].y);
        world.main.incPenalty();

        /* Collision with another monster */
        if ((clbits & 0x3f00) != 0) {
            mondat[mon].t++; /* Time penalty */
            checkCoincide(mon, clbits); /* Ensure both aren't moving in the same dir. */
            incPenalties(clbits);
        }

        /* Check for collision with bag */
        boolean push = true;
        if ((clbits & world.bags.bagBits()) != 0) {
            mondat[mon].t++; /* Time penalty */
            mongotgold = false;
            if (mondat[mon].dir == Input.Dir.LEFT || mondat[mon].dir == Input.Dir.RIGHT) { /* Horizontal push */
                push = world.bags.pushBags(mondat[mon].dir, clbits);
                mondat[mon].t++; /* Time penalty */
            } else if (!world.bags.pushudbags(clbits)) /* Vertical push */
                push = false;

            if (mongotgold) /* No time penalty if monster eats gold */
                mondat[mon].t = 0;

            if (!mondat[mon].isNobbin && mondat[mon].hnt > 1)
                world.bags.removeBags(clbits); /* Hobbins eat bags */
        }

        /* Increase hobbin cross counter */
        if (mondat[mon].isNobbin && ((clbits & 0x3f00) != 0) && world.digger.digonscr)
            mondat[mon].hnt++;

        /* See if bags push monster back */
        if (!push) {
            mondat[mon].x = monox;
            mondat[mon].y = monoy;
            world.drawing.drawMonster(mon, mondat[mon].isNobbin, mondat[mon].hdir, mondat[mon].x, mondat[mon].y);
            world.main.incPenalty();
            if (mondat[mon].isNobbin) /* The other way to create hobbin: stuck on x-bag */
                mondat[mon].hnt++;
            if ((mondat[mon].dir == Input.Dir.UP || mondat[mon].dir == Input.Dir.DOWN) && mondat[mon].isNobbin)
                mondat[mon].dir = mondat[mon].dir.getReverse(); /* If vertical, give up */
        }

        /* Collision with digger */
        if (((clbits & 1) != 0) && world.digger.digonscr)
            if (world.digger.bonusmode) {
                killmon(mon);
                world.scores.scoreeatm();
                world.sound.soundeatm(); /* Collision in bonus mode */
            } else
                world.digger.killDigger(3, 0); /* Kill digger */

        /* Update co-ordinates */
        mondat[mon].h = (mondat[mon].x - 12) / 20;
        mondat[mon].v = (mondat[mon].y - 18) / 18;
        mondat[mon].xr = (mondat[mon].x - 12) % 20;
        mondat[mon].yr = (mondat[mon].y - 18) % 18;
    }

    private void monsterDie(int mon) {
        switch (mondat[mon].deathPhase) {
            case PHASE_1:
                if (world.bags.getBagY(mondat[mon].bag) + 6 > mondat[mon].y)
                    mondat[mon].y = world.bags.getBagY(mondat[mon].bag);
                world.drawing.drawMonsterDie(mon, mondat[mon].isNobbin, mondat[mon].hdir, mondat[mon].x, mondat[mon].y);
                world.main.incPenalty();
                if (world.bags.getBagDir(mondat[mon].bag) == Input.Dir.NONE) {
                    mondat[mon].dtime = 1;
                    mondat[mon].deathPhase = Monster.DeathPhase.PHASE_4;
                }
                break;

            case PHASE_4:
                if (mondat[mon].dtime != 0)
                    mondat[mon].dtime--;
                else {
                    killmon(mon);
                    world.scores.scorekill();
                }
                break;
        }
    }

    public void mongold() {
        mongotgold = true;
    }

    public int monleft() {
        return getNumberOfMonstersOnScreen() + totalmonsters - nextmonster;
    }

    private int getNumberOfMonstersOnScreen() {
        int n = 0;
        for (int i = 0; i < 6; i++)
            if (mondat[i].flag)
                n++;
        return n;
    }

    private void squashMonster(int mon, Monster.DeathPhase death, int bag) {
        mondat[mon].alive = false;
        mondat[mon].deathPhase = death;
        mondat[mon].bag = bag;
    }

    public void squashMonsters(int bag, int bits) {
        for (int m = 0, b = 256; m < 6; m++, b <<= 1) {
            if ((bits & b) != 0) {
                if (mondat[m].y >= world.bags.getBagY(bag))
                    squashMonster(m, Monster.DeathPhase.PHASE_1, bag);
            }
        }
    }
}
