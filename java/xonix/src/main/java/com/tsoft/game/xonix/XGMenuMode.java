package com.tsoft.game.xonix;

import com.golden.gamedev.object.Timer;
import com.tsoft.game.InputController;
import com.tsoft.game.keyboard.ActionKeyboard;
import com.tsoft.game.keyboard.AlfaNumericKeyboard;

public class XGMenuMode implements XGMode {
    private Flies innerFlyes;
    private Timer flyTimer;

    private boolean isInitialized;
    private boolean isFinishGame;
    private XGMode nextMode;

    private void initResources(XGScreen screen) {
        screen.showStartMenu();

        innerFlyes = new Flies(screen, null);
        innerFlyes.createFlyes(Fly.INNER_FLY_CHAR, XGScreen.EMPTY_CHAR, 2);
        flyTimer = new Timer(100);

        isInitialized = true;
    }

    @Override
    public void update(XGScreen screen, InputController inputController, long elapsedTime) {
        if (!isInitialized) {
            initResources(screen);
        }

        AlfaNumericKeyboard alfaNumericKeyboard = inputController.getAlfaNumericKeyboard();
        alfaNumericKeyboard.update();
        if (alfaNumericKeyboard.getNextTypedKey() == AlfaNumericKeyboard.ESCAPE_CHAR) {
            isFinishGame = true;
        }

        ActionKeyboard actionKeyboard = inputController.getActionKeyboard();
        actionKeyboard.update();
        if (actionKeyboard.isKeyPressed(ActionKeyboard.PressedKey.FIRE)) {
            nextMode = new XGPlayMode();
        }

        if (flyTimer.action(elapsedTime)) {
            innerFlyes.move();
        }
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
