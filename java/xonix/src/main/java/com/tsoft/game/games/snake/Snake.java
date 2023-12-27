package com.tsoft.game.games.snake;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.badlogic.gdx.utils.TimeUtils;
import com.tsoft.game.games.snake.misc.Sound;
import com.tsoft.game.games.snake.misc.Screen;
import com.tsoft.game.games.snake.misc.State;
import com.tsoft.game.games.snake.scene.MenuScene;
import com.tsoft.game.games.snake.scene.PlayScene;
import com.tsoft.game.utils.GameController;
import com.tsoft.game.utils.GameSceneManager;
import com.tsoft.game.utils.GdxScreen;

import java.util.Map;

public class Snake implements ApplicationListener {

    public static final String MENU_SCENE = "MENU_SCENE";
    public static final String PLAY_SCENE = "PLAY_SCENE";

    public static final State state = new State();

    private GameSceneManager sceneManager;
    private GdxScreen gdxScreen;

    public static void main(String[] args) {
        Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
        config.setTitle("Snake");
        config.setWindowedMode(1024, 768);
        config.useVsync(true);
        config.setForegroundFPS(60);

        new Lwjgl3Application(new Snake(), config);
    }

    @Override
    public void create() {
        // screen
        state.screen = new Screen();

        // graphics
        gdxScreen = new GdxScreen(Screen.WIDTH, Screen.HEIGHT);
        gdxScreen.create("assets/sprites.gif", Screen.FONT_WIDTH, Screen.FONT_HEIGHT);

        // audio
        state.sound = new Sound();
        state.sound.create();

        // controller
        state.controller = new GameController();

        // scenes
        sceneManager = new GameSceneManager(Map.of(
            MENU_SCENE, MenuScene::new,
            PLAY_SCENE, PlayScene::new));
        sceneManager.create(MENU_SCENE);
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void render() {
        // clock
        state.time = TimeUtils.millis();

        // graphics
        gdxScreen.render(state.screen);

        // audio
        state.sound.render();

        // controller
        state.controller.render();

        // scenes
        sceneManager.render();
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
