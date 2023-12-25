package com.tsoft.game.games.xonix;

import com.tsoft.game.games.xonix.actor.XGPlayer;
import com.tsoft.game.utils.GameController;
import com.tsoft.game.utils.GameMode;
import com.tsoft.game.utils.GameSound;

public final class XGGameState {

    public static long time;
    public static GameMode mode;
    public static GameSound sound;
    public static GameController controller;
    public static XGScreen screen;
    public static XGPlayer player;

    private XGGameState() { }
}
