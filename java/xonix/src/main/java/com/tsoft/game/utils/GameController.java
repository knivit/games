package com.tsoft.game.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.Controllers;
import com.badlogic.gdx.utils.Array;
import com.studiohartman.jamepad.ControllerButton;
import com.tsoft.game.utils.geom.Point;

public class GameController {

    public boolean leftPressed;
    public boolean rightPressed;
    public boolean upPressed;
    public boolean downPressed;
    public boolean firePressed;
    public boolean escapePressed;

    public Point offset = new Point(0, 0);

    public void render() {
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
            if (controller.getButton(ControllerButton.A.ordinal()) || controller.getButton(ControllerButton.B.ordinal())) {
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
        if (Gdx.input.isKeyPressed(Input.Keys.SPACE) || Gdx.input.isKeyPressed(Input.Keys.ENTER)) {
            firePressed = true;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.ESCAPE)) {
            escapePressed = true;
        }

        offset.x = 0;
        offset.y = 0;
        if (leftPressed)  {
            offset.x = -1;
        } else if (rightPressed)  {
            offset.x = 1;
        } else if (upPressed)  {
            offset.y = 1;
        } else if (downPressed)  {
            offset.y = -1;
        }
    }
}
