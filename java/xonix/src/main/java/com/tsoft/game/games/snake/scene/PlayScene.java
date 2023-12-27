package com.tsoft.game.games.snake.scene;

import com.tsoft.game.games.snake.actor.Level;
import com.tsoft.game.games.snake.actor.Mouse;
import com.tsoft.game.games.snake.actor.Player;
import com.tsoft.game.utils.ActionTimer;
import com.tsoft.game.utils.GameScene;

import static com.tsoft.game.games.snake.Snake.MENU_SCENE;
import static com.tsoft.game.games.snake.Snake.state;

public class PlayScene implements GameScene {

    private Player player;
    private ActionTimer playerTimer;

    private Mouse mouse;
    private ActionTimer mouseTimer;

    private PlayStatus status;
    private String next;

    private int level;

    @Override
    public void create() {
        status = new PlayStatus();

        level = 0;
        resetLevel();

        next = null;
    }

    @Override
    public void render() {
        if (state.controller.escapePressed) {
            next = MENU_SCENE;
        }

        if (playerTimer.action(state.time)) {
            player.move();
        }

        if (mouseTimer.action(state.time)) {
            mouse.move();
        }

        status.update();

        if (status.getLife() < 0) {
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

    private void resetLevel() {
        Level.load(level);

        player = new Player(status);
        player.create(level);
        playerTimer = new ActionTimer(200);

        mouse = new Mouse();
        mouseTimer = new ActionTimer(10_000);
    }
}
