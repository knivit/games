package com.tsoft.game.xonix;

import java.util.ArrayList;

public class Flies extends ArrayList<Fly> {
    private XGScreen screen;
    private XGPlayer player;

    public Flies(XGScreen screen, XGPlayer player) {
        super();

        this.screen = screen;
        this.player = player;
    }

    public void move() {
        for (Fly fly : this) {
            fly.move();
        }
    }

    public void createFlyes(char flyChar, char emptyChar, int level) {
        clear();
        for (int i = 0; i <= level; i ++) {
            add(Fly.getRandom(screen, player, flyChar, emptyChar));
        }
    }

    public String getLogString() {
        int no = 0;
        StringBuilder buf = new StringBuilder("Flies {\n");
        for (Fly fly : this) {
            buf.append(++ no).append(". ").append(fly.toLogString()).append('\n');
        }
        buf.append('}');
        return buf.toString();
    }
}
