package com.tsoft.game.games.loderunner.mode;

import com.tsoft.game.games.loderunner.misc.Screen;

import static com.tsoft.game.games.loderunner.LodeRunner.state;

public class PlayStatus {

    public int level;
    public int treasureLeft;
    public int life;

    public PlayStatus() {
        level = 0;
        life = 5;
    }

    public void update() {
        state.screen.fill(0, 0, Screen.WIDTH, 1, ' ');
        state.screen.print(0, 0, "LEVEL: %02d", level);
        state.screen.print((Screen.WIDTH - 8), 0, "LIFE: %02d", life);
        state.screen.print((Screen.WIDTH - 12) / 2, 0, "LEFT: %05d", treasureLeft);
    }

    public void nextLevel() {
        life ++;
        level ++;
    }

    public void treasureFound() {
        treasureLeft --;
    }

    public void removeLife() {
        life --;
    }
}
