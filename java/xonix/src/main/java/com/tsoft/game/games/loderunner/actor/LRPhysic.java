package com.tsoft.game.games.loderunner.actor;

import static com.tsoft.game.games.loderunner.LRGameState.screen;
import static com.tsoft.game.games.loderunner.LRScreen.*;

public class LRPhysic {

    public boolean isFalling(int x, int y) {
        // robot must be in a space, not on a ladder of a rope
        if (screen.getChar(x, y) == LADDER_CHAR || screen.getChar(x, y) == ROPE_CHAR) {
            return false;
        }

        // there is should be an empty space under it
        if (y > 1) {
            char underChar = screen.getChar(x, y - 1);
            if (underChar == EMPTY_CHAR || underChar == TREASURE_CHAR || underChar == PLAYER_CHAR) {
                return true;
            }
        }

        return false;
    }
}
