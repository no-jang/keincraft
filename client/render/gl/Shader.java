package client.render.gl;

import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.lwjgl.opengl.GL20;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

//TODO Add error checking
public class Shader {
    private final int id;

    public Shader(String vertexSourcePath, String fragmentSourcePath) throws IOException {
        // Load shader source code from files
        String vertexSource = Files.readString(Path.of(vertexSourcePath));
        String fragmentSource = Files.readString(Path.of(fragmentSourcePath));

        // Create new shader objects
        int vertexId = GL20.glCreateShader(GL20.GL_VERTEX_SHADER);
        int fragmentId = GL20.glCreateShader(GL20.GL_FRAGMENT_SHADER);

        // Inject shader source code into shader objects
        GL20.glShaderSource(vertexId, vertexSource);
        GL20.glShaderSource(fragmentId, fragmentSource);

        // Compile shader sources
        GL20.glCompileShader(vertexId);
        GL20.glCompileShader(fragmentId);

        // Create shader program and link both shaders to it
        id = GL20.glCreateProgram();
        GL20.glAttachShader(id, vertexId);
        GL20.glAttachShader(id, fragmentId);
        GL20.glLinkProgram(id);

        // Delete shader objects as they are now living inside the program object
        GL20.glDeleteShader(vertexId);
        GL20.glDeleteShader(fragmentId);
    }

    public void use() {
        GL20.glUseProgram(id);
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
}
