package com.tsoft.game;

import com.tsoft.game.engine.GameView;
import com.tsoft.game.engine.keyboard.ActionKeyboard;
import com.tsoft.game.engine.keyboard.AlfaNumericKeyboard;

public class InputController {
    private GameView gameView;

    private ActionKeyboard actionKeyboard;
    private AlfaNumericKeyboard alfaNumericKeyboard;

    public InputController(GameView gameView) {
        this.gameView = gameView;
    }

    public ActionKeyboard getActionKeyboard() {
        if (actionKeyboard == null) {
            actionKeyboard = new ActionKeyboard(gameView);
        }
        return actionKeyboard;
    }

    public AlfaNumericKeyboard getAlfaNumericKeyboard() {
        if (alfaNumericKeyboard == null) {
            alfaNumericKeyboard = new AlfaNumericKeyboard(gameView);
        }
        return alfaNumericKeyboard;
    }

    public String toLogString() {
        StringBuilder buf = new StringBuilder("Input Controller {\n");
        buf.append(getActionKeyboard().toLogString()).append('\n');
        buf.append(getAlfaNumericKeyboard().toLogString()).append('\n');
        buf.append('}');

        return buf.toString();
    }
}
