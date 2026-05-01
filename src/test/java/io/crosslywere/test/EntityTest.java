package io.crosslywere.test;

import org.junit.Assert;
import org.junit.Test;

import io.crosslywere.engine.core.component.Transform;
import io.crosslywere.engine.core.entity.ChildEntity;

public class EntityTest {

    private static class TestEntity extends ChildEntity {
        private TestEntity() {
            super("test_entity");
        }

        private TestEntity(String name) {
            super(name);
        }
    }

    @Test
    public void selfParentTest() {
        Assert.assertThrows(RuntimeException.class, () -> {
            TestEntity entity = new TestEntity();
            entity.addChild(entity);
        });
    }

    @Test
    public void reloadTest() {
        Assert.assertThrows("Loaded an already loaded entity", RuntimeException.class, () -> {
            TestEntity entity = new TestEntity();
            entity.onLoad();
            entity.onLoad();
        });
    }

    @Test
    public void unloadUnloadedTest() {
        Assert.assertThrows("Unloaded an already unloaded entity", RuntimeException.class, () -> {
            TestEntity entity = new TestEntity();
            entity.onExit();
        });
    }

    @Test
    public void entityComponentTest() {
        TestEntity entity = new TestEntity();
        Assert.assertTrue("Entities don't work correctly", entity.hasComponent(Transform.class));
    }

    @Test
    public void entityNameTest() {
        Assert.assertThrows("Entities should not be named null", NullPointerException.class,
                () -> new TestEntity((String) null));
    }

}
