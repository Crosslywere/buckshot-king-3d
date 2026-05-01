package io.crosslywere.engine;

import io.crosslywere.engine.core.Window;
import io.crosslywere.engine.core.Window.InputSystem;

public abstract class Game {

    private final Window window;
    protected final InputSystem inputSystem;

    protected Game(int width, int height, String title) {
        this.window = Window.from(width, height, title);
        this.inputSystem = window.getInputSystem();
    }

    public final void run() {
    }

}
