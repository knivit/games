package com.tsoft.game.games.loderunner.mode;

import com.tsoft.game.games.loderunner.LRScreen;

import static com.tsoft.game.games.loderunner.LRGameState.screen;

public class LRPlayStatus {

    private int level;
    private int treasureLeft;
    private int life;

    public LRPlayStatus(int treasureLeft) {
        this.treasureLeft = treasureLeft;
        level = 0;
        life = 5;
    }

    public void update() {
        screen.fill(0, 0, LRScreen.WIDTH, 1, ' ');
        screen.print(0, 0, "LEVEL: %02d", level);
        screen.print((LRScreen.WIDTH - 8), 0, "LIFE: %02d", life);
        screen.print((LRScreen.WIDTH - 12) / 2, 0, "LEFT: %05d", treasureLeft);
    }

    public int getLevel() {
        return level;
    }

    public void nextLevel() {
        life ++;
        level ++;
    }

    public int getTreasureLeft() {
        return treasureLeft;
    }

    public void treasureFound() {
        treasureLeft --;
    }

    public void removeLife() {
        life --;
    }

    public int getLife() {
        return life;
    }
}
