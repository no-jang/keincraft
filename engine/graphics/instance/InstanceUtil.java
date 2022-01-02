package engine.graphics.instance;

import engine.graphics.instance.properties.Version;
import engine.graphics.util.VkFunction;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.vulkan.VK11;

import java.nio.IntBuffer;

public class InstanceUtil {
    public static Version availableVkVersion(MemoryStack stack) {
        IntBuffer versionBuffer = stack.mallocInt(1);
        VkFunction.execute(() -> VK11.vkEnumerateInstanceVersion(versionBuffer));
        return new Version(versionBuffer.get(0));
    }
}
