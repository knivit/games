package com.tsoft.game.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;

import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

public abstract class GameSound {

    private Map<String, Sound> sounds = new HashMap<>();

    private Stack<Sound> stack = new Stack<>();

    public abstract void init();

    public void add(String file) {
        sounds.put(file, Gdx.audio.newSound(Gdx.files.internal("assets/" + file)));
    }

    public void push(String file) {
        Sound sound = sounds.get(file);
        stack.push(sound);
    }

    public void play() {
        while (!stack.isEmpty()) {
            stack.pop().play();
        }
    }
}
