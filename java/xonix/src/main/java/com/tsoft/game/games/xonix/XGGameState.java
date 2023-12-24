package com.tsoft.game.games.xonix;

import com.tsoft.game.games.xonix.actor.XGPlayer;
import com.tsoft.game.utils.GameMode;

public final class XGGameState {

    public static long time;
    public static GameMode mode;
    public static XGGameStatus status;
    public static XGScreen screen;
    public static XGPlayer player;

    private XGGameState() { }
}
