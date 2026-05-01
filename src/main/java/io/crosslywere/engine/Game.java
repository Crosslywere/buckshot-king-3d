package io.crosslywere.engine;

import io.crosslywere.engine.core.SceneTree;
import io.crosslywere.engine.core.Window;
import io.crosslywere.engine.core.Window.InputSystem;

public abstract class Game {

    private final Window window;
    protected final InputSystem inputSystem;
    protected final SceneTree sceneTree;
    protected String initialScene = null;

    protected Game(int width, int height, String title) {
        this.sceneTree = createSceneTree();
        this.window = Window.from(width, height, title);
        this.inputSystem = window.getInputSystem();
    }

    protected abstract SceneTree createSceneTree();

    public final void run() {
        if (initialScene != null)
            sceneTree.load(initialScene);
        else
            sceneTree.load(0);
        while (window.isOpen()) {
            sceneTree.update(window.getTimeDelta().floatValue());
        }
        sceneTree.exit(window);
    }

}
