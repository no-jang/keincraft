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

        // Init vertex buffer arrays for triangle coordinates
        float[] vertices = {
                -0.5f, -0.5f, 0.0f,
                0.5f, -0.5f, 0.0f,
                0.0f, 0.5f, 0.0f
        };

        // Create new vertex array object for storing the vbo and attribs permanent
        vao = GL30.glGenVertexArrays();

        // Create new vertex buffer object for storing the vertices
        int vbo = GL15.glGenBuffers();

        // Bind vao for storing vbo and attribs
        GL30.glBindVertexArray(vao);

        // Bind vbo to store it in current vao
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vbo);

        // Set data of the buffer object
        GL15.glBufferData(GL15.GL_ARRAY_BUFFER, vertices, GL15.GL_STATIC_DRAW);

        // Set vertex attributes for sending them to vertex shader
        GL20.glVertexAttribPointer(0, 3, GL11.GL_FLOAT, false, 3 * Float.BYTES, MemoryUtil.NULL);
        GL20.glEnableVertexAttribArray(0);


    }

    public void render() {
        // Use shaders
        GL20.glUseProgram(shaderProgram);

        // Bind vao for rendering the vbo with attribs given to the vertex shader
        GL30.glBindVertexArray(vao);

        // Draw the triangle
        GL11.glDrawArrays(GL11.GL_TRIANGLES, 0, 3);
    }

    public void destroy() {

    }
}
