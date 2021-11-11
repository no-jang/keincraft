package client.render.vk.surface;

import client.render.Window;
import client.render.vk.instance.VulkanInstance;
import org.lwjgl.glfw.GLFWVulkan;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.vulkan.KHRSurface;

import java.nio.LongBuffer;

import static client.render.vk.debug.VulkanDebug.vkCheck;

public class VulkanSurface {
    private final long handle;

    public VulkanSurface(VulkanInstance instance, Window window) {
        try (MemoryStack stack = MemoryStack.stackPush()) {
            LongBuffer pSurface = stack.mallocLong(1);
            vkCheck(GLFWVulkan.glfwCreateWindowSurface(instance.getInstance(), window.getHandle(), null, pSurface), "Failed to create window surface");
            handle = pSurface.get(0);
        }
    }

    public void destroy(VulkanInstance instance) {
        KHRSurface.vkDestroySurfaceKHR(instance.getInstance(), handle, null);
    }

    public long getHandle() {
        return handle;
    }
}
