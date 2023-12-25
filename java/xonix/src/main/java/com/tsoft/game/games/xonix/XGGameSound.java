package com.tsoft.game.games.xonix;

import com.tsoft.game.utils.GameSound;

public class XGGameSound extends GameSound {

    public static final String STEP = "step.ogg";
    public static final String REMOVE_LIFE = "remove_life.ogg";

    @Override
    public void init() {
        add(STEP);
        add(REMOVE_LIFE);
    }
}
