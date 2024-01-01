package com.tsoft.game.games.packman.scene;

import com.tsoft.game.games.packman.misc.Screen;
import com.tsoft.game.utils.GameController;
import com.tsoft.game.utils.GameScene;

import static com.tsoft.game.games.packman.Packman.PLAY_SCENE;
import static com.tsoft.game.games.packman.Packman.global;

public class MenuScene implements GameScene {

    private String next;

    @Override
    public void create() {
        ((Screen) global.screen).showStartMenu();

        next = null;
    }

    @Override
    public void render() {
        GameController.State controller = global.controller.state(10);
        if (controller.firePressed) {
            next = PLAY_SCENE;
        }
    }

    @Override
    public String next() {
        return next;
    }
}
