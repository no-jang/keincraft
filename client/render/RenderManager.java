package client.render;

import client.MinecraftClient;
import org.joml.Vector2i;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GLUtil;

public class RenderManager {
    private MinecraftClient client;

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
    }

    public void render() {
        // Set window color to green
        GL11.glClearColor(0.0f, 1.0f, 0.0f, 1.0f);
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);
    }

    public void destroy() {

    }
}
