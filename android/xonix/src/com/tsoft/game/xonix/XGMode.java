package com.tsoft.game.xonix;

import com.tsoft.game.InputController;

public interface XGMode {
    public void update(XGScreen screen, InputController inputController, long elapsedTime);

    public boolean isFinishGame();

    public XGMode nextMode();

    public String getLogString();
}
