package io.crosslywere.test;

import org.junit.Assert;
import org.junit.Test;

import io.crosslywere.engine.Game;
import io.crosslywere.engine.core.SceneTree;
import io.crosslywere.engine.core.component.ShaderProgram;

public final class ShaderProgramComponentTest extends Game {

    public ShaderProgramComponentTest() {
        super(800, 600, "Shader Program Component Test");
    }

    protected SceneTree createSceneTree() {
        return SceneTree.from();
    }

    private ShaderProgram goodShader = new ShaderProgram("""
            #version 330 core
            layout (location = 0) in vec3 aPosition;
            void main() {
                gl_Position = vec4(aPosition, 1.0);
            }
            """, """
            #version 330 core
            void main() {
                gl_FragColor = vec4(1.0);
            }
            """);

    private ShaderProgram badShader = new ShaderProgram("""
            #version 330 core
            layout (location = 0) in vec3 aPosition;
            void main() {
                gl_Position = vec4(aPosition, 1.0);
            }
            """, """
            #version 330 core
            layout (location = 0) out vec4 oColor;
            void main() {
                gl_FragColor = vec4(1.0) // A semi colon is missing here
            }
            """);

    @Test
    public void compileTest() {
        Assert.assertTrue("Loading doesn't return true when compiled correctly", goodShader.load());
    }

    @Test
    public void failedCompileTest() {
        Assert.assertFalse("Loading doesn't return false when not compiled correctly", badShader.load());
    }

    @Test
    public void useShaderTest() {
        goodShader.load();
        goodShader.use();
        goodShader.cleanup();
    }

    @Test
    public void failedUseShaderTest() {
        badShader.load();
        Assert.assertThrows("Using bad shaders should result in an assertion error", AssertionError.class,
                () -> badShader.use());
        badShader.cleanup();
    }

}
