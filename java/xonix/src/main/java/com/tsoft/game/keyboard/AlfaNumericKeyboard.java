package com.tsoft.game.keyboard;

import com.golden.gamedev.Game;

import java.awt.event.KeyEvent;
import java.util.Arrays;

public class AlfaNumericKeyboard {
    public static final char NULL_CHAR = (char)0;
    public static final char ESCAPE_CHAR = (char)27;

    private Game game;

    private char[] typedKeysBuffer = new char[10];
    private int typedKeyPos;

    // key codes '0'..'9', 'A'..'Z', SPACE, ESCAPE
    private int[] keyCodes = new int[10 + 26 + 1 + 1];

    public AlfaNumericKeyboard(Game game) {
        this.game = game;

        int i = 0;
        for (int keyCode = KeyEvent.VK_0; keyCode <= KeyEvent.VK_9; keyCode ++) {
            keyCodes[i ++] = keyCode;
        }
        for (int keyCode = KeyEvent.VK_A; keyCode <= KeyEvent.VK_Z; keyCode ++) {
            keyCodes[i ++] = keyCode;
        }
        keyCodes[i ++] = KeyEvent.VK_ESCAPE;
        keyCodes[i ++] = KeyEvent.VK_SPACE;
    }

    public void update() {
        for (int keyCode : keyCodes) {
            if (game.keyPressed(keyCode) && typedKeyPos < typedKeysBuffer.length) {
                typedKeysBuffer[typedKeyPos ++] = (char)keyCode;
            }
        }
    }

    public char getNextTypedKey() {
        if (typedKeyPos == 0) {
            return NULL_CHAR;
        }

        char typedKey = typedKeysBuffer[0];
        for (int i = 1; i < typedKeysBuffer.length; i ++) {
            typedKeysBuffer[i - 1] = typedKeysBuffer[i];
        }
        typedKeysBuffer[typedKeysBuffer.length - 1] = NULL_CHAR;
        typedKeyPos --;

        return typedKey;
    }

    public void resetBuffer() {
        typedKeyPos = 0;
        Arrays.fill(typedKeysBuffer, NULL_CHAR);
    }

    public String toLogString() {
        StringBuilder buf = new StringBuilder("AlfaNumericKeyboard {");

        boolean isFirst = true;
        buf.append("typedKeysBuffer=[");
        for (char ch : typedKeysBuffer) {
            if (!isFirst) {
                buf.append(", ");
            }

            if ((ch >= '0' && ch <= '9') || (ch >= 'A' && ch <= 'Z')) {
                buf.append(ch);
            } else {
                buf.append("0x");
                if (ch < 16) {
                    buf.append('0');
                }
                buf.append(Integer.toHexString(ch));
            }

            isFirst = false;
        }
        buf.append("], typedKeyPos=").append(typedKeyPos);
        buf.append('}');

        return buf.toString();
    }
}
