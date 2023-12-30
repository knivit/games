package com.tsoft.game.games.snake.actor;

import com.badlogic.gdx.graphics.Color;
import com.tsoft.game.games.snake.misc.Screen;
import com.tsoft.game.utils.TextSprite;
import com.tsoft.game.utils.geom.Point;

import java.util.Random;

import static com.tsoft.game.games.snake.Snake.global;
import static com.tsoft.game.games.snake.misc.Screen.EMPTY_CHAR;
import static com.tsoft.game.games.snake.misc.Screen.MOUSE_CHAR;

public class Mouse {

    private static final Random random = new Random();

    public void appear() {
        Point p = findPlace();

        TextSprite sp = global.screen.sprite(p.x, p.y);
        sp.ch = MOUSE_CHAR;
        sp.color = Color.YELLOW;
    }

    private Point findPlace() {
        int count = global.screen.getCharCount(1, 2, Screen.WIDTH - 1, Screen.HEIGHT - 1, EMPTY_CHAR);

        int r = random.nextInt(count);
        for (int y = 2; y < Screen.HEIGHT - 1; y ++) {
            for (int x = 1; x < Screen.WIDTH - 1; x ++) {
                if (r == 0) {
                    return new Point(x, y);
                }
                r --;
            }
        }

        throw new IllegalStateException("Can't find a place for a mouse");
    }
}
