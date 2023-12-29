package com.tsoft.game.utils;

import java.util.function.Consumer;

public class GameMenu {

    public enum Action {
        LEFT,
        RIGHT,
        FIRE
    }

    public record Item(int x, int y, int len, Consumer<Action> action) { }

    private static final String MENU_SELECTION_CLICK_SOUND = "menu_selection_click.ogg";
    private static final String MENU_ACTION_CLICK_SOUND = "menu_action_click.ogg";

    private GameState state;

    private final ActionTimer blinkTimer = new ActionTimer(300);
    private final ActionTimer controlTimer = new ActionTimer(400);

    private Item[] items;

    private int selected;
    private boolean inverse;
    private boolean disposed;

    public void create(GameState state, Item ... items) {
        this.state = state;
        this.items = items;

        if (state.sound != null) {
            state.sound.put(MENU_SELECTION_CLICK_SOUND);
            state.sound.put(MENU_ACTION_CLICK_SOUND);
        }
    }

    public void render(GameState state) {
        if (disposed) {
            return;
        }

        if (blinkTimer.action(state.time)) {
            inverse = !inverse;
            blink(inverse);
        }

        // controller
        if (!controlTimer.action(state.time)) {
            return;
        }

        // menu actions
        GameController.State controller = state.controller.state();

        Action action = null;
        if (controller.leftPressed) {
            action = Action.LEFT;
        } else if (controller.rightPressed) {
            action = Action.RIGHT;
        } else if (controller.firePressed) {
            action = Action.FIRE;
        }

        if (action != null) {
            state.sound.push(MENU_ACTION_CLICK_SOUND);
            items[selected].action.accept(action);
        } else {
            // menu up or down
            int off = 0;
            if (controller.upPressed) {
                off = -1;
            } else if (controller.downPressed) {
                off = 1;
            }

            if (off != 0) {
                state.sound.push(MENU_SELECTION_CLICK_SOUND);

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
