package com.tsoft.game.utils;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class GameSceneManager {

    private final Map<String, Supplier<GameScene>> creators;

    private final Map<String, GameScene> scenes = new HashMap<>();

    private GameScene scene;

    public GameSceneManager(Map<String, Supplier<GameScene>> creators) {
        this.creators = creators;
    }

    public void create(String startScene) {
        scene = getOrCreate(startScene);
        scene.create();
    }

    public void render() {
        if (scene.next() != null) {
            scene = getOrCreate(scene.next());
            scene.create();
        }

        scene.render();
    }

    private GameScene getOrCreate(String name) {
        return scenes.computeIfAbsent(name, e -> creators.get(e).get());
    }
}
