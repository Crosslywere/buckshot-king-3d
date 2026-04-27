package io.crosslywere.engine.core;

import static org.lwjgl.glfw.GLFW.GLFW_CONTEXT_VERSION_MAJOR;
import static org.lwjgl.glfw.GLFW.GLFW_CONTEXT_VERSION_MINOR;
import static org.lwjgl.glfw.GLFW.GLFW_FALSE;
import static org.lwjgl.glfw.GLFW.GLFW_OPENGL_CORE_PROFILE;
import static org.lwjgl.glfw.GLFW.GLFW_OPENGL_PROFILE;
import static org.lwjgl.glfw.GLFW.GLFW_PRESS;
import static org.lwjgl.glfw.GLFW.GLFW_RESIZABLE;
import static org.lwjgl.glfw.GLFW.glfwCreateWindow;
import static org.lwjgl.glfw.GLFW.glfwGetTime;
import static org.lwjgl.glfw.GLFW.glfwInit;
import static org.lwjgl.glfw.GLFW.glfwMakeContextCurrent;
import static org.lwjgl.glfw.GLFW.glfwPollEvents;
import static org.lwjgl.glfw.GLFW.glfwSetCursorPosCallback;
import static org.lwjgl.glfw.GLFW.glfwSetKeyCallback;
import static org.lwjgl.glfw.GLFW.glfwSetMouseButtonCallback;
import static org.lwjgl.glfw.GLFW.glfwSetScrollCallback;
import static org.lwjgl.glfw.GLFW.glfwSetWindowShouldClose;
import static org.lwjgl.glfw.GLFW.glfwSwapBuffers;
import static org.lwjgl.glfw.GLFW.glfwSwapInterval;
import static org.lwjgl.glfw.GLFW.glfwWindowHint;
import static org.lwjgl.glfw.GLFW.glfwWindowShouldClose;

import java.util.HashSet;
import java.util.Set;

import org.joml.Vector2d;
import org.joml.Vector2f;
import org.lwjgl.opengl.GL;

public final class Window {

    private final long handle;
    private final InputSystem inputSystem;
    private double timeDelta = 0.0f;
    private static double totalTime = glfwGetTime();

    private Window(long handle, InputSystem inputSystem) {
        this.handle = handle;
        this.inputSystem = inputSystem;
    }

    private void updateTime() {
        double now = glfwGetTime();
        timeDelta = now - totalTime;
        totalTime = now;
    }

    public boolean isOpen() {
        glfwSwapBuffers(handle);
        inputSystem.processInput();
        glfwPollEvents();
        updateTime();
        return !glfwWindowShouldClose(handle);
    }

    public Double getTimeDelta() {
        return timeDelta;
    }

    public InputSystem getInputSystem() {
        return inputSystem;
    }

    public static Window from(int width, int height, String title) {
        glfwInit();
        glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3);
        glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 3);
        glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE);
        glfwWindowHint(GLFW_RESIZABLE, GLFW_FALSE);
        long handle = glfwCreateWindow(width, height, title, 0L, 0L);
        glfwMakeContextCurrent(handle);
        glfwSwapInterval(1);
        GL.createCapabilities();
        InputSystem inputSystem = new InputSystem(handle);
        glfwSetKeyCallback(handle, inputSystem::keyCallback);
        glfwSetCursorPosCallback(handle, inputSystem::mousePosCallback);
        glfwSetMouseButtonCallback(handle, inputSystem::mouseButtonCallback);
        glfwSetScrollCallback(handle, inputSystem::scrollCallback);
        return new Window(handle, inputSystem);
    }

    public static class InputSystem {

        private final Set<Integer> keysPressed = new HashSet<>();
        private final Set<Integer> mouseButtonsPressed = new HashSet<>();
        private Vector2f mousePos = new Vector2f();
        private Vector2f scrollAmount = new Vector2f();
        private final long handle;

        private InputSystem(long handle) {
            this.handle = handle;
        }

        private void processInput() {
            keysPressed.clear();
            mouseButtonsPressed.clear();
            scrollAmount = new Vector2f();
        }

        private void keyCallback(long window, int key, int scancode, int action, int mods) {
            if (action >= GLFW_PRESS)
                keysPressed.add(key);
        }

        private void mousePosCallback(long window, double xpos, double ypos) {
            mousePos = new Vector2d(xpos, ypos).get(mousePos);
        }

        private void mouseButtonCallback(long window, int button, int action, int mods) {
            if (action >= GLFW_PRESS)
                mouseButtonsPressed.add(button);
        }

        private void scrollCallback(long window, double xoffset, double yoffset) {
            scrollAmount = new Vector2d(xoffset, yoffset).get(scrollAmount);
        }

        public Vector2f getMousePos() {
            return mousePos;
        }

        public Vector2f getScrollAmount() {
            return scrollAmount;
        }

        public boolean isKeyPressed(int key) {
            return keysPressed.contains(key);
        }

        public boolean isAnyKeyPressed() {
            return !keysPressed.isEmpty();
        }

        public boolean isButtonPressed(int button) {
            return mouseButtonsPressed.contains(button);
        }

        public boolean isAnyButtonPressed() {
            return !mouseButtonsPressed.isEmpty();
        }

        public void closeWindow() {
            glfwSetWindowShouldClose(handle, true);
        }

    }

}
