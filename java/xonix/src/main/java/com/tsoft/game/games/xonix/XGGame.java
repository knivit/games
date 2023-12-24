package com.tsoft.game.games.xonix;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.TimeUtils;
import com.tsoft.game.games.xonix.mode.XGMenuMode;

import static com.tsoft.game.games.xonix.XGGameState.*;

public class XGGame implements ApplicationListener {

    private Sprite[] sprites;
    private OrthographicCamera camera;
    private SpriteBatch batch;

    public static void main(String[] args) {
        Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
        config.setTitle("Xonix");
        config.setWindowedMode(800, 600);
        config.useVsync(true);
        config.setForegroundFPS(60);
        new Lwjgl3Application(new XGGame(), config);
    }

    @Override
    public void create() {
        // game
        screen = new XGScreen(XGScreen.WIDTH, XGScreen.HEIGHT);
        mode = new XGMenuMode();
        mode.init();

        // sprites
        int n = 0;
        sprites = new Sprite[16 * 6];
        Texture texture = new Texture(Gdx.files.internal("assets/sprites.gif"));
        for (int y = 0; y < 6 * XGScreen.FONT_HEIGHT; y += XGScreen.FONT_HEIGHT) {
            for (int x = 0; x < 16 * XGScreen.FONT_WIDTH; x += XGScreen.FONT_WIDTH) {
                Sprite sprite = new Sprite(texture, x, y, XGScreen.FONT_WIDTH, XGScreen.FONT_HEIGHT);
                sprites[n ++] = sprite;
            }
        }

        // graphics
        camera = new OrthographicCamera();
        camera.setToOrtho(false, XGScreen.FONT_WIDTH*XGScreen.WIDTH, XGScreen.FONT_HEIGHT*XGScreen.HEIGHT);
        batch = new SpriteBatch();
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

            if (mode.finished()) {
                //finish();
            }
        }

        // render
        camera.update();
        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        for (int y = 0; y < screen.getHeight(); y ++) {
            for (int x = 0; x < screen.getWidth(); x ++) {
                int n = screen.getChar(x, y) - 32;
                if (n < 0) {
                    n = 0;
                }

                batch.draw(sprites[n], XGScreen.FONT_WIDTH*x, XGScreen.FONT_HEIGHT*y);
            }
        }
        batch.end();
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void dispose() {

    }
}
