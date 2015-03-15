package com.tsoft.game.engine;

import java.io.Serializable;

public class ActionTimer implements Serializable {
    private boolean active = true;
    private long delay;
    private long currentTick;

    public ActionTimer(int delay) {
        this.delay = delay;
    }

    public boolean action(long elapsedTime) {
        if (active) {
            currentTick += elapsedTime;

            // time elapsed!
            if (currentTick >= delay) {
                // synch the current tick to make the next tick accurate
                currentTick -= delay;
                return true;
            }
        }

        return false;
    }
}
