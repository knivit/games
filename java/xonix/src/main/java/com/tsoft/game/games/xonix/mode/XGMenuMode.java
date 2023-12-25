package com.tsoft.game.games.xonix.mode;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.tsoft.game.games.xonix.actor.Flies;
import com.tsoft.game.games.xonix.actor.Fly;
import com.tsoft.game.games.xonix.XGScreen;
import com.tsoft.game.utils.ActionTimer;
import com.tsoft.game.utils.GameMode;

import static com.tsoft.game.games.xonix.XGGameState.*;

public class XGMenuMode implements GameMode {

    private Flies innerFlyes;
    private ActionTimer flyTimer;

    private GameMode nextMode;

    @Override
    public void init() {
        screen.showStartMenu();

        innerFlyes = new Flies();
        innerFlyes.createFlyes(Fly.INNER_FLY_CHAR, XGScreen.EMPTY_CHAR, 2);
        flyTimer = new ActionTimer(100);

        nextMode = null;
    }

    @Override
    public void update() {
        if (Gdx.input.isKeyPressed(Input.Keys.SPACE)) {
            nextMode = new XGPlayMode();
        }

        if (flyTimer.action(time)) {
            innerFlyes.move();
        }
    }

    @Override
    public GameMode nextMode() {
        return nextMode;
    }
}
