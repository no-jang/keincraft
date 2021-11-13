package client.render;

import org.lwjgl.glfw.Callbacks;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.glfw.GLFWVulkan;
import org.lwjgl.system.Platform;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.system.MemoryUtil.NULL;

public class Window {
    private final long handle;

    private int width;
    private int height;

    public Window(int width, int height) {
        this.width = width;
        this.height = height;

        GLFWErrorCallback.createPrint(System.err);

        if (!glfwInit()) {
            throw new RuntimeException("Unable to initialize GLFW");
        }

        if (!GLFWVulkan.glfwVulkanSupported()) {
            throw new RuntimeException("Vulkan is not supported");
        }

        glfwWindowHint(GLFW_CLIENT_API, GLFW_NO_API);

        if (Platform.get() == Platform.MACOSX) {
            glfwWindowHint(GLFW_COCOA_RETINA_FRAMEBUFFER, GLFW_FALSE);
        }

        handle = glfwCreateWindow(width, height, "keincraft", 0, 0);
        if (handle == NULL) {
            throw new RuntimeException("Unable to create GLFW window");
        }

        glfwSetFramebufferSizeCallback(handle, (window, newWidth, newHeight) -> {
            setFramebufferSize(newWidth, newHeight);
        });

        glfwSetKeyCallback(handle, (window, key, scancode, action, mods) -> {
            if (key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE) {
                glfwSetWindowShouldClose(handle, true);
            }
        });

        GLFWVidMode vidMode = glfwGetVideoMode(org.lwjgl.glfw.GLFW.glfwGetPrimaryMonitor());
        glfwSetWindowPos(handle, (vidMode.width() - width) / 2, (vidMode.height() - height) / 2);
    }

    public void input() {
        glfwPollEvents();
    }

    public void destroy() {
        Callbacks.glfwFreeCallbacks(handle);
        glfwDestroyWindow(handle);
        glfwTerminate();
    }

    public boolean shouldClose() {
        return glfwWindowShouldClose(handle);
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public long getHandle() {
        return handle;
    }

    public void setFramebufferSize(int width, int height) {
        this.width = width;
        this.height = height;
    }
}
