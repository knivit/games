package com.tsoft.game;

import com.golden.gamedev.Game;
import com.tsoft.game.keyboard.ActionKeyboard;
import com.tsoft.game.keyboard.AlfaNumericKeyboard;

public class InputController {
    private Game game;

    private ActionKeyboard actionKeyboard;
    private AlfaNumericKeyboard alfaNumericKeyboard;

    public InputController(Game game) {
        this.game = game;
    }

    public ActionKeyboard getActionKeyboard() {
        if (actionKeyboard == null) {
            actionKeyboard = new ActionKeyboard(game);
        }
        return actionKeyboard;
    }

    public AlfaNumericKeyboard getAlfaNumericKeyboard() {
        if (alfaNumericKeyboard == null) {
            alfaNumericKeyboard = new AlfaNumericKeyboard(game);
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
