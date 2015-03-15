package com.tsoft.game.xonix;

public class Fly {
    public static final char INNER_FLY_CHAR = 'O';
    public static final char OUTER_FLY_CHAR = ' ';

    private enum FlyDirection {
        LEFT_UP(-1, -1),
        LEFT_DOWN(-1, 1),
        RIGHT_UP(1, -1),
        RIGHT_DOWN(1, 1);

        private int dX;
        private int dY;

        FlyDirection(int dX, int dY) {
            this.dX = dX;
            this.dY = dY;
        }

        public static FlyDirection getRandom() {
            int index = (int)(Math.random() * FlyDirection.values().length);
            return FlyDirection.values()[index];
        }

        public int getdX() {
            return dX;
        }

        public int getdY() {
            return dY;
        }
    }

    private int x;
    private int y;
    private FlyDirection dir;

    private char onChar;
    private char offChar;

    private XGScreen screen;
    private XGPlayer player;

    public Fly(XGScreen screen,  XGPlayer player, int x, int y, FlyDirection dir, char onChar, char offChar) {
        this.screen = screen;
        this.player = player;

        this.x = x;
        this.y = y;
        this.dir = dir;
        this.onChar = onChar;
        this.offChar = offChar;
    }

    public static Fly getRandom(XGScreen screen,  XGPlayer player, char onChar, char offChar) {
        int x, y;
        do {
            x = (int)(Math.random() * screen.getWidth());
            y = (int)(Math.random() * (screen.getHeight() - 1)) + 1;
        } while (screen.getChar(x, y) != offChar);
        screen.putChar(x, y, onChar);

        Fly fly = new Fly(screen, player, x, y, FlyDirection.getRandom(), onChar, offChar);
        return fly;
    }

    public void move() {
        screen.putChar(x, y, offChar);

        FlyDirection newDir = dir;
        for (int i = 0; i < FlyDirection.values().length; i ++) {
            int newX = x + newDir.getdX();
            int newY = y + newDir.getdY();
            char newChar;

            if (newX > -1 && newX < screen.getWidth() && newY > 0 && newY < screen.getHeight()) {
                newChar = screen.getChar(newX, newY);
            } else {
                newDir = changeDir(newX, newY, newDir);
                continue;
            }

            boolean hitByInnerFly = (onChar == INNER_FLY_CHAR &&
                    ((newChar == XGPlayer.PLAYER_CHAR || newChar == XGPlayer.PLAYER_PATH_CHAR)) &&
                    player.isInSpace());
            boolean hitByOuterFly = (onChar == OUTER_FLY_CHAR && (newChar == XGPlayer.PLAYER_CHAR) &&
                    (!player.isInSpace()));
            if (hitByInnerFly || hitByOuterFly) {
                player.removeLife();
            }

            boolean canMoveFly = true;
            if (onChar == INNER_FLY_CHAR && newChar != XGScreen.EMPTY_CHAR) {
                canMoveFly = false;
            }
            if (onChar == OUTER_FLY_CHAR && newChar != XGScreen.BORDER_CHAR && newChar != OUTER_FLY_CHAR) {
                canMoveFly = false;
            }

            if (canMoveFly) {
                x = newX;
                y = newY;
                dir = newDir;
                break;
            } else {
                newDir = changeDir(newX, newY, newDir);
            }
        }

        screen.putChar(x, y, onChar);
    }

    private FlyDirection changeDir(int x, int y, FlyDirection dir) {
        switch (dir) {
            case LEFT_UP: {
                char ch1 = getChar(x, y + 1);
                char ch2 = getChar(x + 1, y);
                if (ch1 == offChar) {
                    return FlyDirection.LEFT_DOWN;
                }
                if (ch2 == offChar) {
                    return FlyDirection.RIGHT_UP;
                }
                return FlyDirection.RIGHT_DOWN;
            }

            case RIGHT_UP: {
                char ch1 = getChar(x - 1, y);
                char ch2 = getChar(x, y + 1);
                if (ch1 == offChar) {
                    return FlyDirection.LEFT_UP;
                }
                if (ch2 == offChar) {
                    return FlyDirection.RIGHT_DOWN;
                }
                return FlyDirection.LEFT_DOWN;
            }

            case LEFT_DOWN: {
                char ch1 = getChar(x + 1, y);
                char ch2 = getChar(x, y - 1);
                if (ch1 == offChar) {
                    return FlyDirection.RIGHT_DOWN;
                }
                if (ch2 == offChar) {
                    return FlyDirection.LEFT_UP;
                }
                return FlyDirection.RIGHT_UP;
            }

            case RIGHT_DOWN: {
                char ch1 = getChar(x, y - 1);
                char ch2 = getChar(x - 1, y);
                if (ch1 == offChar) {
                    return FlyDirection.RIGHT_UP;
                }
                if (ch2 == offChar) {
                    return FlyDirection.LEFT_DOWN;
                }
                return FlyDirection.LEFT_UP;
            }
        }

        return dir;
    }

    private char getChar(int x, int y) {
        if (x >= 0 && x < screen.getWidth() && y > 0 && y < screen.getHeight()) {
            return screen.getChar(x, y);
        }
        return XGScreen.EMPTY_CHAR;
    }


    public String toLogString() {
        return "Fly {" +
                "x=" + x +
                ", y=" + y +
                ", dir=" + dir +
                ", onChar=" + onChar +
                ", offChar=" + offChar +
                '}';
    }
}
