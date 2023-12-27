package com.tsoft.game.games.xonix.scene;

import com.tsoft.game.games.xonix.actor.FlyList;
import com.tsoft.game.games.xonix.misc.Screen;
import com.tsoft.game.utils.ActionTimer;
import com.tsoft.game.utils.GameScene;

import static com.tsoft.game.games.xonix.Xonix.state;
import static com.tsoft.game.games.xonix.misc.Screen.INNER_FLY_CHAR;

public class MenuScene implements GameScene {

    private FlyList innerFlyes;
    private ActionTimer flyTimer;

    private GameScene next;

    @Override
    public void create() {
        ((Screen)state.screen).showStartMenu();

        innerFlyes = new FlyList();
        innerFlyes.create(INNER_FLY_CHAR, Screen.EMPTY_CHAR, 2);
        flyTimer = new ActionTimer(100);

        next = null;
    }

    @Override
    public void render() {
        if (state.controller.firePressed) {
            next = new PlayScene();
        }

        if (flyTimer.action(state.time)) {
            innerFlyes.move();
        }
    }

    @Override
    public GameScene next() {
        return next;
    }
}