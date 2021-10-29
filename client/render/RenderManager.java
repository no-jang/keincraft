package client.render;

import client.MinecraftClient;
import client.render.gl.Mesh;
import client.render.gl.Shader;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GLUtil;

import java.io.IOException;

public class RenderManager {
    private final MinecraftClient client;

    private Shader shader;
    private Mesh mesh;

    public RenderManager(MinecraftClient client) {
        this.client = client;
    }

    public void init() {
        // Setup opengl
        GL.createCapabilities();
        GLUtil.setupDebugMessageCallback(System.err);

        // Enable opengl features
        // GL11.glEnable(GL11.GL_DEPTH_TEST);

        // Load shader
        try {
            shader = new Shader("shaders/base.vs", "shaders/base.fs");
        } catch (IOException e) {
            throw new RuntimeException("Unable to load shader", e);
        }

        float[] vertices = {
                -0.5f, 0.5f, 0.0f,
                -0.5f, -0.5f, 0.0f,
                0.5f, -0.5f, 0.0f,
                0.5f, 0.5f, 0.0f,
        };

        int[] indices = {
                0, 1, 3, 3, 1, 2,
        };

        mesh = new Mesh(vertices, indices);
    }

    public void render() {
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);

        shader.bind();
        mesh.draw();
    }

    public void destroy() {
        shader.destroy();
        mesh.destroy();
    }
}
