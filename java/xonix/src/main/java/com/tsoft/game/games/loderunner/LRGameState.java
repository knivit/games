package com.tsoft.game.games.loderunner;

import com.tsoft.game.utils.GameController;
import com.tsoft.game.utils.GameMode;
import com.tsoft.game.utils.GameSound;

public final class LRGameState {

    public static long time;
    public static GameMode mode;
    public static GameSound sound;
    public static GameController controller;
    public static LRScreen screen;
    public static LRWorld world;

    private LRGameState() { }
}
