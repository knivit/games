package com.tsoft.game.games.loderunner;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.TimeUtils;
import com.tsoft.game.games.loderunner.mode.LRMenuMode;
import com.tsoft.game.utils.GameController;
import com.tsoft.game.utils.TextSprite;

import static com.tsoft.game.games.loderunner.LRGameState.*;

public class LRGame implements ApplicationListener {

    private Sprite[] sprites;
    private OrthographicCamera camera;
    private SpriteBatch batch;

    public static void main(String[] args) {
        Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
        config.setTitle("Lode Runner");
        config.setWindowedMode(800, 600);
        config.useVsync(true);
        config.setForegroundFPS(60);
        new Lwjgl3Application(new LRGame(), config);
    }

    @Override
    public void create() {
        // game
        world = new LRWorld();
        mode = new LRMenuMode();
        mode.init();

        // sprites
        int n = 0;
        sprites = new Sprite[16 * 6];
        Texture texture = new Texture(Gdx.files.internal("assets/sprites.gif"));
        for (int y = 0; y < 6 * LRScreen.FONT_HEIGHT; y += LRScreen.FONT_HEIGHT) {
            for (int x = 0; x < 16 * LRScreen.FONT_WIDTH; x += LRScreen.FONT_WIDTH) {
                Sprite sprite = new Sprite(texture, x, y, LRScreen.FONT_WIDTH, LRScreen.FONT_HEIGHT);
                sprites[n ++] = sprite;
            }
        }

        // graphics
        camera = new OrthographicCamera();
        camera.setToOrtho(false, LRScreen.FONT_WIDTH*LRScreen.WIDTH, LRScreen.FONT_HEIGHT*LRScreen.HEIGHT);
        batch = new SpriteBatch();

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
        camera.update();
        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        TextSprite sprite = new TextSprite();
        for (int y = 0; y < screen.getHeight(); y ++) {
            for (int x = 0; x < screen.getWidth(); x ++) {
                screen.update(sprite, x, y);
                batch.setColor(sprite.color);
                batch.draw(sprites[sprite.n], sprite.x, sprite.y);
            }
        }
        batch.end();

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
        if (batch != null) {
            batch.dispose();
        }
    }
}
