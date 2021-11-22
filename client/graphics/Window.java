package client.graphics;

import org.lwjgl.glfw.*;
import org.lwjgl.system.MemoryUtil;

/**
 * Represents a window and glfw context
 */
public class Window {
    private final long handle;

    private int width;
    private int height;

    private boolean framebufferResized;

    /**
     * Creates and opens a new window. Initializes glfw but not opengl, vulkan.
     *
     * @param title  window title
     * @param width  window width
     * @param height window height
     */
    public Window(String title, int width, int height) {
        this.width = width;
        this.height = height;

        GLFWErrorCallback.createPrint(System.err);

        // Initializes glfw and test vulkan support
        if (!GLFW.glfwInit()) {
            throw new RuntimeException("Failed to initialize glfw");
        }

        if (!GLFWVulkan.glfwVulkanSupported()) {
            throw new RuntimeException("Vulkan is not supported. Update your graphics driver or buy a new gpu");
        }

        // Sets window parameters
        GLFW.glfwWindowHint(GLFW.GLFW_VISIBLE, GLFW.GLFW_FALSE); // Don't show window immediately after creation
        //GLFW.glfwWindowHint(GLFW_CLIENT_API, GLFW_NO_API); // Don't initialize opengl context

        // Create new window
        handle = GLFW.glfwCreateWindow(width, height, title, 0, 0);
        if (handle == MemoryUtil.NULL) {
            throw new RuntimeException("Failed to create window");
        }

        // Key callback close application on esc
        GLFW.glfwSetKeyCallback(handle, (window, key, scancode, action, mods) -> {
            if (key == GLFW.GLFW_KEY_ESCAPE && action == GLFW.GLFW_RELEASE) {
                GLFW.glfwSetWindowShouldClose(window, true);
            }
        });

        // Resize framebuffer callback
        GLFW.glfwSetFramebufferSizeCallback(handle, (window, newWidth, newHeight) -> {
            this.width = width;
            this.height = height;
            this.framebufferResized = true;
        });

        // Center window in the middle of the screen
        GLFWVidMode vidMode = GLFW.glfwGetVideoMode(GLFW.glfwGetPrimaryMonitor());
        GLFW.glfwSetWindowPos(handle, (vidMode.width() - width) / 2, (vidMode.height() - height) / 2);

        // Show window on screen
        GLFW.glfwShowWindow(handle);
    }

    /**
     * Process glfw input events
     */
    public void input() {
        GLFW.glfwPollEvents();
    }

    /**
     * Destroy and closes the window and terminates glfw.
     */
    public void destroy() {
        Callbacks.glfwFreeCallbacks(handle);
        GLFW.glfwDestroyWindow(handle);
        GLFW.glfwTerminate();
    }

    /**
     * Check if window should close and the application be stopped
     * Press on the X on the upper window frame
     * Press key ESC
     *
     * @return true if window should close
     */
    public boolean shouldClose() {
        return GLFW.glfwWindowShouldClose(handle);
    }

    /**
     * @return true if framebuffer size changed and the swapchain could be out of date
     */
    public boolean isFramebufferResized() {
        if (framebufferResized) {
            framebufferResized = false;
            return true;
        } else {
            return false;
        }
    }

    /**
     * @return current framebuffer width
     */
    public int getWidth() {
        return width;
    }

    /**
     * @return current framebuffer height
     */
    public int getHeight() {
        return height;
    }

    /**
     * @return internal glfw window handle (id)
     */
    public long getHandle() {
        return handle;
    }
}
