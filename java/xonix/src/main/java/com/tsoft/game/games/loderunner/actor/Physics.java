package com.tsoft.game.games.loderunner.actor;

import static com.tsoft.game.games.loderunner.LodeRunner.global;
import static com.tsoft.game.games.loderunner.misc.Screen.*;

public class Physics {

    public boolean isFalling(int x, int y) {
        // robot must be in a space, not on a ladder of a rope
        if (global.screen.getChar(x, y) == LADDER_CHAR || global.screen.getChar(x, y) == ROPE_CHAR) {
            return false;
        }

        // there is should be an empty space under it
        if (y > 1) {
            char underChar = global.screen.getChar(x, y - 1);
            if (underChar == EMPTY_CHAR || underChar == TREASURE_CHAR || underChar == PLAYER_CHAR) {
                return true;
            }
        }

        return false;
    }
}
