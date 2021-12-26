package engine.window;

import engine.collections.list.DefaultImmutableList;
import engine.collections.list.ImmutableList;
import engine.graphics.vulkan.instance.properties.InstanceExtension;
import engine.memory.util.EnumBuffers;
import engine.util.pointer.Destroyable;
import org.lwjgl.PointerBuffer;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWVulkan;

import java.util.ArrayList;
import java.util.List;

public class WindowContext implements Destroyable {
    private final ImmutableList<Window> windows;

    public WindowContext() {
        windows = new DefaultImmutableList<>(new ArrayList<>());
    }

    void addWindow(Window window) {
        windows.toMutable().add(window);
    }

    public List<InstanceExtension> getVulkanExtensions() {
        PointerBuffer extensionNames = GLFWVulkan.glfwGetRequiredInstanceExtensions();
        if (extensionNames == null) {
            throw new RuntimeException("Failed to obtain required glfw vulkan instance extension names");
        }

        return EnumBuffers.ofString(extensionNames, InstanceExtension.class);
    }

    public ImmutableList<Window> getWindows() {
        return windows;
    }

    @Override
    public void destroy() {
        GLFW.glfwTerminate();
    }

    public void input() {
        GLFW.glfwPollEvents();
    }
}
