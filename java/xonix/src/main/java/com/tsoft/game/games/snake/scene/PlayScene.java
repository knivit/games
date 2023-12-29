package com.tsoft.game.games.snake.scene;

import com.tsoft.game.games.snake.actor.Level;
import com.tsoft.game.games.snake.actor.Mouse;
import com.tsoft.game.games.snake.actor.Player;
import com.tsoft.game.utils.ActionTimer;
import com.tsoft.game.utils.GameController;
import com.tsoft.game.utils.GameScene;

import static com.tsoft.game.games.snake.Snake.MENU_SCENE;
import static com.tsoft.game.games.snake.Snake.state;

public class PlayScene implements GameScene {

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

    @Override
    public void render() {
        GameController.State controller = state.controller.state();
        if (controller.escapePressed) {
            next = MENU_SCENE;
        }

        if (playerTimer.action(state.time)) {
            player.move(controller);
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

    private void resetLevel() {
        Level.load(status.level);

        player = new Player(status);
        player.create(status.level);
        playerTimer = new ActionTimer(200 - state.speed * 10 - status.level * 4);

        new Mouse().appear();
    }
}
