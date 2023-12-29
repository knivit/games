package com.tsoft.game.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.Controllers;
import com.badlogic.gdx.utils.Array;
import com.studiohartman.jamepad.ControllerButton;

public class GameController {

    public static class State {
        public boolean leftPressed;
        public boolean rightPressed;
        public boolean upPressed;
        public boolean downPressed;
        public boolean firePressed;
        public boolean escapePressed;

        public int dx;
        public int dy;
    }

    private State state = new State();

    public void render() {
        Array<Controller> controllers = Controllers.getControllers();
        for (Controller controller : controllers) {
            if (controller.getButton(ControllerButton.DPAD_LEFT.ordinal())) {
                state.leftPressed = true;
            }
            if (controller.getButton(ControllerButton.DPAD_RIGHT.ordinal())) {
                state.rightPressed = true;
            }
            if (controller.getButton(ControllerButton.DPAD_UP.ordinal())) {
                state.upPressed = true;
            }
            if (controller.getButton(ControllerButton.DPAD_DOWN.ordinal())) {
                state.downPressed = true;
            }
            if (controller.getButton(ControllerButton.A.ordinal()) || controller.getButton(ControllerButton.B.ordinal())) {
                state.firePressed = true;
            }
            if (controller.getButton(ControllerButton.GUIDE.ordinal())) {
                state.escapePressed = true;
            }
        }

        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            state.leftPressed = true;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            state.rightPressed = true;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
            state.upPressed = true;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
            state.downPressed = true;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.SPACE) || Gdx.input.isKeyPressed(Input.Keys.ENTER)) {
            state.firePressed = true;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.ESCAPE)) {
            state.escapePressed = true;
        }

        if (state.leftPressed)  {
            state.dx = -1;
        } else if (state.rightPressed)  {
            state.dx = 1;
        } else if (state.upPressed)  {
            state.dy = 1;
        } else if (state.downPressed)  {
            state.dy = -1;
        }
    }

    public State state() {
        State current = state;
        state = new State();
        return current;
    }
}
