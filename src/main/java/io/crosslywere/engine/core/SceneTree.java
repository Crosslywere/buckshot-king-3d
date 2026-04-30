package io.crosslywere.engine.core;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import io.crosslywere.engine.core.entity.Scene;

public final class SceneTree {

    private final Set<Scene> scenes = new HashSet<>();
    private Scene activeScene = null;

    private SceneTree() {
    }

    public void load(int sceneIndex) {
        if (activeScene != null)
            activeScene.onExit();
        if (sceneIndex >= scenes.size())
            throw new IndexOutOfBoundsException();
        activeScene = scenes.stream().toList().get(sceneIndex);
        activeScene.onLoad();
    }

    public void load(String sceneName) {
        if (activeScene != null)
            activeScene.onExit();
        activeScene = scenes.stream().filter(scene -> scene.getName().equals(sceneName)).findFirst()
                .orElseThrow(() -> new RuntimeException("No such scene with name exists::" + sceneName));
    }

    public void update(float deltaTime) {
        activeScene.onUpdate(deltaTime);
    }

    public void exit(Window window) {
        activeScene.onExit();
        window.exit();
    }

    public static SceneTree from(Collection<Scene> scenes) {
        var tree = new SceneTree();
        tree.scenes.addAll(scenes);
        return tree;
    }

    public static SceneTree from(Scene... scenes) {
        return from(Arrays.asList(scenes));
    }

}
