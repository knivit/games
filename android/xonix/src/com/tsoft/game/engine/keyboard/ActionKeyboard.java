package com.tsoft.game.engine.keyboard;

import android.view.KeyEvent;
import com.tsoft.game.engine.GameView;

public class ActionKeyboard {
    public enum PressedKey {
        LEFT(KeyEvent.KEYCODE_DPAD_LEFT),
        RIGHT(KeyEvent.KEYCODE_DPAD_RIGHT),
        UP(KeyEvent.KEYCODE_DPAD_UP),
        DOWN(KeyEvent.KEYCODE_DPAD_DOWN),
        FIRE(KeyEvent.KEYCODE_DPAD_CENTER),

        NONE(-1);

        private int keyCode;

        PressedKey(int keyCode) {
            this.keyCode = keyCode;
        }

        public static PressedKey getPressedKey(int keyCode) {
            for (PressedKey key : values()) {
                if (key.getKeyCode() == keyCode) {
                    return key;
                }
            }
            return null;
        }

        public int getKeyCode() {
            return keyCode;
        }
    }

    private GameView gameView;

    // don't include NONE key state
    private boolean[] isKeyPressed = new boolean[PressedKey.values().length - 1];

    public ActionKeyboard(GameView gameView) {
        this.gameView = gameView;
    }

    public void update() {
        for (PressedKey key : PressedKey.values()) {
            if (key != PressedKey.NONE) {
                int keyIndex = PressedKey.getPressedKey(key.getKeyCode()).ordinal();
                isKeyPressed[keyIndex] = gameView.isKeyDown(key.getKeyCode());
            }
        }
    }

    public PressedKey getFirstPressedKey() {
        for (int i = 0; i < isKeyPressed.length; i ++) {
            if (isKeyPressed[i]) {
                return PressedKey.values()[i];
            }
        }
        return PressedKey.NONE;
    }

    public boolean isKeyPressed(PressedKey key) {
        if (key == PressedKey.NONE) {
            return false;
        }

        int keyIndex = PressedKey.getPressedKey(key.getKeyCode()).ordinal();
        return isKeyPressed[keyIndex];
    }

    public String toLogString() {
        StringBuilder buf = new StringBuilder("ActionKeyboard {");

        boolean isFirst = true;
        for (PressedKey key : PressedKey.values()) {
            if (!isFirst) {
                buf.append(", ");
            }
            buf.append(key.name()).append('=').append(isKeyPressed(key));
            isFirst = false;
        }
        buf.append('}');
        return buf.toString();
    }
}
