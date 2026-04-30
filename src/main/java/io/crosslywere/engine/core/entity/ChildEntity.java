package io.crosslywere.engine.core.entity;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import io.crosslywere.engine.core.component.Component;
import io.crosslywere.engine.core.component.Transform;

public class ChildEntity implements Entity {

    private Entity parent = null;
    private final String name;
    private final Map<Class<? extends Component>, Component> components = new HashMap<>();
    private final Set<ChildEntity> children = new HashSet<>();
    private boolean loaded = false;
    private LoadCallback loadCallback = null;
    private UpdateCallback updateCallback = null;
    private LoadCallback unloadCallback = null;

    protected ChildEntity(String name) {
        this(name, new Transform());
    }

    protected ChildEntity(String name, Transform transform) {
        this.name = Objects.requireNonNull(name);
        addComponent(transform);
    }

    protected Entity getParent() {
        return parent;
    }

    protected void setParent(Entity entity) {
        if (parent == null) {
            this.parent = entity;
        }
        throw new RuntimeException("Reparenting should not occur!");
    }

    public String getSceneName() {
        if (parent != null)
            if (parent instanceof ChildEntity p)
                return p.getSceneName() + "." + getName();
        return getName();
    }

    public String getName() {
        return name;
    }

    public void addChild(ChildEntity child) {
        if (child == this)
            throw new RuntimeException("Cannot make a child entity of self " + getSceneName());
        if (child.parent != null)
            throw new RuntimeException(
                    "Child `" + child.getSceneName() + "` cannot be reparented to `" + getSceneName() + "`");
        child.setParent(this);
        children.add(child);
    }

    public Collection<ChildEntity> getChildren() {
        return children;
    }

    public void addComponent(Component component) {
        if (!loaded)
            components.put(component.getClass(), component);
    }

    public void addComponents(Component... components) {
        Arrays.asList(components).forEach(this::addComponent);
    }

    public <C extends Component> C getComponent(Class<C> componentClass) {
        return componentClass.cast(components.get(componentClass));
    }

    public Collection<Component> getComponents() {
        return components.values();
    }

    public boolean hasComponent(Class<? extends Component> componentClass) {
        return components.containsKey(componentClass);
    }

    public boolean parentHasComponent(Class<? extends Component> componentClass) {
        if (parent instanceof ChildEntity p) {
            return p.hasComponent(componentClass);
        }
        return false;
    }

    @Override
    public void onLoad() {
        if (loaded)
            throw new RuntimeException("Cannot load a loaded entity " + name);
        if (loadCallback != null)
            loadCallback.load(this);
        Entity.super.onLoad();
        loaded = true;
    }

    @Override
    public void onUpdate(float deltaTime) {
        if (!loaded)
            onLoad();
        if (updateCallback != null)
            updateCallback.update(this, deltaTime);
        Entity.super.onUpdate(deltaTime);
    }

    public void onExit() {
        if (!loaded)
            throw new RuntimeException("Cannot unload an unloaded entity " + name);
        unloadCallback.load(this);
        Entity.super.onExit();
        loaded = false;
    }

    public void setLoadCallback(LoadCallback callback) {
        if (!loaded)
            loadCallback = callback;
    }

    public void setUpdateCallback(UpdateCallback callback) {
        if (!loaded)
            updateCallback = callback;
    }

    public void setUnloadCallback(LoadCallback callback) {
        if (!loaded)
            unloadCallback = callback;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((parent == null) ? 0 : parent.hashCode());
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
        ChildEntity other = (ChildEntity) obj;
        if (parent == null) {
            if (other.parent != null)
                return false;
        } else if (!parent.equals(other.parent))
            return false;
        if (name == null) {
            if (other.name != null)
                return false;
        } else if (!name.equals(other.name))
            return false;
        return true;
    }

}
