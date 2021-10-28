package client.render;

import client.MinecraftClient;
import org.joml.Vector2i;
import org.lwjgl.opengl.*;
import org.lwjgl.system.MemoryUtil;

public class RenderManager {
    private final MinecraftClient client;

    private int shaderProgram;
    private int vao;

    public RenderManager(MinecraftClient client) {
        this.client = client;
    }

    public void init() {
        // Setup opengl
        GL.createCapabilities();
        GLUtil.setupDebugMessageCallback(System.err);

        // Set viewport size
        Vector2i framebufferSize = client.getWindow().getFramebufferSize();
        GL11.glViewport(0, 0, framebufferSize.x, framebufferSize.y);

        // Shader compilation
        String vertexShaderSource = "#version 330 core\n" +
                "layout (location = 0) in vec3 aPos;\n" +
                "\n" +
                "void main()\n" +
                "{\n" +
                "    gl_Position = vec4(aPos.x, aPos.y, aPos.z, 1.0);\n" +
                "}";

        String fragmentShaderSource = "#version 330 core\n" +
                "out vec4 FragColor;\n" +
                "\n" +
                "void main()\n" +
                "{\n" +
                "    FragColor = vec4(1.0f, 0.5f, 0.2f, 1.0f);\n" +
                "} ";

        int vertexShader = GL20.glCreateShader(GL20.GL_VERTEX_SHADER);
        int fragmentShader = GL20.glCreateShader(GL20.GL_FRAGMENT_SHADER);

        GL20.glShaderSource(vertexShader, vertexShaderSource);
        GL20.glShaderSource(fragmentShader, fragmentShaderSource);

        GL20.glCompileShader(vertexShader);
        GL20.glCompileShader(fragmentShader);

        shaderProgram = GL20.glCreateProgram();
        GL20.glAttachShader(shaderProgram, vertexShader);
        GL20.glAttachShader(shaderProgram, fragmentShader);

        GL20.glLinkProgram(shaderProgram);

        GL20.glDeleteShader(vertexShader);
        GL20.glDeleteShader(fragmentShader);

        // Triangles vertices (coordinates)
        float[] vertices = {
                0.5f, 0.5f, 0.0f, // top right
                0.5f, -0.5f, 0.0f, // bottom right
                -0.5f, -0.5f, 0.0f, // bottom left
                -0.5f, 0.5f, 0.0f // top left
        };

        // Triangles indices
        float[] indices = {
                0, 1, 3, // first
                1, 2, 3 // second
        };

        // Create new vertex array object for storing the vbo and attribs permanent
        vao = GL30.glGenVertexArrays();

        // Create new vertex buffer object for storing vertices
        int vbo = GL15.glGenBuffers();

        // Create new element array buffer for storing indices
        int ebo = GL15.glGenBuffers();

        // Bind vao for storing vbo and attribs
        GL30.glBindVertexArray(vao);

        // Bind vbo to store it in current vao
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vbo);

        // Set data of vbo
        GL15.glBufferData(GL15.GL_ARRAY_BUFFER, vertices, GL15.GL_STATIC_DRAW);

        // Bind ebo to store it in current vao
        GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, ebo);

        // Set data of ebo
        GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, indices, GL15.GL_STATIC_DRAW);

        // Set vertex attributes for sending them to vertex shader
        GL20.glVertexAttribPointer(0, 3, GL11.GL_FLOAT, false, 3 * Float.BYTES, MemoryUtil.NULL);
        GL20.glEnableVertexAttribArray(0);

        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
        GL30.glBindVertexArray(0);
    }

    public void render() {
        // Set screen color to green
        GL11.glClearColor(0.0f, 1.0f, 0.0f, 1.0f);
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);

        // Use shaders
        GL20.glUseProgram(shaderProgram);

        // Bind vao for rendering the vbo with attribs given to the vertex shader
        GL30.glBindVertexArray(vao);

        // Draw the triangle
        GL11.glDrawElements(GL11.GL_TRIANGLES, 6, GL11.GL_UNSIGNED_INT, 0);
    }

    public void destroy() {

    }
}
