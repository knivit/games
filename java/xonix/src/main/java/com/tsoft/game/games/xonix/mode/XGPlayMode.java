package com.tsoft.game.games.xonix.mode;

import com.tsoft.game.games.xonix.XGScreen;
import com.tsoft.game.games.xonix.actor.Flies;
import com.tsoft.game.games.xonix.actor.Fly;
import com.tsoft.game.games.xonix.actor.XGPlayer;
import com.tsoft.game.utils.ActionTimer;
import com.tsoft.game.utils.GameMode;

import static com.tsoft.game.games.xonix.XGGameState.*;
import static com.tsoft.game.games.xonix.XGScreen.INNER_FLY_CHAR;
import static com.tsoft.game.games.xonix.XGScreen.OUTER_FLY_CHAR;

public class XGPlayMode implements GameMode {

    private Flies innerFlyes;
    private Flies outerFlyes;
    private ActionTimer flyTimer;

    private ActionTimer playerTimer;
    private XGPlayStatus status;

    private GameMode nextMode;

    @Override
    public void init() {
        status = new XGPlayStatus();

        resetLevel();

        nextMode = null;
    }

    private void resetLevel() {
        screen.reset();

        player = new XGPlayer(status);
        playerTimer = new ActionTimer(100);

        innerFlyes = new Flies();
        innerFlyes.createFlyes(INNER_FLY_CHAR, XGScreen.EMPTY_CHAR, status.getLevel());

        outerFlyes = new Flies();
        outerFlyes.createFlyes(OUTER_FLY_CHAR, XGScreen.BORDER_CHAR, status.getLevel());

        flyTimer = new ActionTimer(100);
    }

    @Override
    public void update() {
        if (controller.escapePressed) {
            nextMode = new XGMenuMode();
        }

        if (playerTimer.action(time)) {
            player.move();
        }

        if (flyTimer.action(time)) {
            innerFlyes.move();
            outerFlyes.move();
        }

        status.update();

        if (status.getLife() < 0) {
            nextMode = new XGMenuMode();
        }

        if (player.isNextLevel()) {
            status.nextLevel();
            resetLevel();
        }
    }

    @Override
    public GameMode nextMode() {
        return nextMode;
    }
}
