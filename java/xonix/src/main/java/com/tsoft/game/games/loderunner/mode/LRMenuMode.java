package com.tsoft.game.games.loderunner.mode;

import com.tsoft.game.games.loderunner.actor.Robots;
import com.tsoft.game.utils.ActionTimer;
import com.tsoft.game.utils.GameScene;

import static com.tsoft.game.games.loderunner.LRGameState.*;

public class LRMenuMode implements GameScene {

    private Robots robots;
    private ActionTimer robotTimer;

    private LRMenuStatus status;
    private ActionTimer statusTimer;

    private GameScene next;

    @Override
    public void create() {
        world.loadLevel(0);

        robots = new Robots();
        robotTimer = new ActionTimer(300);
        world.setRobots(robots);

        status = new LRMenuStatus();
        statusTimer = new ActionTimer(100);

        next = null;
    }

    @Override
    public void render() {
        if (controller.firePressed) {
            next = new LRPlayMode();
        }

        if (robotTimer.action(time)) {
            robots.move();
        }

        if (statusTimer.action(time)) {
            status.update();
        }
    }

    @Override
    public GameScene next() {
        return next;
    }
}
