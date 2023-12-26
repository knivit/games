package com.tsoft.game.games.snake.mode;

import com.tsoft.game.games.xonix.XGScreen;
import com.tsoft.game.games.xonix.actor.Flies;
import com.tsoft.game.games.xonix.mode.XGPlayMode;
import com.tsoft.game.utils.ActionTimer;
import com.tsoft.game.utils.GameMode;

import static com.tsoft.game.games.snake.SNGameState.*;

public class SNMenuMode implements GameMode {

    private ActionTimer cursorTimer;

    private GameMode nextMode;

    @Override
    public void init() {
        screen.showStartMenu();

        nextMode = null;
    }

    @Override
    public void update() {
        if (controller.firePressed) {
            nextMode = new SNPlayMode();
        }
    }

    @Override
    public GameMode nextMode() {
        return nextMode;
    }
}
