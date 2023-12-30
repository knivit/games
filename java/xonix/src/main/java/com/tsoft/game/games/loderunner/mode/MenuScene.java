package com.tsoft.game.games.loderunner.mode;

import com.tsoft.game.games.loderunner.actor.Robots;
import com.tsoft.game.utils.ActionTimer;
import com.tsoft.game.utils.GameController;
import com.tsoft.game.utils.GameScene;

import static com.tsoft.game.games.loderunner.LodeRunner.PLAY_SCENE;
import static com.tsoft.game.games.loderunner.LodeRunner.global;

public class MenuScene implements GameScene {

    private Robots robots;
    private ActionTimer robotTimer;

    private MenuStatus status;
    private ActionTimer statusTimer;

    private String next;

    @Override
    public void create() {
        global.world.loadLevel(0);

        robots = new Robots();
        robotTimer = new ActionTimer(150);

        status = new MenuStatus();
        statusTimer = new ActionTimer(100);

        next = null;
    }

    @Override
    public void render() {
        GameController.State controller = global.controller.state(10);
        if (controller.firePressed) {
            next = PLAY_SCENE;
        }

        if (robotTimer.action(global.time)) {
            robots.move();
        }

        if (statusTimer.action(global.time)) {
            status.update();
        }
    }

    @Override
    public String next() {
        return next;
    }
}
