package com.tsoft.game.games.loderunner.mode;

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
    private LRPlayStatus status;

    private GameMode nextMode;

    @Override
    public void init() {
        resetLevel();

        status = new LRPlayStatus(world.getTreasureNumber());

        nextMode = null;
    }

    private void resetLevel() {
        world.loadLevel(status.getLevel());

        robots = new Robots();
        robotTimer = new ActionTimer(150);

        player = new LRPlayer(status);
        playerTimer = new ActionTimer(100);

        status.update();
    }

    @Override
    public void update() {
        if (controller.escapePressed) {
            nextMode = new LRMenuMode();
        }

        if (playerTimer.action(time)) {
            player.move();
        }

        if (robotTimer.action(time)) {
            robots.move();
        }

        status.update();

        if (status.getLife() < 0) {
            nextMode = new LRMenuMode();
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
