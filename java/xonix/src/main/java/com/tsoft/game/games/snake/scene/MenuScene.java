package com.tsoft.game.games.snake.scene;

import com.tsoft.game.games.snake.misc.Screen;
import com.tsoft.game.utils.GameScene;
import com.tsoft.game.utils.GameMenu;

import java.util.function.Consumer;

import static com.tsoft.game.games.snake.Snake.PLAY_SCENE;
import static com.tsoft.game.games.snake.Snake.global;

public class MenuScene implements GameScene {

    private GameMenu menu;

    private String next;

    @Override
    public void create() {
        ((Screen) global.screen).showStartMenu();

        menu = new GameMenu();
        menu.create(global,
            new GameMenu.Item(36, 8, 5, new ChangeSpeedAction()),
            new GameMenu.Item(36, 7, 5, new StartAction()));

        next = null;
    }

    @Override
    public void render() {
        menu.render(global);
    }

    @Override
    public String next() {
        return next;
    }

    static class ChangeSpeedAction implements Consumer<GameMenu.Action> {

        @Override
        public void accept(GameMenu.Action action) {
            int off = 0;
            if (action == GameMenu.Action.FIRE || action == GameMenu.Action.RIGHT) {
                off = 1;
            } else if (action == GameMenu.Action.LEFT) {
                off = -1;
            }

            global.speed += off;
            if (global.speed < 1) {
                global.speed = 9;
            } else if (global.speed > 9) {
                global.speed = 1;
            }

            global.screen.putChar(49, 8, (char)('0' + global.speed));
        }
    }

    class StartAction implements Consumer<GameMenu.Action> {

        @Override
        public void accept(GameMenu.Action action) {
            menu.dispose();
            next = PLAY_SCENE;
        }
    }
}
