package com.tsoft.game.games.xonix.scene;

import com.tsoft.game.games.xonix.misc.Screen;
import com.tsoft.game.games.xonix.actor.FlyList;
import com.tsoft.game.games.xonix.actor.Player;
import com.tsoft.game.utils.ActionTimer;
import com.tsoft.game.utils.GameScene;

import static com.tsoft.game.games.xonix.Xonix.state;
import static com.tsoft.game.games.xonix.misc.Screen.*;

public class PlayScene implements GameScene {

    private FlyList innerFlyes;
    private FlyList outerFlyes;
    private ActionTimer flyTimer;

    private ActionTimer playerTimer;
    private PlayStatus status;

    private GameScene next;

    @Override
    public void create() {
        status = new PlayStatus();

        resetLevel();

        next = null;
    }

    @Override
    public void render() {
        if (state.controller.escapePressed) {
            next = new MenuScene();
        }

        if (playerTimer.action(state.time)) {
            state.player.move();
        }

        if (flyTimer.action(state.time)) {
            innerFlyes.move();
            outerFlyes.move();
        }

        status.update();

        if (status.getLife() < 0) {
            next = new MenuScene();
        }

        if (state.player.isNextLevel()) {
            status.nextLevel();
            resetLevel();
        }
    }

    @Override
    public GameScene next() {
        return next;
    }

    private void resetLevel() {
        ((Screen)state.screen).reset();

        state.player = new Player(status);
        playerTimer = new ActionTimer(100);

        innerFlyes = new FlyList();
        innerFlyes.create(INNER_FLY_CHAR, Screen.EMPTY_CHAR, status.getLevel());

        outerFlyes = new FlyList();
        outerFlyes.create(OUTER_FLY_CHAR, Screen.BORDER_CHAR, status.getLevel());

        flyTimer = new ActionTimer(100);
    }
}
