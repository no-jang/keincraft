package client.graphics;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL11;

/**
 * Graphics module for managing everything regarded to window, graphics and rendering
 */
public class Graphics {
    private final Window window;

    public Graphics(Window window) {
        this.window = window;

        //GLCapabilities.initialize();

        //GL11.glViewport(0, 0, window.getWidth(), window.getHeight());
    }

    public void render() {
        if (window.isFramebufferResized()) {
            GL11.glViewport(0, 0, window.getWidth(), window.getHeight());
        }

        GL11.glClearColor(0.2f, 0.3f, 0.3f, 1.0f);
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);

        GLFW.glfwSwapBuffers(window.getHandle());
    }

    public void destroy() {

    }
}
