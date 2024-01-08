package com.tsoft.game.games.packman.scene;

import com.tsoft.game.games.packman.actor.Enemies;
import com.tsoft.game.games.packman.misc.Level;
import com.tsoft.game.games.packman.actor.Player;
import com.tsoft.game.utils.timer.ActionTimer;
import com.tsoft.game.utils.GameController;
import com.tsoft.game.utils.GameScene;

import static com.tsoft.game.games.packman.Packman.MENU_SCENE;
import static com.tsoft.game.games.packman.Packman.global;

public class PlayScene implements GameScene {

    private ActionTimer enemyTimer;

    private ActionTimer playerTimer;

    private String next;

    @Override
    public void create() {
        global.status = new PlayStatus();
        loadLevel();
        next = null;
    }

    @Override
    public void render() {
        GameController.State controller = global.controller.state(10);
        if (controller.escapePressed) {
            next = MENU_SCENE;
        }

        if (playerTimer.action(global.time)) {
            global.player.move(controller);
        }

        if (enemyTimer.action(global.time)) {
            global.enemies.move();
        }

        if (global.status.life < 0) {
            next = MENU_SCENE;
        }

        if (global.player.isResetLevel) {
            resetLevel();
        }

        if (global.player.isNextLevel) {
            global.status.nextLevel();
            loadLevel();
        }
    }

    @Override
    public String next() {
        return next;
    }

    private void loadLevel() {
        Level.load(global.status.level);

        global.player = new Player();
        global.player.create();
        playerTimer = new ActionTimer(100);

        global.enemies = new Enemies();
        global.enemies.create();
        enemyTimer = new ActionTimer(100);
    }

    private void resetLevel() {
        global.player.reset();
        global.enemies.reset();
    }
}

