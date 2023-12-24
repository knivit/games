package com.tsoft.game.games.loderunner.actor;

import com.tsoft.game.games.loderunner.LRScreen;

import java.awt.*;

import static com.tsoft.game.games.loderunner.LRGameState.*;

public class Robot {

    public static final char START_PLACE_CHAR = 'Z';
    public static final char ROBOT_CHAR = '@';

    private RobotBehaviour behaviour;

    private int x;
    private int y;
    private RobotBehaviour.Action action;
    private char offChar;

    public Robot(int x, int y, RobotBehaviour behaviour) {
        this.x = x;
        this.y = y;
        this.behaviour = behaviour;

        show();
    }

    private boolean canMoveUp(char ch) {
        return ch == LRScreen.EMPTY_CHAR || ch == LRScreen.LADDER_CHAR ||
                ch == LRScreen.ROPE_CHAR || ch == LRScreen.TREASURE_CHAR;
    }

    public void move() {
        hide();

        RobotBehaviour.Action priorAction = action;

        // falling
        int dx = 0, dy = 0;
        if (world.getPhysic().isFalling(x, y)) {
            priorAction = null;
            dy = 1;
        } else {
            if (action == null || action.duration < 0) {
                action = behaviour.getRandomAction(world.getPlayer() != null);
            }
            Point off = getOff(world.getPlayer());
            dx = off.x;
            dy = off.y;
        }

        boolean isUpOrDown = (priorAction != null &&
                ((priorAction.actionType == RobotBehaviour.ActionType.GO_UP) || (priorAction.actionType == RobotBehaviour.ActionType.GO_DOWN)));
        if (!isUpOrDown) {
            switch (offChar) {
                case LRScreen.LADDER_CHAR: {
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
                        Point off = getOff(world.getPlayer());
                        dx = off.x;
                        dy = off.y;
                    }
                    break;
                }
            }
        }

        int newX = x + dx;
        int newY = y + dy;

        if (newX < 0 || newX >= LRScreen.WIDTH || newY < 0 || newY >= (LRScreen.HEIGHT - 1)) {
            action = null;
        } else {
            char newChar = screen.getChar(newX, newY);
            if (dx == 0 && dy == -1 && !canMoveUp(newChar)) {
                action = null;
            } else {
                boolean canMove = true;
                switch (newChar) {
                    case LRPlayer.PLAYER_CHAR: {
                        world.getPlayer().removeLife();
                        break;
                    }
                    case LRScreen.WALL_CHAR: {
                        action = null;
                        canMove = false;
                    }
                    case LRScreen.EMPTY_CHAR: {
                        if (dy < 0) {
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

    private Point getOff(LRPlayer player) {
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
                dy = -1;
                break;
            }
            case GO_DOWN: {
                dy = 1;
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
        if (offChar != ROBOT_CHAR) {
            screen.putChar(x, y, offChar);
        }
    }

    private void show() {
        offChar = screen.getChar(x, y);
        screen.putChar(x, y, ROBOT_CHAR);
    }

    public String getLogString() {
        return "Robot {" +
                "x=" + x +
                ", y=" + y +
                ", action=" + (action == null ? "null" : action.getLogString()) +
                ", offChar='" + offChar + '\'' +
                '}';
    }
}
