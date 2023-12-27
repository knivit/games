package com.tsoft.game.games.xonix.misc;

import com.tsoft.game.utils.GameSound;

public class Sound extends GameSound {

    public static final String STEP = "step.ogg";
    public static final String REMOVE_LIFE = "remove_life.ogg";

    @Override
    public void create() {
        add(STEP);
        add(REMOVE_LIFE);
    }
}
