package com.tsoft.game.games.snake;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.badlogic.gdx.utils.TimeUtils;
import com.tsoft.game.games.snake.misc.Sound;
import com.tsoft.game.games.snake.misc.Screen;
import com.tsoft.game.games.snake.misc.State;
import com.tsoft.game.games.snake.mode.MenuScene;
import com.tsoft.game.utils.GameController;
import com.tsoft.game.utils.GdxScreen;

public class Snake implements ApplicationListener {

    public static final State state = new State();

    private GdxScreen gdxScreen;

    public static void main(String[] args) {
        Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
        config.setTitle("Snake");
        config.setWindowedMode(800, 600);
        config.useVsync(true);
        config.setForegroundFPS(60);

        new Lwjgl3Application(new Snake(), config);
    }

    @Override
    public void create() {
        // game
        state.screen = new Screen();
        state.scene = new MenuScene();
        state.scene.create();

        // graphics
        gdxScreen = new GdxScreen(Screen.WIDTH, Screen.HEIGHT);
        gdxScreen.create("assets/sprites.gif", Screen.FONT_WIDTH, Screen.FONT_HEIGHT);

        // audio
        state.sound = new Sound();
        state.sound.create();

        // controller
        state.controller = new GameController();
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void render() {
        // game
        state.time = TimeUtils.millis();
        if (state.scene.next() != null) {
            state.scene = state.scene.next();
            state.scene.create();
        } else {
            state.scene.render();
        }

        // graphics
        gdxScreen.render(state.screen);

        // audio
        state.sound.play();

        // controller
        state.controller.update();
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void dispose() {
        gdxScreen.dispose();
    }
}
