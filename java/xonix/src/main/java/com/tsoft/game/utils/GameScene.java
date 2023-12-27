package com.tsoft.game.utils;

public interface GameScene {

    void create();

    void render();

    GameScene next();
}
