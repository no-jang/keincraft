package engine.graphics.vulkan.instance;

import engine.graphics.vulkan.instance.properties.Version;
import engine.graphics.vulkan.util.function.VkFunction;
import engine.memory.MemoryContext;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.vulkan.VK11;

import java.nio.IntBuffer;

public final class InstanceUtil {
    public static Version currentVulkanVersion() {
        MemoryStack stack = MemoryContext.getStack();

        IntBuffer vkVersion = stack.mallocInt(1);
        VkFunction.execute(() -> VK11.vkEnumerateInstanceVersion(vkVersion));
        return Version.ofVk(vkVersion.get(0));
    }
}
