package com.tsoft.game.games.snake.mode;

import com.tsoft.game.utils.GameScene;

public class PlayScene implements GameScene {

    private GameScene nextMode;

    @Override
    public void create() {

    }

    @Override
    public void render() {

    }

    @Override
    public GameScene next() {
        return nextMode;
    }
}
