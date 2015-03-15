package com.tsoft.game.loderunner;

import com.golden.gamedev.object.Timer;
import com.tsoft.game.InputController;
import com.tsoft.game.keyboard.ActionKeyboard;
import com.tsoft.game.keyboard.AlfaNumericKeyboard;

public class LRMenuMode implements LRMode {
    private Robots robots;
    private Timer robotTimer;

    private boolean isInitialized;
    private boolean isFinishGame;
    private LRMode nextMode;

    private void initResources(LRWorld world) {
        world.loadLevel(0);

        robots = new Robots(world);
        robotTimer = new Timer(100);
        world.setRobots(robots);

        isInitialized = true;
    }

    @Override
    public void update(LRWorld world, InputController inputController, long elapsedTime) {
        if (!isInitialized) {
            initResources(world);
        }

        AlfaNumericKeyboard alfaNumericKeyboard = inputController.getAlfaNumericKeyboard();
        alfaNumericKeyboard.update();
        if (alfaNumericKeyboard.getNextTypedKey() == AlfaNumericKeyboard.ESCAPE_CHAR) {
            isFinishGame = true;
        }

        ActionKeyboard actionKeyboard = inputController.getActionKeyboard();
        actionKeyboard.update();
        if (actionKeyboard.isKeyPressed(ActionKeyboard.PressedKey.FIRE)) {
            nextMode = new LRPlayMode();
        }

        if (robotTimer.action(elapsedTime)) {
            robots.move();
        }
    }

    @Override
    public boolean isFinishGame() {
        return isFinishGame;
    }

    @Override
    public LRMode nextMode() {
        return nextMode;
    }

    @Override
    public String getLogString() {
        StringBuilder buf = new StringBuilder(getClass().getName()).append(" {\n");
        buf.append(robots.getLogString()).append('\n');
        buf.append('}');
        return buf.toString();
    }
}
