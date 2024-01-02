package com.tsoft.game.games.packman;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.badlogic.gdx.utils.TimeUtils;
import com.tsoft.game.games.packman.misc.Global;
import com.tsoft.game.games.packman.misc.Screen;
import com.tsoft.game.games.packman.misc.Sound;
import com.tsoft.game.utils.GameController;
import com.tsoft.game.utils.GameSceneManager;
import com.tsoft.game.utils.GdxScreen;

import com.tsoft.game.games.packman.scene.MenuScene;
import com.tsoft.game.games.packman.scene.PlayScene;
import com.tsoft.game.utils.console.nes.ChrTable;
import com.tsoft.game.utils.console.nes.ChrTableLoader;
import com.tsoft.game.utils.console.nes.PPU;

import java.util.Map;

public class Packman implements ApplicationListener {

    public static final String MENU_SCENE = "MENU_SCENE";
    public static final String PLAY_SCENE = "PLAY_SCENE";

    public static final Global global = new Global();

    private GameSceneManager sceneManager;
    private GdxScreen gdxScreen;

    public static void main(String[] args) {
        Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
        config.setTitle("Packman");
        config.setWindowedMode(1024, 768);
        config.useVsync(true);
        config.setForegroundFPS(60);

        new Lwjgl3Application(new Packman(), config);
    }

    @Override
    public void create() {
        // screen
        global.screen = new Screen();

        // NES sprites
        ChrTable[] chrTables = ChrTableLoader.load("assets/packman/alpha.chr");
        PPU ppu = new PPU();
        ppu.draw(chrTables[0], new int[] { 63, 16, 32, 48 });

        // graphics
        gdxScreen = new GdxScreen(Screen.WIDTH, Screen.HEIGHT, Screen.FONT_WIDTH, Screen.FONT_HEIGHT);
        gdxScreen.create(ppu.pixmap, (char) 0);

        // audio
        global.sound = new Sound();
        global.sound.create();

        // controller
        global.controller = new GameController();

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
        global.time = TimeUtils.millis();

        // graphics
        gdxScreen.render(global.screen);

        // audio
        global.sound.render();

        // controller
        global.controller.render();

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
