package client.graphics.vk.queue;

import client.graphics.vk.device.PhysicalDevice;
import client.graphics.vk.memory.MemoryContext;
import client.graphics.vk.models.function.CheckFunction;
import client.graphics.vk.surface.Surface;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.vulkan.KHRSurface;
import org.lwjgl.vulkan.VK10;

import java.nio.IntBuffer;
import java.util.Set;

public class QueueFamily {
    private final PhysicalDevice device;
    private final int index;
    private final int queueCount;
    private final Set<QueueCapability> capabilities;

    private Boolean supportsPresentation;

    public QueueFamily(PhysicalDevice device, int index, int queueCount, Set<QueueCapability> capabilities) {
        this.device = device;
        this.index = index;
        this.queueCount = queueCount;
        this.capabilities = capabilities;
    }

    public boolean supportsPresentation(Surface surface) {
        if(supportsPresentation != null) {
            return supportsPresentation;
        }

        MemoryStack stack = MemoryContext.getStack();

        IntBuffer pSupportsPresentation = stack.mallocInt(1);
        CheckFunction.execute(() -> KHRSurface.vkGetPhysicalDeviceSurfaceSupportKHR(device.getReference(), index, surface.getHandle(), pSupportsPresentation));

        supportsPresentation = pSupportsPresentation.get(0) == VK10.VK_TRUE;
        return supportsPresentation;
    }

    public int getIndex() {
        return index;
    }

    public int getQueueCount() {
        return queueCount;
    }

    public Set<QueueCapability> getCapabilities() {
        return capabilities;
    }

    @Override
    public String toString() {
        return "QueueFamily[" +
                ", index=" + index +
                ", queueCount=" + queueCount +
                ", capabilities=" + capabilities +
                ", supportsPresentation=" + supportsPresentation +
                ']';
    }
}
