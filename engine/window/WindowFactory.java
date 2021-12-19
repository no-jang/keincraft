package engine.window;

import engine.window.properties.WindowException;
import engine.window.properties.WindowInfo;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.glfw.GLFWVulkan;

public class WindowFactory {
    public WindowContext createWindowContext() {
        GLFWErrorCallback.create((error, description) -> {
            throw new WindowException(error, description);
        });

        if (!GLFW.glfwInit()) {
            throw new RuntimeException("Failed to initialize glfw");
        }

        if (!GLFWVulkan.glfwVulkanSupported()) {
            throw new RuntimeException("Vulkan is not supported");
        }

        GLFW.glfwWindowHint(GLFW.GLFW_VISIBLE, GLFW.GLFW_FALSE);
        GLFW.glfwWindowHint(GLFW.GLFW_CLIENT_API, GLFW.GLFW_NO_API);

        return new WindowContext();
    }

    public Window createWindow(WindowContext context, WindowInfo info) {
        long handle = GLFW.glfwCreateWindow(info.getWidth(), info.getHeight(), info.getTitle(), 0, 0);
        if (handle == 0L) {
            throw new RuntimeException("Failed to create window " + info);
        }

        Window window = new Window(handle, info.getWidth(), info.getHeight());

        GLFW.glfwSetKeyCallback(handle, (windowHandle, key, scancode, action, mods) -> {
            if (key == GLFW.GLFW_KEY_ESCAPE && action == GLFW.GLFW_RELEASE) {
                GLFW.glfwSetWindowShouldClose(windowHandle, true);
            }
        });

        GLFW.glfwSetFramebufferSizeCallback(handle, (windowHandle, width, height) -> {
            window.updateSize(width, height);
        });

        GLFWVidMode videoMode = GLFW.glfwGetVideoMode(GLFW.glfwGetPrimaryMonitor());
        if (videoMode != null) {
            GLFW.glfwSetWindowPos(handle, (videoMode.width() - info.getWidth()) / 2, (videoMode.height() - info.getHeight()) / 2);
        }

        GLFW.glfwShowWindow(handle);
        context.addWindow(window);

        return window;
    }
}
