package com.tsoft.game.games.snake.actor;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.tsoft.game.games.snake.misc.Screen;

import java.io.BufferedReader;
import java.io.IOException;

import static com.tsoft.game.games.snake.Snake.state;

public class Level {

    public static void load(int n) {
        FileHandle resource = Gdx.files.internal("assets/snake/levels/" + n + ".txt");

        try (BufferedReader reader = resource.reader(1024)) {
            int y = Screen.HEIGHT - 1;
            String line;

            while ((line = reader.readLine()) != null) {
                for (int x = 0; x < Screen.WIDTH && x < line.length(); x ++) {
                    state.screen.putChar(x, y, line.charAt(x));
                }

                y --;
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
