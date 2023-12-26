package com.tsoft.game.games.loderunner;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.badlogic.gdx.utils.TimeUtils;
import com.tsoft.game.games.loderunner.mode.LRMenuMode;
import com.tsoft.game.utils.GameController;
import com.tsoft.game.utils.GdxScreen;

import static com.tsoft.game.games.loderunner.LRGameState.*;

public class LodeRunner implements ApplicationListener {

    private GdxScreen gdxScreen;

    public static void main(String[] args) {
        Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
        config.setTitle("Lode Runner");
        config.setWindowedMode(800, 600);
        config.useVsync(true);
        config.setForegroundFPS(60);

        new Lwjgl3Application(new LodeRunner(), config);
    }

    @Override
    public void create() {
        // game
        world = new LRWorld();
        mode = new LRMenuMode();
        mode.init();

        // sprites
        gdxScreen = new GdxScreen(LRScreen.WIDTH, LRScreen.HEIGHT);
        gdxScreen.create("assets/sprites.gif", LRScreen.FONT_WIDTH, LRScreen.FONT_HEIGHT);

        // sound
        sound = new LRGameSound();
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

        // render
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
