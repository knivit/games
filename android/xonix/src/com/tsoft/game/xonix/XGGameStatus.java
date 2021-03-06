package com.tsoft.game.xonix;

public class XGGameStatus {
    private XGScreen screen;

    private int level;
    private int score;
    private int life;

    public XGGameStatus(XGScreen screen) {
        this.screen = screen;

        level = 1;
        life = 5;
    }

    public void update() {
        screen.print(0, 0, "LEVEL: %02d", level);
        screen.print((XGScreen.WIDTH - 8), 0, "LIFE: %02d", life);
        screen.print((XGScreen.WIDTH - 12) / 2, 0, "SCORE: %05d", score);
    }

    public int getLevel() {
        return level;
    }

    public void nextLevel() {
        life ++;
        level ++;
    }

    public void addScore(int score) {
        this.score += score;
    }

    public void removeLife() {
        life --;
    }

    public int getLife() {
        return life;
    }
}
