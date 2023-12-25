package com.tsoft.game.games.loderunner.mode;

import com.tsoft.game.games.loderunner.LRScreen;

import static com.tsoft.game.games.loderunner.LRGameState.screen;

public class LRPlayStatus {

    private int level;
    private int score;
    private int life;

    public LRPlayStatus() {
        level = 0;
        life = 5;
    }

    public void update() {
        screen.fill(0, 0, LRScreen.WIDTH, 1, ' ');
        screen.print(0, 0, "LEVEL: %02d", level);
        screen.print((LRScreen.WIDTH - 8), 0, "LIFE: %02d", life);
        screen.print((LRScreen.WIDTH - 12) / 2, 0, "SCORE: %05d", score);
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
