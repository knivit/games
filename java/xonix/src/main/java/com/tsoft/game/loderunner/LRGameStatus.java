package com.tsoft.game.loderunner;

public class LRGameStatus {
    private LRScreen screen;

    private int level;
    private int score;
    private int life;

    public LRGameStatus(LRScreen screen) {
        this.screen = screen;

        level = 0;
        life = 5;
    }

    public void update() {
        screen.fill(0, 24, LRScreen.WIDTH, 24, ' ');
        screen.print(0, 24, "LEVEL: %02d", level);
        screen.print((LRScreen.WIDTH - 8), 24, "LIFE: %02d", life);
        screen.print((LRScreen.WIDTH - 12) / 2, 24, "SCORE: %05d", score);
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
