package com.tsoft.game.games.xonix.scene;

import com.tsoft.game.games.xonix.actor.FlyList;
import com.tsoft.game.games.xonix.misc.Screen;
import com.tsoft.game.utils.ActionTimer;
import com.tsoft.game.utils.GameController;
import com.tsoft.game.utils.GameScene;

import static com.tsoft.game.games.xonix.Xonix.PLAY_SCENE;
import static com.tsoft.game.games.xonix.Xonix.global;
import static com.tsoft.game.games.xonix.misc.Screen.INNER_FLY_CHAR;

public class MenuScene implements GameScene {

    private FlyList innerFlyes;
    private ActionTimer flyTimer;

    private String next;

    @Override
    public void create() {
        ((Screen) global.screen).showStartMenu();

        innerFlyes = new FlyList();
        innerFlyes.create(INNER_FLY_CHAR, Screen.EMPTY_CHAR, 2);
        flyTimer = new ActionTimer(100);

        next = null;
    }

    @Override
    public void render() {
        GameController.State controller = global.controller.state(10);
        if (controller.firePressed) {
            next = PLAY_SCENE;
        }

        if (flyTimer.action(global.time)) {
            innerFlyes.move();
        }
    }

    @Override
    public String next() {
        return next;
    }
}
