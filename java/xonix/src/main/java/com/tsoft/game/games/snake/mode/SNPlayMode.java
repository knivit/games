package com.tsoft.game.games.snake.mode;

import com.tsoft.game.utils.GameMode;

public class SNPlayMode implements GameMode {

    private GameMode nextMode;

    @Override
    public void init() {

    }

    @Override
    public void update() {

    }

    @Override
    public GameMode nextMode() {
        return nextMode;
    }
}
