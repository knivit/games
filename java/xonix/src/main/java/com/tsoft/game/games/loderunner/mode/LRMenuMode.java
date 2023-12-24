package com.tsoft.game.games.loderunner.mode;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.tsoft.game.games.loderunner.LRGameState;
import com.tsoft.game.games.loderunner.actor.Robots;
import com.tsoft.game.utils.ActionTimer;
import com.tsoft.game.utils.GameMode;

import static com.tsoft.game.games.loderunner.LRGameState.time;

public class LRMenuMode implements GameMode {

    private Robots robots;
    private ActionTimer robotTimer;

    private boolean finished;
    private GameMode nextMode;

    @Override
    public void init() {
        LRGameState.world.loadLevel(0);

        robots = new Robots();
        robotTimer = new ActionTimer(300);
        LRGameState.world.setRobots(robots);

        nextMode = null;
    }

    @Override
    public void update() {
        if (Gdx.input.isKeyPressed(Input.Keys.ESCAPE)) {
            finished = true;
        }

        if (Gdx.input.isKeyPressed(Input.Keys.SPACE)) {
            nextMode = new LRPlayMode();
        }

        if (robotTimer.action(time)) {
            robots.move();
        }
    }

    @Override
    public boolean finished() {
        return finished;
    }

    @Override
    public GameMode nextMode() {
        return nextMode;
    }

    @Override
    public String getLogString() {
        StringBuilder buf = new StringBuilder(getClass().getName()).append(" {\n");
        buf.append(robots.getLogString()).append('\n');
        buf.append('}');
        return buf.toString();
    }
}
