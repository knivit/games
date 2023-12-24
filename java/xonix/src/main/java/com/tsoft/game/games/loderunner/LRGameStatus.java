package com.tsoft.game.games.loderunner;

public class LRGameStatus {

    private int level;
    private int score;
    private int life;

    public LRGameStatus() {
        level = 0;
        life = 5;
    }

    public void update() {
        LRGameState.screen.fill(0, 24, LRScreen.WIDTH, 24, ' ');
        LRGameState.screen.print(0, 24, "LEVEL: %02d", level);
        LRGameState.screen.print((LRScreen.WIDTH - 8), 24, "LIFE: %02d", life);
        LRGameState.screen.print((LRScreen.WIDTH - 12) / 2, 24, "SCORE: %05d", score);
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
