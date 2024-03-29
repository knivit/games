package com.tsoft.game.games.xonix.misc;

import com.tsoft.game.utils.GameSound;

public class Sound extends GameSound {

    public static final String STEP_SOUND = "step.ogg";
    public static final String REMOVE_LIFE_SOUND = "remove_life.ogg";

    @Override
    public void create() {
        put(STEP_SOUND);
        put(REMOVE_LIFE_SOUND);
    }
}
