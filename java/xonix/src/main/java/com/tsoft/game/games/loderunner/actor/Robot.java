package com.tsoft.game.games.loderunner.actor;

import com.badlogic.gdx.graphics.Color;
import com.tsoft.game.games.loderunner.misc.Screen;
import com.tsoft.game.utils.TextSprite;

import java.awt.*;

import static com.tsoft.game.games.loderunner.misc.Screen.*;
import static com.tsoft.game.games.loderunner.LodeRunner.global;

public class Robot {

    private final RobotBehaviour behaviour;
    private final TextSprite sub = new TextSprite();

    private int x;
    private int y;
    private RobotBehaviour.Action action;

    public Robot(int x, int y, RobotBehaviour behaviour) {
        this.x = x;
        this.y = y;
        this.behaviour = behaviour;

        show();
    }

    private boolean canMoveUp(char ch) {
        return ch == EMPTY_CHAR || ch == Screen.LADDER_CHAR ||
            ch == Screen.ROPE_CHAR || ch == Screen.TREASURE_CHAR;
    }

    public void move() {
        hide();

        RobotBehaviour.Action priorAction = action;

        // falling
        int dx = 0, dy;
        if (global.world.getPhysics().isFalling(x, y)) {
            priorAction = null;
            dy = -1;
        } else {
            if (action == null || action.duration < 0) {
                action = behaviour.getRandomAction(global.world.getPlayer() != null);
            }

            Point off = getOff(global.world.getPlayer());
            dx = off.x;
            dy = off.y;
        }

        boolean isUpOrDown = (priorAction != null &&
                ((priorAction.actionType == RobotBehaviour.ActionType.GO_UP) || (priorAction.actionType == RobotBehaviour.ActionType.GO_DOWN)));

        if (!isUpOrDown) {
            switch (sub.ch) {
                case LADDER_CHAR: {
                    int n = (int)(Math.random() * 100);
                    boolean useLadder;
                    if (action.actionType == RobotBehaviour.ActionType.HUNTING_PLAYER) {
                        useLadder = n < behaviour.probabilityLadderAffectsHuntingAction;
                    } else {
                        useLadder = n <  behaviour.probabilityLadderAffectsGoAction;
                    }

                    if (useLadder) {
                        n = (int)(Math.random() * 100);
                        if (n < 50) {
                            action = behaviour.getAction(RobotBehaviour.ActionType.GO_UP);
                        } else {
                            action = behaviour.getAction(RobotBehaviour.ActionType.GO_DOWN);
                        }
                        Point off = getOff(global.world.getPlayer());
                        dx = off.x;
                        dy = off.y;
                    }
                    break;
                }
            }
        }

        int newX = x + dx;
        int newY = y + dy;

        if (newX < 0 || newX >= Screen.WIDTH || newY < 1 || newY >= Screen.HEIGHT) {
            action = null;
        } else {
            char newChar = global.screen.getChar(newX, newY);

            if (dx == 0 && dy == 1 && !canMoveUp(newChar)) {
                action = null;
            } else {
                boolean canMove = true;
                switch (newChar) {
                    case PLAYER_CHAR: {
                        global.world.getPlayer().removeLife();
                        break;
                    }
                    case WALL_CHAR: case ROBOT_CHAR: {
                        action = null;
                        canMove = false;
                    }
                    case EMPTY_CHAR: {
                        if (dy > 0) {
                            action = null;
                            canMove = false;
                        }
                    }
                }

                if (canMove) {
                    x = newX;
                    y = newY;
                }
            }
        }

        show();
    }

    private Point getOff(Player player) {
        int dx = 0, dy = 0;

        switch (action.actionType) {
            case GO_LEFT: {
                dx = -1;
                break;
            }
            case GO_RIGHT: {
                dx = 1;
                break;
            }
            case GO_UP: {
                dy = 1;
                break;
            }
            case GO_DOWN: {
                dy = -1;
                break;
            }
            case WAIT: {
                break;
            }
            case HUNTING_PLAYER: {
                if (x > player.x) {
                    dx = -1;
                }
                if (x < player.x) {
                    dx = 1;
                }
                if (y > player.y) {
                    dy = -1;
                }
                if (y < player.y) {
                    dy = 1;
                }
                break;
            }
        }

        action.duration --;

        return new Point(dx, dy);
    }

    private void hide() {
        if (sub.ch != ROBOT_CHAR) {
            TextSprite sp = global.screen.sprite(x, y);
            sp.ch = sub.ch;
            sp.color = sub.color;
        }
    }

    private void show() {
        TextSprite sp = global.screen.sprite(x, y);
        sub.ch = sp.ch;
        sub.color = sp.color;

        sp.ch = ROBOT_CHAR;
        sp.color = Color.RED;
    }
}
