package io.crosslywere.engine.core.component;

import static org.lwjgl.opengl.GL20.GL_COMPILE_STATUS;
import static org.lwjgl.opengl.GL20.GL_FRAGMENT_SHADER;
import static org.lwjgl.opengl.GL20.GL_LINK_STATUS;
import static org.lwjgl.opengl.GL20.GL_VERTEX_SHADER;
import static org.lwjgl.opengl.GL20.glAttachShader;
import static org.lwjgl.opengl.GL20.glCompileShader;
import static org.lwjgl.opengl.GL20.glCreateProgram;
import static org.lwjgl.opengl.GL20.glCreateShader;
import static org.lwjgl.opengl.GL20.glDeleteProgram;
import static org.lwjgl.opengl.GL20.glDeleteShader;
import static org.lwjgl.opengl.GL20.glGetProgrami;
import static org.lwjgl.opengl.GL20.glGetShaderInfoLog;
import static org.lwjgl.opengl.GL20.glGetShaderi;
import static org.lwjgl.opengl.GL20.glGetUniformLocation;
import static org.lwjgl.opengl.GL20.glLinkProgram;
import static org.lwjgl.opengl.GL20.glShaderSource;
import static org.lwjgl.opengl.GL20.glUniform1f;
import static org.lwjgl.opengl.GL20.glUniform1i;
import static org.lwjgl.opengl.GL20.glUniform3f;
import static org.lwjgl.opengl.GL20.glUniformMatrix4fv;
import static org.lwjgl.opengl.GL20.glUseProgram;

import java.nio.FloatBuffer;
import java.util.HashMap;
import java.util.Map;

import org.joml.Matrix4f;
import org.joml.Vector3f;

public final class ShaderProgram implements Component {

    private final String vertexSource;
    private final String fragmentSource;
    private int program;
    private boolean loaded = false;
    private final Map<String, Integer> uniformCache = new HashMap<>();

    public ShaderProgram(String vertexSource, String fragmentSource) {
        this.vertexSource = vertexSource;
        this.fragmentSource = fragmentSource;
    }

    @Override
    public boolean load() {
        int vs = compileShader(vertexSource, GL_VERTEX_SHADER);
        if (glGetShaderi(vs, GL_COMPILE_STATUS) == 0) {
            glDeleteShader(vs);
            return false;
        }
        int fs = compileShader(fragmentSource, GL_FRAGMENT_SHADER);
        if (glGetShaderi(fs, GL_COMPILE_STATUS) == 0) {
            glDeleteShader(vs);
            glDeleteShader(fs);
            return false;
        }
        program = glCreateProgram();
        glAttachShader(program, vs);
        glAttachShader(program, fs);
        glLinkProgram(program);
        glDeleteShader(vs);
        glDeleteShader(fs);
        final var success = glGetProgrami(program, GL_LINK_STATUS);
        if (success == 0) {
            glDeleteProgram(program);
            program = 0;
            return false;
        } else {
            loaded = true;
        }
        return loaded;
    }

    @Override
    public void cleanup() {
        glDeleteProgram(program);
        program = 0;
        loaded = false;
    }

    private static int compileShader(String source, int type) {
        var shader = glCreateShader(type);
        glShaderSource(shader, source);
        glCompileShader(shader);
        var success = glGetShaderi(shader, GL_COMPILE_STATUS);
        if (success == 0)
            System.err.println(glGetShaderInfoLog(shader));
        return shader;
    }

    private int getUniformLocation(String uniformName) {
        if (!uniformCache.containsKey(uniformName)) {
            var location = glGetUniformLocation(program, uniformName);
            uniformCache.put(uniformName, location);
            System.out.println(uniformName + " = " + location);
        }
        return uniformCache.get(uniformName);
    }

    public void use() {
        assert loaded;
        glUseProgram(program);
    }

    public void setFloat(String name, float value) {
        use();
        glUniform1f(getUniformLocation(name), value);
    }

    public void setTexture(String name, Framebuffer framebuffer, int value) {
        framebuffer.unbind();
        framebuffer.bindTexture(value);
        setInt(name, value);
    }

    public void setInt(String name, int value) {
        use();
        glUniform1i(getUniformLocation(name), value);
    }

    public void setFloat3(String name, Vector3f value) {
        use();
        glUniform3f(getUniformLocation(name), value.x(), value.y(), value.z());
    }

    public void setMatrix4(String name, Matrix4f value) {
        use();
        var buffer = value.get(FloatBuffer.allocate(16));
        glUniformMatrix4fv(getUniformLocation(name), false, buffer);
    }

}
