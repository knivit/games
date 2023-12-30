package com.tsoft.game.games.loderunner.mode;

import static com.tsoft.game.games.loderunner.LodeRunner.global;

public class MenuStatus {

    private static final String INFO_MESSAGE = "PRESS FIRE TO CONTINUE";

    private char[] text;
    private int pos;

    public void update() {
        if (text == null) {
            char[] line = global.screen.getLine(0);
            text = (new String(line) + INFO_MESSAGE).toCharArray();
        }

        pos ++;
        if (pos == text.length) {
            pos = 0;
        }

        for (int x = 0; x < global.screen.getWidth(); x ++) {
            int n = pos + x;
            if (n > text.length - 1) {
                n = n - text.length;
            }
            global.screen.putChar(x, 0, text[n]);
        }
    }
}
