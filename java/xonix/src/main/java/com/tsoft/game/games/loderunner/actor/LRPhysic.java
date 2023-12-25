package com.tsoft.game.games.loderunner.actor;

import com.tsoft.game.games.loderunner.LRScreen;

import static com.tsoft.game.games.loderunner.LRGameState.screen;

public class LRPhysic {

    public boolean isFalling(int x, int y) {
        // robot must be in a space, not on a ladder of a rope
        if (screen.getChar(x, y) == LRScreen.LADDER_CHAR || screen.getChar(x, y) == LRScreen.ROPE_CHAR) {
            return false;
        }

        // there is should be an empty space under it
        if (y > 1) {
            char underChar = screen.getChar(x, y - 1);
            if (underChar == LRScreen.EMPTY_CHAR || underChar == LRScreen.TREASURE_CHAR || underChar == LRPlayer.PLAYER_CHAR) {
                return true;
            }
        }

        return false;
    }
}
