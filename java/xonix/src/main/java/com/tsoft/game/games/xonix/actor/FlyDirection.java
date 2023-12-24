package com.tsoft.game.games.xonix.actor;

public enum FlyDirection {

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