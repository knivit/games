package com.tsoft.game.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.Controllers;
import com.badlogic.gdx.utils.Array;
import com.studiohartman.jamepad.ControllerButton;

public class GameController {

    public boolean leftPressed;
    public boolean rightPressed;
    public boolean upPressed;
    public boolean downPressed;
    public boolean firePressed;
    public boolean escapePressed;

    public void update() {
        leftPressed = false;
        rightPressed = false;
        upPressed = false;
        downPressed = false;
        firePressed = false;
        escapePressed = false;

        Array<Controller> controllers = Controllers.getControllers();
        for (Controller controller : controllers) {
            if (controller.getButton(ControllerButton.DPAD_LEFT.ordinal())) {
                leftPressed = true;
            }
            if (controller.getButton(ControllerButton.DPAD_RIGHT.ordinal())) {
                rightPressed = true;
            }
            if (controller.getButton(ControllerButton.DPAD_UP.ordinal())) {
                upPressed = true;
            }
            if (controller.getButton(ControllerButton.DPAD_DOWN.ordinal())) {
                downPressed = true;
            }
            if (controller.getButton(ControllerButton.A.ordinal())) {
                firePressed = true;
            }
            if (controller.getButton(ControllerButton.GUIDE.ordinal())) {
                escapePressed = true;
            }
        }

        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            leftPressed = true;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            rightPressed = true;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
            upPressed = true;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
            downPressed = true;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.SPACE)) {
            firePressed = true;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.ESCAPE)) {
            escapePressed = true;
        }
    }
}
