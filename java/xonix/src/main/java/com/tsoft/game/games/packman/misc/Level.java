package com.tsoft.game.games.packman.misc;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;

import java.io.BufferedReader;
import java.io.IOException;

import static com.tsoft.game.games.packman.Packman.global;
import static com.tsoft.game.games.packman.misc.Screen.EMPTY_CHAR;

public class Level {

    public static void load(int n) {
        global.screen.fill(EMPTY_CHAR, Color.WHITE);

        FileHandle resource = Gdx.files.internal("assets/packman/levels/" + n + ".txt");

        try (BufferedReader reader = resource.reader(1024)) {
            int y = Screen.HEIGHT - 1;

            String line;
            while ((line = reader.readLine()) != null) {
                for (int x = 0; x < global.screen.getWidth() && x < line.length(); x ++) {
                    global.screen.putChar(x, y, line.charAt(x), Color.WHITE);
                }

                y --;
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
