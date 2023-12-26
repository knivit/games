package com.tsoft.game.games.xonix;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.badlogic.gdx.utils.TimeUtils;
import com.tsoft.game.games.xonix.mode.XGMenuMode;
import com.tsoft.game.utils.GameController;
import com.tsoft.game.utils.GdxScreen;

import static com.tsoft.game.games.xonix.XGGameState.*;

public class Xonix implements ApplicationListener {

    private GdxScreen gdxScreen;

    public static void main(String[] args) {
        Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
        config.setTitle("Xonix");
        config.setWindowedMode(800, 600);
        config.useVsync(true);
        config.setForegroundFPS(60);

        new Lwjgl3Application(new Xonix(), config);
    }

    @Override
    public void create() {
        // game
        screen = new XGScreen();
        mode = new XGMenuMode();
        mode.init();

        // graphics
        gdxScreen = new GdxScreen(XGScreen.WIDTH, XGScreen.HEIGHT);
        gdxScreen.create("assets/sprites.gif", XGScreen.FONT_WIDTH, XGScreen.FONT_HEIGHT);

        // audio
        sound = new XGGameSound();
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
