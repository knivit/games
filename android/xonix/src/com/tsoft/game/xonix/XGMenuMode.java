package com.tsoft.game.xonix;

import android.util.TimingLogger;
import com.tsoft.game.InputController;
import com.tsoft.game.engine.ActionTimer;
import com.tsoft.game.engine.keyboard.ActionKeyboard;
import com.tsoft.game.engine.keyboard.AlfaNumericKeyboard;

public class XGMenuMode implements XGMode {
    private Flies innerFlyes;
    private ActionTimer flyActionTimer;

    private boolean isInitialized;
    private boolean isFinishGame;
    private XGMode nextMode;

    private void initResources(XGScreen screen) {
        screen.showStartMenu();

        innerFlyes = new Flies(screen, null);
        innerFlyes.createFlyes(Fly.INNER_FLY_CHAR, XGScreen.EMPTY_CHAR, 2);
        flyActionTimer = new ActionTimer(100);

        isInitialized = true;
    }

    @Override
    public void update(XGScreen screen, InputController inputController, long elapsedTime) {
        TimingLogger timings = new TimingLogger(XGGameView.GAME_TIMING_TAG, "update");
        if (!isInitialized) {
            initResources(screen);
            timings.addSplit("initResources()");
        }

        AlfaNumericKeyboard alfaNumericKeyboard = inputController.getAlfaNumericKeyboard();
        alfaNumericKeyboard.update();
        timings.addSplit("alfaNumericKeyboard.update()");
        if (alfaNumericKeyboard.getNextTypedKey() == AlfaNumericKeyboard.ESCAPE_CHAR) {
            isFinishGame = true;
        }

        ActionKeyboard actionKeyboard = inputController.getActionKeyboard();
        actionKeyboard.update();
        timings.addSplit("actionKeyboard.update()");
        if (actionKeyboard.isKeyPressed(ActionKeyboard.PressedKey.FIRE)) {
            nextMode = new XGPlayMode();
        }

        if (flyActionTimer.action(elapsedTime)) {
            innerFlyes.move();
            timings.addSplit("innerFlyes.move()");
        }
        timings.dumpToLog();
    }

    @Override
    public boolean isFinishGame() {
        return isFinishGame;
    }

    @Override
    public XGMode nextMode() {
        return nextMode;
    }

    @Override
    public String getLogString() {
        StringBuilder buf = new StringBuilder(getClass().getName()).append(" {\n");
        buf.append(innerFlyes.getLogString()).append('\n');
        buf.append('}');
        return buf.toString();
    }
}
