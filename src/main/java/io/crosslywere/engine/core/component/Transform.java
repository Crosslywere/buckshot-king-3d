package io.crosslywere.engine.core.component;

import java.util.Objects;

import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.joml.Vector3f;

public final class Transform implements Component {

    private static final Vector3f WORLD_UP = new Vector3f(0, 1, 0);
    private Vector3f position;
    private Vector3f rotation;
    private Vector3f scale;

    public Transform(Transform other) {
        this.position = new Vector3f(other.position);
        this.rotation = new Vector3f(other.rotation);
        this.scale = new Vector3f(other.scale);
    }

    public Transform(Vector3f position, Vector3f rotation, Vector3f scale) {
        this.position = Objects.requireNonNull(position);
        this.rotation = Objects.requireNonNull(rotation);
        this.scale = Objects.requireNonNull(scale);
    }

    public Transform(Vector3f position, Vector3f rotation) {
        this(position, rotation, new Vector3f(1));
    }

    public Transform(Vector3f position) {
        this(position, new Vector3f());
    }

    public Transform() {
        this(new Vector3f());
    }

    public static Vector3f getWorldUp() {
        return WORLD_UP;
    }

    public Vector3f getPosition() {
        return this.position;
    }

    public void setPosition(Vector3f position) {
        this.position = position;
    }

    public void addPosition(Vector3f offset) {
        position.add(offset);
    }

    public float getPitch() {
        return rotation.x();
    }

    public void setPitch(float pitch) {
        rotation.x = pitch;
    }

    public void addPitch(float offset) {
        rotation.x += offset;
    }

    public float getYaw() {
        return rotation.y();
    }

    public void setYaw(float yaw) {
        rotation.y = yaw;
    }

    public void addYaw(float offset) {
        rotation.y += offset;
    }

    public float getRoll() {
        return rotation.z();
    }

    public void setRoll(float roll) {
        rotation.z = roll;
    }

    public void addRoll(float offset) {
        rotation.z += offset;
    }

    public Vector3f getRotation() {
        return getQuaternionRotation().getEulerAnglesXYZ(new Vector3f());
    }

    public Vector3f getScale() {
        return scale;
    }

    public void setScale(Vector3f scale) {
        this.scale = scale;
    }

    public Vector3f getForward() {
        var forward = new Vector3f(0, 0, 1);
        forward.rotate(getQuaternionRotation());
        return forward;
    }

    public Matrix4f getModelMatrix() {
        var model = new Matrix4f();
        model.translate(position);
        model.rotate(getQuaternionRotation());
        model.scale(scale);
        return model;
    }

    public Quaternionf getQuaternionRotation() {
        var pitch = rotation.x();
        var yaw = rotation.y();
        var roll = rotation.z();
        return new Quaternionf().rotateXYZ(pitch, yaw, roll);
    }

}
