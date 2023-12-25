package com.tsoft.game.games.loderunner.mode;

import static com.tsoft.game.games.loderunner.LRGameState.screen;

public class LRMenuStatus {

    private static final String INFO_MESSAGE = "PRESS FIRE TO CONTINUE";

    private char[] text;
    private int pos;

    public void update() {
        if (text == null) {
            char[] line = screen.getLine(0);
            text = (new String(line) + INFO_MESSAGE).toCharArray();
        }

        pos ++;
        if (pos == text.length) {
            pos = 0;
        }

        for (int x = 0; x < screen.getWidth(); x ++) {
            int n = pos + x;
            if (n > text.length - 1) {
                n = n - text.length;
            }
            screen.putChar(x, 0, text[n]);
        }
    }
}
