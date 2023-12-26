package com.tsoft.game.games.snake;

import com.tsoft.game.utils.GameController;
import com.tsoft.game.utils.GameMode;
import com.tsoft.game.utils.GameSound;

public final class SNGameState {

    public static long time;
    public static GameMode mode;
    public static GameSound sound;
    public static GameController controller;
    public static SNScreen screen;

    private SNGameState() { }
}
