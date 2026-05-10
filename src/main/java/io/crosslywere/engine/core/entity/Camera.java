package io.crosslywere.engine.core.entity;

import org.joml.Matrix4f;

import io.crosslywere.engine.core.component.Component;
import io.crosslywere.engine.core.component.Transform;

public final class Camera extends ChildEntity {

    private float fovy;
    private float near;
    private float far;

    public Camera(Component... components) {
        this("camera", components);
    }

    public Camera(Transform transform, Component... components) {
        this("camera", transform, components);
    }

    public Camera(String name, Component... components) {
        this(name, new Transform(), components);
    }

    public Camera(String name, Transform transform, Component... components) {
        this(name, transform, 45f, 0.1f, 1000f, components);
    }

    public Camera(String name, Transform transform, float fovy, float near, float far, Component... components) {
        super(name, transform);
        super.addComponents(components);
        this.fovy = fovy;
        this.near = near;
        this.far = far;
    }

    public Matrix4f getViewMatrix() {
        if (hasComponent(Transform.class)) {
            var transform = getComponent(Transform.class);
            return new Matrix4f().lookAt(transform.getPosition(), transform.getPosition().add(transform.getForward()),
                    Transform.getWorldUp());
        }
        return new Matrix4f();
    }

    public Matrix4f getPerspective(int width, int height) {
        float aspect = (float) width / height;
        return new Matrix4f().perspective((float) Math.toRadians(fovy), aspect, near, far);
    }

    public Matrix4f getOrtho(int width, int height) {
        return new Matrix4f().ortho(-width / 2, width / 2, -height / 2, height / 2, near, far);
    }

    public float getFovY() {
        return fovy;
    }

    public void setFovY(float fovy) {
        this.fovy = fovy;
    }

    public float getNear() {
        return this.near;
    }

    public void setNear(float near) {
        this.near = near;
    }

    public float getFar() {
        return this.far;
    }

    public void setFar(float far) {
        this.far = far;
    }

}
