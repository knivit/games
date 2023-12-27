package com.tsoft.game.games.loderunner;

import com.tsoft.game.utils.GameController;
import com.tsoft.game.utils.GameScene;
import com.tsoft.game.utils.GameSound;

public final class LRGameState {

    public static long time;
    public static GameScene mode;
    public static GameSound sound;
    public static GameController controller;
    public static LRScreen screen;
    public static LRWorld world;

    private LRGameState() { }
}
