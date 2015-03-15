package com.tsoft.game.loderunner;

import com.golden.gamedev.object.Timer;
import com.tsoft.game.InputController;
import com.tsoft.game.keyboard.ActionKeyboard;
import com.tsoft.game.keyboard.AlfaNumericKeyboard;

public class LRPlayMode implements LRMode {
    private LRWorld world;
    private InputController inputController;

    private Robots robots;
    private Timer robotTimer;

    private LRPlayer player;
    private Timer playerTimer;

    private LRGameStatus status;

    private boolean isInitialized;
    private LRMode nextMode;

    private void initResources(LRWorld world, InputController inputController) {
        this.world = world;
        this.inputController = inputController;

        status = new LRGameStatus(world.getScreen());

        resetLevel();

        isInitialized = true;
    }

    private void resetLevel() {
        world.loadLevel(status.getLevel());

        robots = new Robots(world);
        robotTimer = new Timer(100);

        player = new LRPlayer(world, inputController, status);
        playerTimer = new Timer(100);
    }

    @Override
    public void update(LRWorld world, InputController inputController, long elapsedTime) {
        if (!isInitialized) {
            initResources(world, inputController);
        }

        AlfaNumericKeyboard alfaNumericKeyboard = inputController.getAlfaNumericKeyboard();
        alfaNumericKeyboard.update();
        if (alfaNumericKeyboard.getNextTypedKey() == AlfaNumericKeyboard.ESCAPE_CHAR) {
            nextMode = new LRMenuMode();
        }

        ActionKeyboard actionKeyboard = inputController.getActionKeyboard();
        actionKeyboard.update();
        if (playerTimer.action(elapsedTime)) {
            player.move();
        }

        if (robotTimer.action(elapsedTime)) {
            robots.move();
        }

        status.update();

        if (status.getLife() < 0) {
            nextMode = new LRMenuMode();
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
    public LRMode nextMode() {
        return nextMode;
    }

    @Override
    public String getLogString() {
        StringBuilder buf = new StringBuilder(getClass().getName()).append(" {\n");
        buf.append(robots.getLogString()).append('\n');
        buf.append(player.getLogString()).append('\n');
        buf.append('}');
        return buf.toString();
    }
}
