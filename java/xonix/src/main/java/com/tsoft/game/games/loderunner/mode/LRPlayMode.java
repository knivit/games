package com.tsoft.game.games.loderunner.mode;

import com.tsoft.game.games.loderunner.actor.LRPlayer;
import com.tsoft.game.games.loderunner.actor.Robots;
import com.tsoft.game.utils.ActionTimer;
import com.tsoft.game.utils.GameScene;

import static com.tsoft.game.games.loderunner.LRGameState.*;

public class LRPlayMode implements GameScene {

    private Robots robots;
    private ActionTimer robotTimer;

    private LRPlayer player;
    private ActionTimer playerTimer;
    private LRPlayStatus status;

    private GameScene next;

    @Override
    public void create() {
        status = new LRPlayStatus();

        resetLevel();

        next = null;
    }

    private void resetLevel() {
        world.loadLevel(status.level);

        robots = new Robots();
        robotTimer = new ActionTimer(150);

        player = new LRPlayer(status);
        playerTimer = new ActionTimer(100);

        status.treasureLeft = world.getTreasureNumber();
        status.update();
    }

    @Override
    public void render() {
        if (controller.escapePressed) {
            next = new LRMenuMode();
        }

        if (playerTimer.action(time)) {
            player.move();
        }

        if (robotTimer.action(time)) {
            robots.move();
        }

        status.update();

        if (status.life < 0) {
            next = new LRMenuMode();
        }

        if (player.isNextLevel()) {
            status.nextLevel();
            resetLevel();
        }
    }

    @Override
    public GameScene next() {
        return next;
    }
}
