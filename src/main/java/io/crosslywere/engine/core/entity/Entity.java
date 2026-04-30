package io.crosslywere.engine.core.entity;

import java.util.Arrays;
import java.util.Collection;

public interface Entity {

    String getName();

    void addChild(ChildEntity entity);

    default void addChildren(ChildEntity... entities) {
        Arrays.asList(entities).forEach(this::addChild);
    }

    default void addChildren(Collection<ChildEntity> entities) {
        entities.forEach(this::addChild);
    }

    Collection<ChildEntity> getChildren();

    default void onLoad() {
        getChildren().forEach(Entity::onLoad);
    }

    default void onUpdate(float deltaTime) {
        getChildren().forEach(child -> child.onUpdate(deltaTime));
    }

    default void onExit() {
        getChildren().forEach(ChildEntity::onExit);
    }

    public static interface LoadCallback {
        void load(Entity self);
    }

    public static interface UpdateCallback {
        void update(Entity self, float timeDelta);
    }

}
