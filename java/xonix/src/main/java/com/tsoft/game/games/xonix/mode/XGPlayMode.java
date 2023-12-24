package com.tsoft.game.games.xonix.mode;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.tsoft.game.games.xonix.XGGameStatus;
import com.tsoft.game.games.xonix.XGScreen;
import com.tsoft.game.games.xonix.actor.Flies;
import com.tsoft.game.games.xonix.actor.Fly;
import com.tsoft.game.games.xonix.actor.XGPlayer;
import com.tsoft.game.utils.ActionTimer;
import com.tsoft.game.utils.GameMode;

import static com.tsoft.game.games.xonix.XGGameState.*;

public class XGPlayMode implements GameMode {

    private Flies innerFlyes;
    private Flies outerFlyes;
    private ActionTimer flyTimer;

    private ActionTimer playerTimer;

    private GameMode nextMode;

    @Override
    public void init() {
        status = new XGGameStatus();

        resetLevel();

        nextMode = null;
    }

    private void resetLevel() {
        screen.reset();

        player = new XGPlayer();
        playerTimer = new ActionTimer(100);

        innerFlyes = new Flies();
        innerFlyes.createFlyes(Fly.INNER_FLY_CHAR, XGScreen.EMPTY_CHAR, status.getLevel());

        outerFlyes = new Flies();
        outerFlyes.createFlyes(Fly.OUTER_FLY_CHAR, XGScreen.BORDER_CHAR, status.getLevel());

        flyTimer = new ActionTimer(100);
    }

    @Override
    public void update() {
        if (Gdx.input.isKeyPressed(Input.Keys.ESCAPE)) {
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
    public boolean finished() {
        return false;
    }

    @Override
    public GameMode nextMode() {
        return nextMode;
    }

    @Override
    public String getLogString() {
        StringBuilder buf = new StringBuilder(getClass().getName()).append(" {\n");
        buf.append(player.getLogString()).append('\n');
        buf.append(innerFlyes.getLogString()).append('\n');
        buf.append(outerFlyes.getLogString()).append('\n');
        buf.append('}');
        return buf.toString();
    }
}
