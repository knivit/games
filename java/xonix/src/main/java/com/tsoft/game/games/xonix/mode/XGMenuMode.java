package com.tsoft.game.games.xonix.mode;

import com.tsoft.game.games.xonix.actor.Flies;
import com.tsoft.game.games.xonix.actor.Fly;
import com.tsoft.game.games.xonix.XGScreen;
import com.tsoft.game.utils.ActionTimer;
import com.tsoft.game.utils.GameMode;

import static com.tsoft.game.games.xonix.XGGameState.*;
import static com.tsoft.game.games.xonix.XGScreen.INNER_FLY_CHAR;

public class XGMenuMode implements GameMode {

    private Flies innerFlyes;
    private ActionTimer flyTimer;

    private GameMode nextMode;

    @Override
    public void init() {
        screen.showStartMenu();

        innerFlyes = new Flies();
        innerFlyes.createFlyes(INNER_FLY_CHAR, XGScreen.EMPTY_CHAR, 2);
        flyTimer = new ActionTimer(100);

        nextMode = null;
    }

    @Override
    public void update() {
        if (controller.firePressed) {
            nextMode = new XGPlayMode();
        }

        if (flyTimer.action(time)) {
            innerFlyes.move();
        }
    }

    @Override
    public GameMode nextMode() {
        return nextMode;
    }
}
