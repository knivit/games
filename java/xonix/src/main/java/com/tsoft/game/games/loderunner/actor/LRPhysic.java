package com.tsoft.game.games.loderunner.actor;

import com.tsoft.game.games.loderunner.LRGameState;
import com.tsoft.game.games.loderunner.LRScreen;

public class LRPhysic {

    public boolean isFalling(int x, int y) {
        // robot must be in a space, not on a ladder of a rope
        if (LRGameState.screen.getChar(x, y) == LRScreen.LADDER_CHAR || LRGameState.screen.getChar(x, y) == LRScreen.ROPE_CHAR) {
            return false;
        }

        // there is should be an empty space under it
        if (y < (LRScreen.HEIGHT - 2)) {
            char underChar = LRGameState.screen.getChar(x, y + 1);
            if (underChar == LRScreen.EMPTY_CHAR || underChar == LRPlayer.PLAYER_CHAR) {
                return true;
            }
        }

        return false;
    }
}
