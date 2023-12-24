package com.tsoft.game.games.digger;

public class Monster {
    public static enum DeathPhase {
        PHASE_1, PHASE_4;
    }

    int x, y, h, v, xr, yr;
    Input.Dir dir;
    Input.Dir hdir;
    int t;
    int hnt;
    DeathPhase deathPhase;
    int bag;
    int dtime;
    int startTime;

    boolean flag;
    boolean isNobbin;
    boolean alive;

    public void createMonster() {
        flag = true;
        alive = true;
        t = 0;
        isNobbin = true;
        hnt = 0;
        h = 14;
        v = 0;
        x = 292;
        y = 18;
        xr = 0;
        yr = 0;
        dir = Input.Dir.LEFT;
        hdir = Input.Dir.LEFT;
        startTime = 5;
    }

    public void checkMonsterScared(int h) {
        if ((this.h == h) && (dir == Input.Dir.UP))
            dir = Input.Dir.DOWN;
    }
}
