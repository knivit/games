package com.tsoft.game.games.packman.misc;

import com.tsoft.game.utils.GameSound;

public class Sound extends GameSound {

    public static final String DOT_SOUND = "menu_selection_click.ogg";
    public static final String MAGIC_SOUND = "menu_action_click.ogg";
    public static final String REMOVE_LIFE_SOUND = "remove_life.ogg";

    @Override
    public void create() {
        put(DOT_SOUND);
        put(MAGIC_SOUND);
        put(REMOVE_LIFE_SOUND);
    }
}
