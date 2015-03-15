package com.tsoft.game.xonix;

import com.golden.gamedev.object.Timer;
import com.tsoft.game.InputController;
import com.tsoft.game.keyboard.ActionKeyboard;
import com.tsoft.game.keyboard.AlfaNumericKeyboard;

public class XGPlayMode implements XGMode {
    private InputController inputController;
    private XGScreen screen;

    private Flies innerFlyes;
    private Flies outerFlyes;
    private Timer flyTimer;

    private XGPlayer player;
    private Timer playerTimer;

    private XGGameStatus status;

    private boolean isInitialized;
    private XGMode nextMode;

    private void initResources(XGScreen screen, InputController inputController) {
        this.screen = screen;
        this.inputController = inputController;

        status = new XGGameStatus(screen);

        resetLevel();

        isInitialized = true;
    }

    private void resetLevel() {
        screen.reset();

        player = new XGPlayer(screen, inputController, status);
        playerTimer = new Timer(100);

        innerFlyes = new Flies(screen, player);
        innerFlyes.createFlyes(Fly.INNER_FLY_CHAR, XGScreen.EMPTY_CHAR, status.getLevel());

        outerFlyes = new Flies(screen, player);
        outerFlyes.createFlyes(Fly.OUTER_FLY_CHAR, XGScreen.BORDER_CHAR, status.getLevel());

        flyTimer = new Timer(100);
    }

    @Override
    public void update(XGScreen screen, InputController inputController, long elapsedTime) {
        if (!isInitialized) {
            initResources(screen, inputController);
        }

        AlfaNumericKeyboard alfaNumericKeyboard = inputController.getAlfaNumericKeyboard();
        alfaNumericKeyboard.update();
        if (alfaNumericKeyboard.getNextTypedKey() == AlfaNumericKeyboard.ESCAPE_CHAR) {
            nextMode = new XGMenuMode();
        }

        ActionKeyboard actionKeyboard = inputController.getActionKeyboard();
        actionKeyboard.update();
        if (playerTimer.action(elapsedTime)) {
            player.move();
        }

        if (flyTimer.action(elapsedTime)) {
            innerFlyes.move();
            outerFlyes.move();
        }

        status.update();

        if (status.getLife() < 0) {
            nextMode = new XGMenuMode();
        }

        if (player.isNextLevel()) {
            status.nextLevel();
            resetLevel();
        }
    }

    @Override
    public boolean isFinishGame() {
        return false;
    }

    @Override
    public XGMode nextMode() {
        return nextMode;
    }

    @Override
    public String getLogString() {
        StringBuilder buf = new StringBuilder(getClass().getName()).append(" {\n");
        buf.append(player.getLogString()).append('\n');
        buf.append(innerFlyes.getLogString()).append('\n');
        buf.append(outerFlyes.getLogString()).append('\n');
        buf.append('}');
        return buf.toString();
    }
}
