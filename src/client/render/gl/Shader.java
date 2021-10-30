package client.render.gl;

import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.lwjgl.opengl.GL20;
import org.lwjgl.system.MemoryStack;

import java.io.IOException;
import java.nio.FloatBuffer;
import java.nio.file.Files;
import java.nio.file.Path;

public class Shader {
    private final int id;

    public Shader(String vertexSourcePath, String fragmentSourcePath) throws IOException {
        // Load and compile vertex and fragment shader
        int vertexId = createShader(vertexSourcePath, GL20.GL_VERTEX_SHADER);
        int fragmentId = createShader(fragmentSourcePath, GL20.GL_FRAGMENT_SHADER);

        // Create shader program and link both shaders to it
        id = GL20.glCreateProgram();
        GL20.glAttachShader(id, vertexId);
        GL20.glAttachShader(id, fragmentId);
        GL20.glLinkProgram(id);

        // Check for linking errors
        if (GL20.glGetProgrami(id, GL20.GL_LINK_STATUS) == 0) {
            throw new RuntimeException("Error linking shaders vertex: " + vertexSourcePath + " fragment: " +
                    fragmentSourcePath + ": " + GL20.glGetProgramInfoLog(id, 1024));
        }

        // Validate program for inconsistencies
        GL20.glValidateProgram(id);

        // Check for validating errors
        if (GL20.glGetProgrami(id, GL20.GL_VALIDATE_STATUS) == 0) {
            throw new RuntimeException("Error validating shaders vertex: " + vertexSourcePath + " fragment: " +
                    fragmentSourcePath + ": " + GL20.glGetProgramInfoLog(id, 1024));
        }

        // Delete shader objects as they are now living inside the program object
        GL20.glDeleteShader(vertexId);
        GL20.glDeleteShader(fragmentId);
    }

    private static int createShader(String sourcePath, int shaderType) throws IOException {
        String shaderSource = Files.readString(Path.of(sourcePath));

        // Create, inject source and compile shader
        int shaderId = GL20.glCreateShader(shaderType);
        GL20.glShaderSource(shaderId, shaderSource);
        GL20.glCompileShader(shaderId);

        // Check for compilation errors
        if (GL20.glGetShaderi(shaderId, GL20.GL_COMPILE_STATUS) == 0) {
            throw new RuntimeException("Error compiling shader " + sourcePath + ": " + GL20.glGetShaderInfoLog(shaderId, 1024));
        }

        return shaderId;
    }

    public void bind() {
        GL20.glUseProgram(id);
    }

    public void destroy() {
        GL20.glDeleteProgram(id);
    }

    public void setInt(String name, int value) {
        GL20.glUniform1i(GL20.glGetUniformLocation(id, name), value);
    }

    public void setFloat(String name, float value) {
        GL20.glUniform1f(GL20.glGetUniformLocation(id, name), value);
    }

    public void setVec2(String name, Vector2f value) {
        GL20.glUniform2f(GL20.glGetUniformLocation(id, name), value.x, value.y);
    }

    public void setVec3(String name, Vector3f value) {
        GL20.glUniform3f(GL20.glGetUniformLocation(id, name), value.x, value.y, value.z);
    }

    public void setVec4(String name, Vector4f value) {
        GL20.glUniform4f(GL20.glGetUniformLocation(id, name), value.x, value.y, value.z, value.w);
    }

    public void setMat4(String name, Matrix4f matrix) {
        try (MemoryStack stack = MemoryStack.stackPush()) {
            FloatBuffer buffer = stack.mallocFloat(16);
            matrix.get(buffer);
            GL20.glUniformMatrix4fv(GL20.glGetUniformLocation(id, name), false, buffer);
        }
    }
}
