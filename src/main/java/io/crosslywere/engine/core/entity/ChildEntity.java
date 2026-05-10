package io.crosslywere.engine.core.entity;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import io.crosslywere.engine.core.component.Component;
import io.crosslywere.engine.core.component.Transform;

public abstract class ChildEntity implements Entity {

    private Scene parent = null;
    private final String name;
    private final Map<Class<? extends Component>, Component> components = new HashMap<>();
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

    protected Scene getParent() {
        return parent;
    }

    protected void setParent(Scene entity) {
        if (parent == null) {
            this.parent = entity;
            return;
        }
        throw new RuntimeException("Reparenting should not occur!");
    }

    public String getName() {
        return name;
    }

    public void addChild(ChildEntity child) {
        throw new RuntimeException("Child entity cannot have children");
    }

    public Collection<ChildEntity> getChildren() {
        throw new RuntimeException("Child entity cannot have children");
    }

    public void addComponent(Component component) {
        if (!loaded)
            components.put(component.getClass(), component);
        else
            throw new RuntimeException("Cannot add component to a loaded entity");
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

    @Override
    public void onLoad() {
        if (loaded)
            throw new RuntimeException("Cannot load a loaded entity " + name);
        if (loadCallback != null)
            loadCallback.load(this);
        loaded = true;
    }

    @Override
    public void onUpdate(float deltaTime) {
        if (!loaded)
            onLoad();
        if (updateCallback != null)
            updateCallback.update(this, deltaTime);
    }

    public void onExit() {
        if (!loaded)
            throw new RuntimeException("Cannot unload an unloaded entity " + name);
        if (unloadCallback != null)
            unloadCallback.load(this);
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

    public static interface LoadCallback {
        void load(Entity self);
    }

    public static interface UpdateCallback {
        void update(Entity self, float timeDelta);
    }

}
