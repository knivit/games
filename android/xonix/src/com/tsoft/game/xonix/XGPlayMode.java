package com.tsoft.game.xonix;

import android.util.TimingLogger;
import com.tsoft.game.engine.ActionTimer;
import com.tsoft.game.InputController;
import com.tsoft.game.engine.keyboard.ActionKeyboard;
import com.tsoft.game.engine.keyboard.AlfaNumericKeyboard;

public class XGPlayMode implements XGMode {
    private InputController inputController;
    private XGScreen screen;

    private Flies innerFlyes;
    private Flies outerFlyes;
    private ActionTimer flyActionTimer;

    private XGPlayer player;
    private ActionTimer playerActionTimer;

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
        playerActionTimer = new ActionTimer(100);

        innerFlyes = new Flies(screen, player);
        innerFlyes.createFlyes(Fly.INNER_FLY_CHAR, XGScreen.EMPTY_CHAR, status.getLevel());

        outerFlyes = new Flies(screen, player);
        outerFlyes.createFlyes(Fly.OUTER_FLY_CHAR, XGScreen.BORDER_CHAR, status.getLevel());

        flyActionTimer = new ActionTimer(100);
    }

    @Override
    public void update(XGScreen screen, InputController inputController, long elapsedTime) {
        TimingLogger timings = new TimingLogger(XGGameView.GAME_TIMING_TAG, "update");
        if (!isInitialized) {
            initResources(screen, inputController);
            timings.addSplit("initResources()");
        }

        AlfaNumericKeyboard alfaNumericKeyboard = inputController.getAlfaNumericKeyboard();
        alfaNumericKeyboard.update();
        timings.addSplit("alfaNumericKeyboard.update()");
        if (alfaNumericKeyboard.getNextTypedKey() == AlfaNumericKeyboard.ESCAPE_CHAR) {
            nextMode = new XGMenuMode();
        }

        ActionKeyboard actionKeyboard = inputController.getActionKeyboard();
        actionKeyboard.update();
        timings.addSplit("actionKeyboard.update()");
        if (playerActionTimer.action(elapsedTime)) {
            player.move();
        }

        if (flyActionTimer.action(elapsedTime)) {
            innerFlyes.move();
            timings.addSplit("innerFlyes.move()");

            outerFlyes.move();
            timings.addSplit("outerFlyes.move()");
        }

        status.update();
        timings.addSplit("status.update()");

        if (status.getLife() < 0) {
            nextMode = new XGMenuMode();
        }

        if (player.isNextLevel()) {
            status.nextLevel();
            resetLevel();
        }
        timings.dumpToLog();
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
