package engine.window;

import engine.graphics.vulkan.instance.properties.InstanceExtension;
import engine.memory.util.EnumBuffers;
import engine.test.owner.DestroyableOwner;
import org.lwjgl.PointerBuffer;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWVulkan;

import java.util.List;

public class WindowContext extends DestroyableOwner<WindowContext, Window> {
    @Override
    protected void doDestroy() {
        GLFW.glfwTerminate();
    }

    public void input() {
        GLFW.glfwPollEvents();
    }

    public List<InstanceExtension> getVulkanExtensions() {
        PointerBuffer extensionNames = GLFWVulkan.glfwGetRequiredInstanceExtensions();
        if (extensionNames == null) {
            throw new RuntimeException("Failed to obtain required glfw vulkan instance extension names");
        }

        return EnumBuffers.ofString(extensionNames, InstanceExtension.class);
    }
}
