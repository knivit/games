package com.tsoft.game.keyboard;

import com.golden.gamedev.Game;

import java.awt.event.KeyEvent;

public class ActionKeyboard {
    public enum PressedKey {
        LEFT(KeyEvent.VK_LEFT),
        RIGHT(KeyEvent.VK_RIGHT),
        UP(KeyEvent.VK_UP),
        DOWN(KeyEvent.VK_DOWN),
        FIRE(KeyEvent.VK_SPACE),

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

    private Game game;

    // don't include NONE key state
    private boolean[] isKeyPressed = new boolean[PressedKey.values().length - 1];

    public ActionKeyboard(Game game) {
        this.game = game;
    }

    public void update() {
        for (PressedKey key : PressedKey.values()) {
            if (key != PressedKey.NONE) {
                int keyIndex = PressedKey.getPressedKey(key.getKeyCode()).ordinal();
                isKeyPressed[keyIndex] = game.keyDown(key.getKeyCode());
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
