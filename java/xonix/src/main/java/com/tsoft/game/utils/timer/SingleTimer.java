package com.tsoft.game.utils.timer;

public class SingleTimer {

    private int period;

    private boolean active;
    private long lastTime;

    public void start(long time, int period) {
        this.period = period;
        lastTime = time;
        active = true;
    }

    public void stop() {
        active = false;
    }

    public boolean isActive(long time) {
        if (active && (time - lastTime >= period)) {
            stop();
        }

        return active;
    }
}
