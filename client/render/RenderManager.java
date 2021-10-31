package client.render;

import client.MinecraftClient;
import client.render.gl.Camera;
import client.render.gl.Mesh;
import client.render.gl.Shader;
import client.render.gl.Texture;
import client.render.util.Transformation;
import common.util.math.PosRot;
import org.joml.Matrix4f;
import org.joml.Vector2i;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GLUtil;

import java.io.IOException;

public class RenderManager {
    private final MinecraftClient client;
    private final Camera camera;

    private Shader shader;
    private Mesh mesh;

    public RenderManager(MinecraftClient client) {
        this.client = client;

        this.camera = new Camera();
    }

    public void init() {
        // Setup opengl
        GL.createCapabilities();
        GLUtil.setupDebugMessageCallback(System.err);

        // Enable opengl features
        GL11.glEnable(GL11.GL_DEPTH_TEST);

        // Load shader
        shader = new Shader("shaders/base.vs", "shaders/base.fs");

        float[] vertices = {
                // V0
                -0.5f, 0.5f, 0.5f,
                // V1
                -0.5f, -0.5f, 0.5f,
                // V2
                0.5f, -0.5f, 0.5f,
                // V3
                0.5f, 0.5f, 0.5f,
                // V4
                -0.5f, 0.5f, -0.5f,
                // V5
                0.5f, 0.5f, -0.5f,
                // V6
                -0.5f, -0.5f, -0.5f,
                // V7
                0.5f, -0.5f, -0.5f,
                // For text coords in top face
                // V8: V4 repeated
                -0.5f, 0.5f, -0.5f,
                // V9: V5 repeated
                0.5f, 0.5f, -0.5f,
                // V10: V0 repeated
                -0.5f, 0.5f, 0.5f,
                // V11: V3 repeated
                0.5f, 0.5f, 0.5f,
                // For text coords in right face
                // V12: V3 repeated
                0.5f, 0.5f, 0.5f,
                // V13: V2 repeated
                0.5f, -0.5f, 0.5f,
                // For text coords in left face
                // V14: V0 repeated
                -0.5f, 0.5f, 0.5f,
                // V15: V1 repeated
                -0.5f, -0.5f, 0.5f,
                // For text coords in bottom face
                // V16: V6 repeated
                -0.5f, -0.5f, -0.5f,
                // V17: V7 repeated
                0.5f, -0.5f, -0.5f,
                // V18: V1 repeated
                -0.5f, -0.5f, 0.5f,
                // V19: V2 repeated
                0.5f, -0.5f, 0.5f
        };

        float[] texCoords = new float[]{
                0.0f, 0.0f,
                0.0f, 0.5f,
                0.5f, 0.5f,
                0.5f, 0.0f,
                0.0f, 0.0f,
                0.5f, 0.0f,
                0.0f, 0.5f,
                0.5f, 0.5f,
                // For text coords in top face
                0.0f, 0.5f,
                0.5f, 0.5f,
                0.0f, 1.0f,
                0.5f, 1.0f,
                // For text coords in right face
                0.0f, 0.0f,
                0.0f, 0.5f,
                // For text coords in left face
                0.5f, 0.0f,
                0.5f, 0.5f,
                // For text coords in bottom face
                0.5f, 0.0f,
                1.0f, 0.0f,
                0.5f, 0.5f,
                1.0f, 0.5f
        };

        int[] indices = {
                // Front face
                0, 1, 3, 3, 1, 2,
                // Top Face
                8, 10, 11, 9, 8, 11,
                // Right face
                12, 13, 7, 5, 12, 7,
                // Left face
                14, 15, 6, 4, 14, 6,
                // Bottom face
                16, 18, 19, 17, 16, 19,
                // Back face
                4, 6, 7, 5, 4, 7
        };

        mesh = new Mesh(vertices, texCoords, indices, new Texture("textures/grassblock.png"));
    }

    public void render(Window window) {
        // Clear screen
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);

        // If screen size changed, update opengl viewport
        if(window.isResized()) {
            GL11.glViewport(0, 0, window.getWidth(), window.getHeight());
            camera.updateView(window.getWidth(), window.getHeight());
        }

        shader.bind();
        shader.setInt("texture_sampler", 0);

        camera.update();

        PosRot posRot = new PosRot(0, 0, -2);
        shader.setMat4("projViewModelMatrix", Transformation.makeProjViewModelMatrix(camera.getProjViewMatrix(), posRot.getPosition(), posRot.getRotation()));

        mesh.draw();
    }

    public void destroy() {
        shader.destroy();
        mesh.destroy();
    }

    public Camera getCamera() {
        return camera;
    }
}
