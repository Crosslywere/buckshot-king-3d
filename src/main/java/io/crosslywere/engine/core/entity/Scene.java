package io.crosslywere.engine.core.entity;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public final class Scene implements Entity {

    private final String name;
    private Camera sceneCamera = null;
    private final List<ChildEntity> children = new ArrayList<>();

    public Scene(String name) {
        this.name = name;
    }

    public Camera getSceneCamera() {
        if (sceneCamera == null)
            setSceneCamera(new Camera());
        return sceneCamera;
    }

    public void setSceneCamera(Camera camera) {
        if (children.contains(camera))
            sceneCamera = Camera.class.cast(children.indexOf(camera));
        children.add(camera);
        sceneCamera = camera;
    }

    @Override
    public void addChild(ChildEntity entity) {
        children.add(entity);
    }

    @Override
    public Collection<ChildEntity> getChildren() {
        return children;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Scene other = (Scene) obj;
        if (name == null) {
            if (other.name != null)
                return false;
        } else if (!name.equals(other.name))
            return false;
        return true;
    }

}
