package com.tsoft.game.games.xonix.scene;

import com.tsoft.game.games.xonix.misc.Screen;

import static com.tsoft.game.games.xonix.Xonix.global;

public class PlayStatus {

    public int level;
    public int score;
    public int life;

    public PlayStatus() {
        level = 1;
        life = 5;
    }

    public void update() {
        global.screen.print(0, 0, "LEVEL: %02d", level);
        global.screen.print((Screen.WIDTH - 8), 0, "LIFE: %02d", life);
        global.screen.print((Screen.WIDTH - 12) / 2, 0, "SCORE: %05d", score);
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
}
