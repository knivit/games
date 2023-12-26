package com.tsoft.game.games.snake;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.badlogic.gdx.utils.TimeUtils;
import com.tsoft.game.games.snake.mode.SNMenuMode;
import com.tsoft.game.utils.GameController;
import com.tsoft.game.utils.GdxScreen;

import static com.tsoft.game.games.snake.SNGameState.*;

public class Snake implements ApplicationListener {

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
        screen = new SNScreen();
        mode = new SNMenuMode();
        mode.init();

        // graphics
        gdxScreen = new GdxScreen(SNScreen.WIDTH, SNScreen.HEIGHT);
        gdxScreen.create("assets/sprites.gif", SNScreen.FONT_WIDTH, SNScreen.FONT_HEIGHT);

        // audio
        sound = new SNGameSound();
        sound.init();

        // controller
        controller = new GameController();
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void render() {
        // game
        time = TimeUtils.millis();
        if (mode.nextMode() != null) {
            mode = mode.nextMode();
            mode.init();
        } else {
            mode.update();
        }

        // graphics
        gdxScreen.render(screen);

        // audio
        sound.play();

        // controller
        controller.update();
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
