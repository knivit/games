package com.tsoft.game.games.xonix.actor;

public enum FlyDir {

    LEFT_UP(-1, -1),
    LEFT_DOWN(-1, 1),
    RIGHT_UP(1, -1),
    RIGHT_DOWN(1, 1);

    private final int dX;
    private final int dY;

    FlyDir(int dX, int dY) {
        this.dX = dX;
        this.dY = dY;
    }

    public static FlyDir getRandom() {
        int index = (int)(Math.random() * FlyDir.values().length);
        return FlyDir.values()[index];
    }

    public int getdX() {
        return dX;
    }

    public int getdY() {
        return dY;
    }
}