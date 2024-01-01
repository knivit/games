package com.tsoft.game.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;

import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

public abstract class GameSound {

    private final Map<String, Sound> sounds = new HashMap<>();

    private final Stack<Sound> stack = new Stack<>();

    public abstract void create();

    public void put(String file) {
        sounds.put(file, Gdx.audio.newSound(Gdx.files.internal("assets/sound/" + file)));
    }

    public void push(String file) {
        Sound sound = sounds.get(file);
        if (sound == null) {
            throw new IllegalArgumentException("Sound " + file + " not found");
        }
        stack.push(sound);
    }

    public void render() {
        while (!stack.isEmpty()) {
            stack.pop().play();
        }
    }
}
