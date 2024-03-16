package com.ychstudio.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.Controllers;
import com.badlogic.gdx.utils.Array;
import com.studiohartman.jamepad.ControllerButton;

public class InputController {

    public static class State {
        public boolean isLeftPressed;
        public boolean isRightPressed;
        public boolean isUpPressed;
        public boolean isDownPressed;
        public boolean isFirePressed;
        public boolean isEscapePressed;
    }

    public static State queryControllers() {
        Array<Controller> controllers = Controllers.getControllers();

        State state = new State();
        for (Controller controller : controllers) {
            if (controller.getButton(ControllerButton.DPAD_LEFT.ordinal())) {
                state.isLeftPressed = true;
            }

            if (controller.getButton(ControllerButton.DPAD_RIGHT.ordinal())) {
                state.isRightPressed = true;
            }

            if (controller.getButton(ControllerButton.DPAD_UP.ordinal())) {
                state.isUpPressed = true;
            }

            if (controller.getButton(ControllerButton.DPAD_DOWN.ordinal())) {
                state.isDownPressed = true;
            }

            if (controller.getButton(ControllerButton.A.ordinal()) || controller.getButton(ControllerButton.B.ordinal())) {
                state.isFirePressed = true;
            }

            if (controller.getButton(ControllerButton.GUIDE.ordinal())) {
                state.isEscapePressed = true;
            }
        }

        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            state.isLeftPressed = true;
        }

        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            state.isRightPressed = true;
        }

        if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
            state.isUpPressed = true;
        }

        if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
            state.isDownPressed = true;
        }

        if (Gdx.input.isKeyPressed(Input.Keys.SPACE) || Gdx.input.isKeyPressed(Input.Keys.ENTER)) {
            state.isFirePressed = true;
        }

        if (Gdx.input.isKeyPressed(Input.Keys.ESCAPE)) {
            state.isEscapePressed = true;
        }

        return state;
    }
}
