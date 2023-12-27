package com.tsoft.game.utils;

import java.util.function.Consumer;

public class GameMenu {

    public enum Action {
        LEFT,
        RIGHT,
        FIRE
    }

    public record Item(int x, int y, int len, Consumer<Action> action) { }

    private GameState state;

    private final ActionTimer timer = new ActionTimer(300);

    private Item[] items;

    private int selected;
    private boolean inverse;
    private boolean disposed;

    public void create(GameState state, Item ... items) {
        this.state = state;
        this.items = items;
    }

    public void render(GameState state) {
        if (disposed) {
            return;
        }

        if (!timer.action(state.time)) {
            return;
        }

        // blink
        inverse = !inverse;
        blink(inverse);

        // controller

        // actions
        Action action = null;
        if (state.controller.leftPressed) {
            action = Action.LEFT;
        } else if (state.controller.rightPressed) {
            action = Action.RIGHT;
        } else if (state.controller.firePressed) {
            action = Action.FIRE;
        }

        if (action != null) {
            items[selected].action.accept(action);
            return;
        }

        // up or down
        int off = 0;
        if (state.controller.upPressed) {
            off = 1;
        } else if (state.controller.downPressed) {
            off = -1;
        }

        if (off != 0) {
            if (inverse) {
                blink(false);
            }

            selected += off;
            if (selected < 0) {
                selected = items.length - 1;
            } else if (selected > (items.length - 1)) {
                selected = 0;
            }

            if (inverse) {
                blink(true);
            }
        }
    }

    public void dispose() {
        disposed = true;
        blink(false);
    }

    private void blink(boolean value) {
        Item item = items[selected];
        for (int i = 0; i < item.len; i ++) {
            state.screen.sprite(item.x + i, item.y).inverse = value;
        }
    }
}
