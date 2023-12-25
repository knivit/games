package com.tsoft.game.games.loderunner.mode;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.tsoft.game.games.loderunner.actor.Robots;
import com.tsoft.game.utils.ActionTimer;
import com.tsoft.game.utils.GameMode;

import static com.tsoft.game.games.loderunner.LRGameState.*;

public class LRMenuMode implements GameMode {

    private Robots robots;
    private ActionTimer robotTimer;

    private LRMenuStatus status;
    private ActionTimer statusTimer;

    private GameMode nextMode;

    @Override
    public void init() {
        world.loadLevel(0);

        robots = new Robots();
        robotTimer = new ActionTimer(300);
        world.setRobots(robots);

        status = new LRMenuStatus();
        statusTimer = new ActionTimer(100);

        nextMode = null;
    }

    @Override
    public void update() {
        if (Gdx.input.isKeyPressed(Input.Keys.SPACE)) {
            nextMode = new LRPlayMode();
        }

        if (robotTimer.action(time)) {
            robots.move();
        }

        if (statusTimer.action(time)) {
            status.update();
        }
    }

    @Override
    public GameMode nextMode() {
        return nextMode;
    }
}