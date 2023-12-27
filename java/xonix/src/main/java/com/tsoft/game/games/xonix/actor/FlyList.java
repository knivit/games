package com.tsoft.game.games.xonix.actor;

import java.util.ArrayList;

public class FlyList extends ArrayList<Fly> {

    public FlyList() {
        super();
    }

    public void move() {
        for (Fly fly : this) {
            fly.move();
        }
    }

    public void create(char flyChar, char emptyChar, int level) {
        clear();

        for (int i = 0; i <= level; i ++) {
            add(Fly.getRandom(flyChar, emptyChar));
        }
    }
}
