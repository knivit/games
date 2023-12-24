package com.tsoft.game.utils;

public interface GameMode {

    void init();

    void update();

    boolean finished();

    GameMode nextMode();

    String getLogString();
}
