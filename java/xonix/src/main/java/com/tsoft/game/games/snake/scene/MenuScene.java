package com.tsoft.game.games.snake.scene;

import com.tsoft.game.games.snake.misc.Screen;
import com.tsoft.game.utils.GameScene;
import com.tsoft.game.utils.GameMenu;

import java.util.function.Consumer;

import static com.tsoft.game.games.snake.Snake.PLAY_SCENE;
import static com.tsoft.game.games.snake.Snake.state;

public class MenuScene implements GameScene {

    private GameMenu menu;

    private String next;

    @Override
    public void create() {
        ((Screen)state.screen).showStartMenu();

        menu = new GameMenu();
        menu.create(state,
            new GameMenu.Item(36, 8, 5, new ChangeSpeedAction()),
            new GameMenu.Item(36, 7, 5, new StartAction()));

        next = null;
    }

    @Override
    public void render() {
        menu.render(state);
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

            state.speed += off;
            if (state.speed < 0) {
                state.speed = 9;
            } else if (state.speed > 9) {
                state.speed = 1;
            }

            state.screen.putChar(49, 8, (char)('0' + state.speed));
        }
    }

    class StartAction implements Consumer<GameMenu.Action> {

        @Override
        public void accept(GameMenu.Action action) {
            next = PLAY_SCENE;
        }
    }
}
