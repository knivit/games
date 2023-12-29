package com.tsoft.game.games.loderunner.mode;

import com.tsoft.game.games.loderunner.actor.Player;
import com.tsoft.game.games.loderunner.actor.Robots;
import com.tsoft.game.utils.ActionTimer;
import com.tsoft.game.utils.GameController;
import com.tsoft.game.utils.GameScene;

import static com.tsoft.game.games.loderunner.LodeRunner.*;

public class PlayScene implements GameScene {

    private Robots robots;
    private ActionTimer robotTimer;

    private Player player;
    private ActionTimer playerTimer;
    private PlayStatus status;

    private String next;

    @Override
    public void create() {
        status = new PlayStatus();

        resetLevel();

        next = null;
    }

    private void resetLevel() {
        state.world.loadLevel(status.level);

        robots = new Robots();
        robotTimer = new ActionTimer(150);

        player = new Player(status);
        playerTimer = new ActionTimer(100);

        status.treasureLeft = state.world.getTreasureNumber();
        status.update();
    }

    @Override
    public void render() {
        GameController.State controller = state.controller.state();
        if (controller.escapePressed) {
            next = MENU_SCENE;
        }

        if (playerTimer.action(state.time)) {
            player.move(controller);
        }

        if (robotTimer.action(state.time)) {
            robots.move();
        }

        status.update();

        if (status.life < 0) {
            next = MENU_SCENE;
        }

        if (player.isNextLevel()) {
            status.nextLevel();
            resetLevel();
        }
    }

    @Override
    public String next() {
        return next;
    }
}
