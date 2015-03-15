package com.tsoft.game.loderunner;

import com.tsoft.game.InputController;

public interface LRMode {
    public void update(LRWorld world, InputController inputController, long elapsedTime);

    public boolean isFinishGame();

    public LRMode nextMode();

    public String getLogString();
}
