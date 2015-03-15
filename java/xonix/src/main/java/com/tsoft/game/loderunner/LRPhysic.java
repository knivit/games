package com.tsoft.game.loderunner;

public class LRPhysic {
    private LRScreen screen;

    public LRPhysic(LRScreen screen) {
        this.screen = screen;
    }

    public boolean isFalling(int x, int y) {
        // robot must be in a space, not on a ladder of a rope
        if (screen.getChar(x, y) == LRScreen.LADDER_CHAR || screen.getChar(x, y) == LRScreen.ROPE_CHAR) {
            return false;
        }

        // there is should be an empty space under it
        if (y < (LRScreen.HEIGHT - 2)) {
            char underChar = screen.getChar(x, y + 1);
            if (underChar == LRScreen.EMPTY_CHAR || underChar == LRPlayer.PLAYER_CHAR) {
                return true;
            }
        }

        return false;
    }
}
