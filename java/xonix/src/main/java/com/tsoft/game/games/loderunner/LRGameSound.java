package com.tsoft.game.games.loderunner;

import com.tsoft.game.utils.GameSound;

public class LRGameSound extends GameSound {

    public static final String STEP = "step.ogg";
    public static final String TREASURE = "treasure.ogg";
    public static final String REMOVE_LIFE = "remove_life.ogg";

    @Override
    public void init() {
        add(STEP);
        add(TREASURE);
        add(REMOVE_LIFE);
    }
}