package io.crosslywere.engine.core.component;

public interface Component {

    default boolean load() {
        return true;
    }

    default void cleanup() {
    }

}
