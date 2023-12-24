package com.tsoft.game.games.loderunner.mode;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.tsoft.game.games.loderunner.LRGameState;
import com.tsoft.game.games.loderunner.LRGameStatus;
import com.tsoft.game.games.loderunner.actor.LRPlayer;
import com.tsoft.game.games.loderunner.actor.Robots;
import com.tsoft.game.utils.ActionTimer;
import com.tsoft.game.utils.GameMode;

import static com.tsoft.game.games.loderunner.LRGameState.*;

public class LRPlayMode implements GameMode {

    private Robots robots;
    private ActionTimer robotTimer;

    private LRPlayer player;
    private ActionTimer playerTimer;

    private GameMode nextMode;

    @Override
    public void init() {
        LRGameState.status = new LRGameStatus();

        resetLevel();

        nextMode = null;
    }

    private void resetLevel() {
        world.loadLevel(status.getLevel());

        robots = new Robots();
        robotTimer = new ActionTimer(300);

        player = new LRPlayer();
        playerTimer = new ActionTimer(300);
    }

    @Override
    public void update() {
        if (Gdx.input.isKeyPressed(Input.Keys.ESCAPE)) {
            nextMode = new LRMenuMode();
        }

        if (playerTimer.action(time)) {
            player.move();
        }

        if (robotTimer.action(time)) {
            robots.move();
        }

        LRGameState.status.update();

        if (LRGameState.status.getLife() < 0) {
            nextMode = new LRMenuMode();
        }

        if (player.isNextLevel()) {
            LRGameState.status.nextLevel();
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
        buf.append(robots.getLogString()).append('\n');
        buf.append(player.getLogString()).append('\n');
        buf.append('}');
        return buf.toString();
    }
}
