package client.render.vk.present;

import client.render.Window;
import client.render.vk.Instance;
import org.lwjgl.glfw.GLFWVulkan;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.vulkan.KHRSurface;

import java.nio.LongBuffer;

import static client.render.vk.Global.vkCheck;

public class Surface {
    private final long handle;

    public Surface(Instance instance, Window window) {
        try (MemoryStack stack = MemoryStack.stackPush()) {
            LongBuffer pSurface = stack.mallocLong(1);
            vkCheck(GLFWVulkan.glfwCreateWindowSurface(instance.getHandle(), window.getHandle(), null, pSurface), "Failed to create window surface");
            handle = pSurface.get(0);
        }
    }

    public void destroy(Instance instance) {
        KHRSurface.vkDestroySurfaceKHR(instance.getHandle(), handle, null);
    }

    public long getHandle() {
        return handle;
    }
}
