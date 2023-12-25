package com.tsoft.game.utils;

public interface GameMode {

    void init();

    void update();

    GameMode nextMode();
}
