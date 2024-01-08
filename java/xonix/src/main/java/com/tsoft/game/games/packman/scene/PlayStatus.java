package com.tsoft.game.games.packman.scene;

import com.tsoft.game.games.packman.misc.Screen;

import static com.tsoft.game.games.packman.Packman.global;

public class PlayStatus {

    public int level;
    public int score;
    public int life;

    public PlayStatus() {
        level = 1;
        life = 5;
        score = 0;

        update();
    }

    public void addScore(int val) {
        score += val;
        update();
    }

    public void removeLife() {
        life --;
        update();
    }

    public void nextLevel() {
        level ++;
        update();
    }

    private void update() {
        global.screen.print(0, 0, "LEVEL:%02d", level);
        global.screen.print((Screen.WIDTH - 10) / 2, 0, "SCORE:%05d", score);
        global.screen.print((Screen.WIDTH - 8), 0, "LIFE:%02d", life);
    }
}
