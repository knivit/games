package com.tsoft.game.utils;

public class ActionTimer {

    private final int interval;

    private long lastTime;

    public ActionTimer(int interval) {
        this.interval = interval;
    }

    public boolean action(long time) {
        if (time - lastTime >= interval) {
            lastTime = time;
            return true;
        }
        return false;
    }
}
